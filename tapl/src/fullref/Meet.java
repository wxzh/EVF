package fullref;

import fullref.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IMeet;

public interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty>, bot.Meet<Ty>, fullsub.Meet<Ty> {
	@Override default Zero<IMeet<Ty>> m() {
		return bot.Meet.super.m();
	}

	@Override default IMeet<Ty> TyRef(Ty ty1) {
		return ty -> matcher()
				.TyRef(ty2 -> subtype().subtype(ty1, ty2) && subtype().subtype(ty2, ty1)
						? alg().TyRef(ty1) : alg().TySource(meet(ty1, ty2))) // Warning: this is incomplete ...
				.TySource(ty2 -> alg().TySource(meet(ty1, ty2)))
				.TySink(ty2 -> alg().TySink(join(ty1, ty2)))
				.otherwise(() -> m().empty().meet(ty))
				.visitTy(ty);
	}

	@Override default IMeet<Ty> TySource(Ty ty1) {
		return ty -> matcher()
				.TyRef(ty2 -> alg().TySource(meet(ty1, ty2)))
				.TySource(ty2 -> alg().TySource(meet(ty1, ty2)))
				.otherwise(() -> m().empty().meet(ty))
				.visitTy(ty);
	}

	@Override default IMeet<Ty> TySink(Ty ty1) {
		return ty -> matcher()
				.TyRef(ty2 -> alg().TySink(join(ty1, ty2)))
				.TySink(ty2 -> alg().TySink(join(ty1, ty2)))
				.otherwise(() -> m().empty().meet(ty))
				.visitTy(ty);
	}
}
