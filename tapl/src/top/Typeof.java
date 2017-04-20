package top;

import typed.termalg.shared.GTermAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, typed.Typeof<Term, Ty, Bind> {
	boolean subtype(Ty ty1, Ty ty2);

	@Override default ITypeof<Ty, Bind> TmApp(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);
			return tyMatcher()
					.TyArr(ty11 -> ty12 -> subtype(ty2, ty11) ? ty12 : typeError("parameter type mismatch"))
					.otherwise(() -> typeError("arrow type expected"))
					.visitTy(ty1);
		};
	}
}
