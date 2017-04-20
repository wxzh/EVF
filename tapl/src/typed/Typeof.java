package typed;

import typed.bindingalg.shared.GBindingAlg;
import typed.termalg.shared.GTermAlg;
import typed.tyalg.external.TyAlgMatcher;
import typed.tyalg.shared.GTyAlg;
import utils.Context;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, Ty, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();
	GBindingAlg<Bind, Ty, Bind> bindAlg();
	Ty getTypeFromBind(Bind bind);

	@Override default ITypeof<Ty, Bind> TmApp(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);
			return tyMatcher()
					.TyArr(ty11 -> ty12 -> tyEqv(ty2, ty11) ? ty12 : typeError("parameter mismatch"))
					.otherwise(() -> typeError("arrow type expected"))
					.visitTy(ty1);
		};
	}

	@Override default ITypeof<Ty, Bind> TmVar(int x, int n) {
		return ctx -> getTypeFromBind(ctx.getBinding(x));
	}

	@Override default ITypeof<Ty, Bind> TmAbs(String x, Ty ty, Term t) {
		return ctx -> {
			Context<Bind> ctx2 = ctx.addBinding(x, bindAlg().VarBind(ty));
			Ty ty2 = visitTerm(t).typeof(ctx2);
			return tyAlg().TyArr(ty, ty2);
		};
	}
}