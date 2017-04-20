package fullsub;

import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.GTyAlg;
import fullsub.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends top.JoinMeet<Ty>, record.JoinMeet<Ty> {
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override GTyAlg<Ty, Ty> alg();

  interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty>, top.JoinMeet.Join<Ty>, record.JoinMeet.Join<Ty> {
    default Zero<IJoin<Ty>> m() {
      return top.JoinMeet.Join.super.m();
    }
  }

  interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty>, top.JoinMeet.Meet<Ty>, record.JoinMeet.Meet<Ty> {
    default Zero<IMeet<Ty>> m() {
      return top.JoinMeet.Meet.super.m();
    }
  }
}