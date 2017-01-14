package tyarith;

import arith.termalg.shared.GTermAlg;
import library.Zero;
import tyarith.tyalg.external.TyAlgMatcher;
import tyarith.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind>
		extends GTermAlg<Term, ITypeof<Ty, Bind>>, bool.Typeof<Term, Ty, Bind>, nat.Typeof<Term, Ty, Bind> {
	@Override GTyAlg<Ty, Ty> tyAlg();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();
	@Override TyEqv<Ty> tyEqv();

	@Override default ITypeof<Ty, Bind> TmIsZero(Term t) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyEqv().visitTy(tyT).tyEqv(tyAlg().TyNat()) ? tyAlg().TyBool() : m().empty().typeof(ctx);
		};
	}

	@Override default Zero<ITypeof<Ty, Bind>> m() {
		return nat.Typeof.super.m();
	}
}