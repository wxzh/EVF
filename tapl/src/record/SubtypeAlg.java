package record;

import java.util.List;

import library.Tuple2;
import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;
import utils.ISubtype;

public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty>, typed.SubtypeAlg<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ISubtype<Ty> TyRecord(List<Tuple2<String, Ty>> fS) {
		return ty -> matcher()
				.TyRecord(
						fT -> fS.size() == fT.size() && fT.stream()
								.allMatch(pr2 -> fS.stream()
										.anyMatch(pr1 -> pr1._1.equals(pr2._1) && subtype(pr1._2, pr2._2))))
				.otherwise(() -> false)
				.visitTy(ty);
	}
}