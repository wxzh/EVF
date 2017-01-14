package fullerror;

import fullerror.tyalg.external.TyAlgMatcher;
import fullerror.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>>, simplebool.TyEqv<Ty>, bot.TyEqv<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
