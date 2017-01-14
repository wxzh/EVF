package top;

import top.tyalg.external.TyAlgMatcher;

public interface Subtype<Ty> extends typed.Subtype<Ty> {
	@Override TyEqv<Ty> tyEqv();
	@Override SubtypeAlg<Ty> subtype();
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default boolean subtype(Ty ty1, Ty ty2) {
		return typed.Subtype.super.subtype(ty1, ty2)
				|| matcher().TyTop(() -> true).otherwise(() -> false).visitTy(ty2);
	}
}
