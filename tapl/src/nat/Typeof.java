package nat;

import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	TyAlg<Ty> tyAlg();
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