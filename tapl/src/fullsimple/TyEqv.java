package fullsimple;

import fullsimple.tyalg.external.TyAlgMatcher;
import fullsimple.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, moreextension.TyEqv<Ty>, variant.TyEqv<Ty>, simplebool.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}