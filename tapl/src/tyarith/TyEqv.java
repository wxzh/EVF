package tyarith;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, bool.TyEqv<Ty>, nat.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}