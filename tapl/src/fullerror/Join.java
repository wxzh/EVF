package fullerror;

import fullerror.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IJoin;

public interface Join<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IJoin<Ty>>, bot.Join<Ty> {
	@Override default Zero<IJoin<Ty>> m() {
		return bot.Join.super.m();
	}
}