package bot;

import bot.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty>, top.SubtypeAlg<Ty> {
	@Override
	default ISubtype<Ty> TyBot() {
		return ty -> true;
	}
}