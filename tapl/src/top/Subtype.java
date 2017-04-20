package top;

import top.tyalg.external.TyAlgMatcher;
import top.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, typed.Subtype<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default boolean subtype(Ty ty1, Ty ty2) {
		return tyEqv(ty1,ty2) || matcher()
		    .TyTop(() -> true)
		    .otherwise(() -> visitTy(ty1).subtype(ty2))
		    .visitTy(ty2);
	}

  @Override default ISubtype<Ty> TyTop() {
    return ty -> matcher()
        .TyTop(() -> true)
        .otherwise(() -> false)
        .visitTy(ty);
  }
}
