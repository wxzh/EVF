package top;

import library.Zero;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends utils.JoinMeet<Ty> {
	TyAlg<Ty> alg();
  TyAlgMatcher<Ty, Ty> matcher();

  interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty> {
    default Zero<IJoin<Ty>> m() {
      return () -> ty -> alg().TyTop();
    }

    default IJoin<Ty> TyArr(Ty tyS1, Ty tyS2) {
      return ty -> matcher()
          .TyArr(tyT1 -> tyT2 -> alg().TyArr(meet(tyS1, tyT1), join(tyS2, tyT2)))
          .otherwise(() -> m().empty().join(ty))
          .visitTy(ty);
    }
  }

  interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty> {
    default Zero<IMeet<Ty>> m() {
      return () -> ty -> notFound();
    }

    default IMeet<Ty> TyArr(Ty tyS1, Ty tyS2) {
      return ty -> matcher()
          .TyArr(tyT1 -> tyT2 -> alg().TyArr(join(tyS1, tyT1), meet(tyS2, tyT2)))
          .otherwise(() -> m().empty().meet(ty))
          .visitTy(ty);
    }
  }
}