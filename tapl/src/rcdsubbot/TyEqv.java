package rcdsubbot;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, bot.TyEqv<Ty>, record.TyEqv<Ty>{
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
