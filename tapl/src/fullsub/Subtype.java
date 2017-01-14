package fullsub;

import fullsub.tyalg.external.TyAlgMatcher;

public interface Subtype<Ty> extends top.Subtype<Ty>, record.Subtype<Ty> {
	@Override TyEqv<Ty> tyEqv();
	@Override SubtypeAlg<Ty> subtype();
	@Override TyAlgMatcher<Ty, Boolean> matcher();
}
