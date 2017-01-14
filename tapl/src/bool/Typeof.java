package bool;

import bool.termalg.shared.TermAlgDefault;
import bool.tyalg.external.TyAlgMatcher;
import bool.tyalg.shared.GTyAlg;
import library.Zero;
import utils.ITypeof;
import utils.TypeError;

public interface Typeof<Term, Ty, Bind> extends TermAlgDefault<Term, ITypeof<Ty, Bind>> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();
	TyEqv<Ty> tyEqv();

	@Override default Zero<ITypeof<Ty, Bind>> m() {
		throw new TypeError();
	}

	@Override default ITypeof<Ty, Bind> TmIf(Term t1, Term t2, Term t3) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			if (tyEqv().visitTy(ty1).tyEqv(tyAlg().TyBool())) {
				Ty ty2 = visitTerm(t2).typeof(ctx);
				Ty ty3 = visitTerm(t3).typeof(ctx);
				if (tyEqv().visitTy(ty2).tyEqv(ty3))
					return ty2;
			}
			return m().empty().typeof(ctx);
		};
	}

	@Override default ITypeof<Ty, Bind> TmTrue() {
		return ctx -> tyAlg().TyBool();
	}

	@Override default ITypeof<Ty, Bind> TmFalse() {
		return ctx -> tyAlg().TyBool();
	}
}