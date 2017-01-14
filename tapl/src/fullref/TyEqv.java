package fullref;

import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, fullsub.TyEqv<Ty>, bot.TyEqv<Ty>, variant.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyRef(Ty ty1) {
		return ty -> matcher()
				.TyRef(ty2 -> visitTy(ty1).tyEqv(ty2))
				.otherwise(() -> false)
				.visitTy(ty);
	}

	@Override default ITyEqv<Ty> TySource(Ty ty1) {
		return ty -> matcher()
				.TySource(ty2 -> visitTy(ty1).tyEqv(ty2))
				.otherwise(() -> false)
				.visitTy(ty);
	}

	@Override default ITyEqv<Ty> TySink(Ty ty1) {
		return ty -> matcher()
				.TySink(ty2 -> visitTy(ty1).tyEqv(ty2))
				.otherwise(() -> false)
				.visitTy(ty);
	}
}