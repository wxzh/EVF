package rcdsubbot;

import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, bot.Subtype<Ty>, record.Subtype<Ty>{
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
