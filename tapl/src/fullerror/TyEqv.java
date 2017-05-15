package fullerror;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, simplebool.TyEqv<Ty>, bot.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
