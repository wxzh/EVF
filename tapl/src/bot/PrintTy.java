package bot;

import bot.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, typed.PrintTy<Ty, Bind> {
	@Override default IPrint<Bind> TyBot() {
		return ctx -> "Bot";
	}

	@Override default IPrint<Bind> TyTop() {
		return ctx -> "Top";
	}
}