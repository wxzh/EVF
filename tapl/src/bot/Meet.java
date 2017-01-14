package bot;

import bot.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IMeet;

public interface Meet<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IMeet<Ty>>, top.Meet<Ty> {
	@Override default Zero<IMeet<Ty>> m() {
		return () -> ty -> alg().TyBot();
	}
}