package typed;

import typed.tyalg.external.TyAlgMatcher;
import typed.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>> {
	boolean tyEqv(Ty ty1, Ty ty2);
  TyAlgMatcher<Ty, Boolean> matcher();

	default boolean subtype(Ty ty1, Ty ty2) {
		return tyEqv(ty1, ty2) || visitTy(ty1).subtype(ty2);
	}

  @Override default ISubtype<Ty> TyArr(Ty tyS1, Ty tyS2) {
    return ty -> matcher()
      .TyArr(tyT1 -> tyT2 -> subtype(tyT1, tyS1) && subtype(tyS2, tyT2))
      .otherwise(() -> false)
      .visitTy(ty);
  }
}
