package nat;

import nat.termalg.shared.GTermAlg;
import nat.tyalg.external.TyAlgMatcher;
import nat.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();

	@Override default ITypeof<Ty, Bind> TmZero() {
		return ctx -> tyAlg().TyNat();
	}

	@Override default ITypeof<Ty, Bind> TmSucc(Term t) {
		return ctx -> {
			Ty tyNat = tyAlg().TyNat();
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv(tyT,tyNat) ? tyNat : typeError("argument of succ is not a number");
		};
	}

	@Override default ITypeof<Ty, Bind> TmPred(Term t) {
		return TmSucc(t);
	}
}