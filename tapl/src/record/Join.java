package record;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import library.Tuple2;
import record.tyalg.shared.TyAlgDefault;
import utils.IJoin;

public interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>>, JoinMeet<Ty>, typed.Join<Ty> {
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