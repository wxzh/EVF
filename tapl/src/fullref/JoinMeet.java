package fullref;

import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.shared.GTyAlg;
import fullref.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends bot.JoinMeet<Ty>, fullsub.JoinMeet<Ty> {
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override GTyAlg<Ty, Ty> alg();

  interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty>, bot.JoinMeet.Join<Ty>, fullsub.JoinMeet.Join<Ty> {
    default IJoin<Ty> TyRef(Ty ty1) {
      return ty -> matcher()
          .TyRef(ty2 -> subtype(ty1, ty2) && subtype(ty2, ty1)
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

  interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty>, bot.JoinMeet.Meet<Ty>, fullsub.JoinMeet.Meet<Ty> {
    @Override default Zero<IMeet<Ty>> m() {
      return bot.JoinMeet.Meet.super.m();
    }

    @Override default IMeet<Ty> TyRef(Ty ty1) {
      return ty -> matcher()
          .TyRef(ty2 -> subtype(ty1, ty2) && subtype(ty2, ty1)
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
}
