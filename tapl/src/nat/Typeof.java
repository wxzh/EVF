package nat;

import library.Zero;
import nat.termalg.shared.TermAlgDefault;
import nat.tyalg.external.TyAlgMatcher;
import nat.tyalg.shared.GTyAlg;
import utils.ITypeof;
import utils.TypeError;

public interface Typeof<Term, Ty, Bind> extends TermAlgDefault<Term, ITypeof<Ty, Bind>> {
	GTyAlg<Ty, Ty> tyAlg();
	TyAlgMatcher<Ty, Ty> tyMatcher();
	TyEqv<Ty> tyEqv();

	@Override default Zero<ITypeof<Ty, Bind>> m() {
		throw new TypeError();
	}

	@Override default ITypeof<Ty, Bind> TmZero() {
		return ctx -> tyAlg().TyNat();
	}

	@Override default ITypeof<Ty, Bind> TmSucc(Term t) {
		return ctx -> {
			Ty tyNat = tyAlg().TyNat();
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv().visitTy(tyT).tyEqv(tyNat) ? tyNat : m().empty().typeof(ctx);
		};
	}

	@Override default ITypeof<Ty, Bind> TmPred(Term t) {
		return TmSucc(t);
	}
}