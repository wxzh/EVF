package variant;

import java.util.List;
import java.util.stream.Collectors;

import library.Tuple3;
import typed.bindingalg.shared.GBindingAlg;
import utils.ITypeof;
import variant.termalg.shared.GTermAlg;
import variant.tyalg.external.TyAlgMatcher;
import variant.tyalg.shared.GTyAlg;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	GTyAlg<Ty, Ty> tyAlg();
	GBindingAlg<Bind, Ty, Bind> bindAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();

	@Override default ITypeof<Ty, Bind> TmTag(String l, Term t, Ty ty) {
		return ctx -> tyMatcher()
				.TyVariant(fieldTys -> fieldTys.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> {
					Ty tyTExpected = pr._2;
					Ty tyT = visitTerm(t).typeof(ctx);
					return tyEqv(tyT,tyTExpected) ? ty : typeError("field does not have expected type");
				}).orElseGet(() -> typeError("label " + l + " not found")))
				.otherwise(() -> typeError("Expected variant type"))
				.visitTy(ty);
	}

	@Override default ITypeof<Ty, Bind> TmCase(Term t, List<Tuple3<String, String, Term>> cases) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyMatcher()
			    .TyVariant(fieldsTys -> {
            if (cases.stream().allMatch(triple -> fieldsTys.stream().anyMatch(pr -> pr._1.equals(triple._1)))) {
            // all case labels are contained
              List<Ty> caseTypes = cases.stream().map(triple -> {
                String li = triple._1;
                String xi = triple._2;
                Term ti = triple._3;
                Ty tyi = fieldsTys.stream().filter(pr -> pr._1.equals(li)).findFirst().get()._2;
                return visitTerm(ti).typeof(ctx.addBinding(xi, bindAlg().VarBind(tyi)));
              }).collect(Collectors.toList());
              // all case terms of the same type
              Ty tyT1 = caseTypes.get(0);
              if (caseTypes.stream().allMatch(ty -> tyEqv(ty,tyT1)))
                return tyT1;
            }
            return typeError("fields do not have the same type");})
			    .otherwise(() -> typeError("Expected variant type"))
			    .visitTy(tyT);
		};
	}
}