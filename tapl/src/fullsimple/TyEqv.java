package fullsimple;

import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, moreextension.TyEqv<Ty>, variant.TyEqv<Ty>, simplebool.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}