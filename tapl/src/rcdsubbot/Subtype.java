package rcdsubbot;

import rcdsubbot.tyalg.external.TyAlgMatcher;
import rcdsubbot.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, bot.Subtype<Ty>, record.Subtype<Ty>{
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
