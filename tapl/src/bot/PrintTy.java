package bot;

import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, top.PrintTy<Ty, Bind> {
	@Override default IPrint<Bind> TyBot() {
		return ctx -> "Bot";
	}
}