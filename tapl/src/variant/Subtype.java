package variant;

import java.util.List;

import library.Tuple2;
import utils.ISubtype;
import variant.tyalg.external.TyAlgMatcher;
import variant.tyalg.shared.GTyAlg;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();
	boolean subtype(Ty ty1, Ty ty2);

  @Override default ISubtype<Ty> TyVariant(List<Tuple2<String, Ty>> fS) {
    return ty -> matcher()
        .TyVariant(fT -> fS.size() == fT.size() && fS.stream()
            .allMatch(pr2 -> fT.stream()
                    .anyMatch(pr1 -> pr1._1.equals(pr2._1) && subtype(pr1._2, pr2._2))))
        .otherwise(() -> false)
        .visitTy(ty);
  }
}
