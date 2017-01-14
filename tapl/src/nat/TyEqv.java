package nat;

import nat.tyalg.external.TyAlgMatcher;
import nat.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyNat() {
		return ty -> matcher()
				.TyNat(() -> true)
				.otherwise(() -> false)
				.visitTy(ty);
	}
}