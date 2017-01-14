package typed;

import typed.tyalg.external.TyAlgMatcher;
import typed.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyArr(Ty tyT1, Ty tyT2) {
		return ty -> matcher()
				.TyArr(tyS1 -> tyS2 -> visitTy(tyT1).tyEqv(tyS1) && visitTy(tyT2).tyEqv(tyS2))
				.otherwise(() -> false)
				.visitTy(ty);
	}
}