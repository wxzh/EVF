package fullref;

import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, bot.Subtype<Ty>, fullsub.Subtype<Ty>, variant.Subtype<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

  default ISubtype<Ty> TyRef(Ty ty1) {
    return ty -> matcher()
        .TyRef(ty2 -> subtype(ty1, ty2) && subtype(ty2, ty1))
        .TySource(ty2 -> subtype(ty1, ty2))
        .TySink(ty2 -> subtype(ty2, ty1))
        .otherwise(() -> false)
        .visitTy(ty);
  }

  @Override default ISubtype<Ty> TySource(Ty ty1) {
    return ty -> matcher()
        .TySource(ty2 -> subtype(ty1, ty2))
        .otherwise(() -> false)
        .visitTy(ty);
  }

  @Override default ISubtype<Ty> TySink(Ty ty1) {
    return ty -> matcher()
        .TySink(ty2 -> subtype(ty2, ty1))
        .otherwise(() -> false)
        .visitTy(ty);
  }
}
