package moreextension;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, extension.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyUnit() {
		return ty -> matcher().TyUnit(() -> true).otherwise(() -> false).visitTy(ty);
	}
}