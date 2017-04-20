package tyarith;

import arith.termalg.shared.GTermAlg;
import tyarith.tyalg.external.TyAlgMatcher;
import tyarith.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, bool.Typeof<Term, Ty, Bind>, nat.Typeof<Term, Ty, Bind> {
	@Override GTyAlg<Ty, Ty> tyAlg();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();

	@Override default ITypeof<Ty, Bind> TmIsZero(Term t) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv(tyT,tyAlg().TyNat()) ? tyAlg().TyBool() : typeError("argument of iszero is not a number");
		};
	}
}