package bool;

import bool.tyalg.external.TyAlgMatcher;
import bool.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyBool() {
		return ty -> matcher()
				.TyBool(() -> true)
				.otherwise(() -> false)
				.visitTy(ty);
	}
}