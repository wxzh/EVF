package simplebool;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, typed.TyEqv<Ty>, bool.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}