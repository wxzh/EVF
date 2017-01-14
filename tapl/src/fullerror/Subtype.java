package fullerror;

import fullerror.tyalg.external.TyAlgMatcher;

public interface Subtype<Ty> extends bot.Subtype<Ty> {
	@Override TyEqv<Ty> tyEqv();
	@Override SubtypeAlg<Ty> subtype();
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
