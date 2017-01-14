package fullref;

import fullref.tyalg.shared.TyAlgDefault;
import utils.IJoin;

public interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty>, bot.Join<Ty>, fullsub.Join<Ty> {
	default IJoin<Ty> TyRef(Ty ty1) {
		return ty -> matcher()
				.TyRef(ty2 -> subtype().subtype(ty1, ty2) && subtype().subtype(ty2, ty1)
						? alg().TyRef(ty1) : alg().TySource(join(ty1, ty2)) /* Warning: this is incomplete */ )
				.TySource(ty2 -> alg().TySource(join(ty1, ty2)))
				.TySink(ty2 -> alg().TySink(meet(ty1, ty2)))
				.otherwise(() -> m().empty().join(ty))
				.visitTy(ty);
	}

	default IJoin<Ty> TySource(Ty ty1) {
		return ty -> matcher()
				.TySource(ty2 -> alg().TySource(join(ty1, ty2)))
				.TyRef(ty2 -> alg().TySource(join(ty1, ty2)))
				.otherwise(() -> m().empty().join(ty))
				.visitTy(ty);
	}

	default IJoin<Ty> TySink(Ty ty1) {
		return ty -> matcher()
				.TySink(ty2 -> alg().TySink(meet(ty1, ty2)))
				.TyRef(ty2 -> alg().TySink(meet(ty1, ty2)))
				.otherwise(() -> m().empty().join(ty))
				.visitTy(ty);
	}
}