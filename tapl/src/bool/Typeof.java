package bool;

import bool.termalg.shared.GTermAlg;
import bool.tyalg.external.TyAlgMatcher;
import bool.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();

	@Override default ITypeof<Ty, Bind> TmIf(Term t1, Term t2, Term t3) {
		return ctx -> {
			Ty ty1 = visitTerm(t1).typeof(ctx);
			if (tyEqv(ty1,tyAlg().TyBool())) {
				Ty ty2 = visitTerm(t2).typeof(ctx);
				Ty ty3 = visitTerm(t3).typeof(ctx);
				if (tyEqv(ty2,ty3))
					return ty2;
			}
			return typeError("guard of conditional not a boolean");
		};
	}

	@Override default ITypeof<Ty, Bind> TmTrue() {
		return ctx -> tyAlg().TyBool();
	}

	@Override default ITypeof<Ty, Bind> TmFalse() {
		return ctx -> tyAlg().TyBool();
	}
}