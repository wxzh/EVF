package fullerror;

import library.Zero;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends top.JoinMeet<Ty> {
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override TyAlg<Ty> alg();

  interface Join<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IJoin<Ty>>, bot.JoinMeet.Join<Ty> {
    @Override default Zero<IJoin<Ty>> m() {
      return bot.JoinMeet.Join.super.m();
    }
  }

  interface Meet<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IMeet<Ty>>, bot.JoinMeet.Meet<Ty> {}
}
