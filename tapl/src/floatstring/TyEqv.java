package floatstring;

import floatstring.tyalg.external.TyAlgMatcher;
import floatstring.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyFloat() {
		return ty -> matcher()
		    .TyFloat(() -> true)
		    .otherwise(() -> false)
		    .visitTy(ty);
	}

	@Override default ITyEqv<Ty> TyString() {
		return ty -> matcher()
		    .TyString(() -> true)
		    .otherwise(() -> false)
		    .visitTy(ty);
	}
}