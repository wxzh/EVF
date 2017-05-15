package top;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, typed.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyTop() {
		return ty -> matcher()
				.TyTop(() -> true)
				.otherwise(() -> false)
				.visitTy(ty);
	}
}