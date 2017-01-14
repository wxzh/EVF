package fullref;

import fullref.tyalg.external.TyAlgMatcher;

public interface Subtype<Ty> extends bot.Subtype<Ty>, fullsub.Subtype<Ty>, variant.Subtype<Ty> {
	@Override TyEqv<Ty> tyEqv();
	@Override SubtypeAlg<Ty> subtype();
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
