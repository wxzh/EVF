package bot;

import library.Zero;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends top.JoinMeet<Ty>{
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override TyAlg<Ty> alg();

	interface Join<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IJoin<Ty>>, top.JoinMeet.Join<Ty>{}

	interface Meet<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IMeet<Ty>>, top.JoinMeet.Meet<Ty> {
	  @Override default Zero<IMeet<Ty>> m() {
	    return () -> ty -> alg().TyBot();
	  }
	}
}