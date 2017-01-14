package fullsub;

import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, moreextension.TyEqv<Ty>, top.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
