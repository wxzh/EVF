package extension;

import extension.tyalg.external.TyAlgMatcher;
import extension.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, tyarith.TyEqv<Ty>, record.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyFloat() {
		return ty -> matcher().TyFloat(() -> true).otherwise(() -> false).visitTy(ty);
	}

	@Override default ITyEqv<Ty> TyString() {
		return ty -> matcher().TyString(() -> true).otherwise(() -> false).visitTy(ty);
	}
}