package record;

import java.util.List;

import library.Tuple2;
import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface Subtype<Ty> extends GTyAlg<Ty, ISubtype<Ty>> {
  TyAlgMatcher<Ty, Boolean> matcher();
  boolean subtype(Ty ty1, Ty ty2);

  @Override default ISubtype<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
    return ty -> matcher()
        .TyRecord(fT -> fT.stream().allMatch(pr2 -> fS.stream().anyMatch(pr1 -> pr1._1.equals(pr2._1) && subtype(pr1._2, pr2._2))))
        .otherwise(() -> false)
        .visitTy(ty);
  }
}
