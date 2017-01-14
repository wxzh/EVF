package typed;

import library.Zero;
import typed.bindingalg.shared.GBindingAlg;
import typed.termalg.shared.TermAlgDefault;
import typed.tyalg.external.TyAlgMatcher;
import typed.tyalg.shared.GTyAlg;
import utils.Context;
import utils.ITypeof;
import utils.TypeError;

public interface Typeof<Term, Ty, Bind> extends TermAlgDefault<Term, Ty, ITypeof<Ty, Bind>> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();
	TyEqv<Ty> tyEqv();
	GBindingAlg<Bind, Ty, Bind> bindAlg();
	GetTypeFromBind<Bind, Ty> getTypeFromBind();

	@Override default Zero<ITypeof<Ty, Bind>> m() {
		throw new TypeError();
	}

	@Override default ITypeof<Ty, Bind> TmApp(Term t1, Term t2) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			Ty ty2 = visitTerm(t2).typeof(ctx);
			return tyMatcher()
					.TyArr(ty11 -> ty12 -> tyEqv().visitTy(ty2).tyEqv(ty11) ? ty12 : m().empty().typeof(ctx))
					.otherwise(() -> m().empty().typeof(ctx))
					.visitTy(ty1);
		};
	}

	@Override default ITypeof<Ty, Bind> TmVar(int x, int n) {
		return ctx -> getTypeFromBind().visitBind(ctx.getBinding(x));
	}

	@Override default ITypeof<Ty, Bind> TmAbs(String x, Ty ty, Term t) {
		return ctx -> {
			Context<Bind> ctx2 = ctx.addBinding(x, bindAlg().VarBind(ty));
			Ty ty2 = visitTerm(t).typeof(ctx2);
			return tyAlg().TyArr(ty, ty2);
		};
	}
}