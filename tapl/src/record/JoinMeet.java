package record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import library.Tuple2;
import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;
import record.tyalg.shared.TyAlgDefault;
import utils.IJoin;
import utils.IMeet;

public interface JoinMeet<Ty> extends utils.JoinMeet<Ty>{
	TyAlgMatcher<Ty, Ty> matcher();
	GTyAlg<Ty, Ty> alg();

  interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty> {
    @Override default IJoin<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
      return ty -> matcher()
          .TyRecord(fT -> {
            List<String> labelsS = fS.stream().map(pr -> pr._1).collect(Collectors.toList());
            List<String> labelsT = fT.stream().map(pr -> pr._1).collect(Collectors.toList());
            List<String> commonLabels = new ArrayList<>(labelsS);
            commonLabels.retainAll(labelsT);
            List<Tuple2<String, Ty>> commonFields = commonLabels.stream().map(l -> {
              Ty tyS = fS.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> pr._2).get();
              Ty tyT = fT.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> pr._2).get();
              return new Tuple2<>(l, join(tyS, tyT));
            }).collect(Collectors.toList());
            return alg().TyRecord(commonFields);
          })
          .otherwise(() -> m().empty().join(ty))
          .visitTy(ty);
    }
  }

  interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty> {
    @Override default IMeet<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
      return ty -> matcher()
          .TyRecord(fT -> {
            Stream<String> labelsS = fS.stream().map(pr -> pr._1);
            Stream<String> labelsT = fT.stream().map(pr -> pr._1);
            Set<String> allLabels = Stream.concat(labelsS, labelsT).collect(Collectors.toSet());
            List<Tuple2<String, Ty>> allFields = allLabels.stream().map(l -> {
              Ty tyS = fS.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> pr._2).get();
              Ty tyT = fT.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> pr._2).get();
              return new Tuple2<>(l, meet(tyS, tyT));
            }).collect(Collectors.toList());
            return alg().TyRecord(allFields);
          })
          .otherwise(() -> m().empty().meet(ty))
          .visitTy(ty);
    }
  }
}