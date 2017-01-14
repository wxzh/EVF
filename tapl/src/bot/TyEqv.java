package bot;

import bot.tyalg.external.TyAlgMatcher;
import bot.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, top.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyBot() {
		return ty -> matcher()
				.TyBot(() -> true)
				.otherwise(() -> false)
				.visitTy(ty);
	}
}