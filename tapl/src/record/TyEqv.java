package record;

import java.util.List;

import library.Tuple2;
import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;
import utils.ITyEqv;

public interface TyEqv<Ty> extends GTyAlg<Ty, ITyEqv<Ty>> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ITyEqv<Ty> TyRecord(List<Tuple2<String, Ty>> fields1) {
		return ty -> matcher()
				.TyRecord(
						fields2 -> fields1.size() == fields2.size()
								&& fields1.stream()
										.allMatch(pr1 -> fields2.stream()
												.anyMatch(pr2 -> pr2._1.equals(pr1._1)
														&& visitTy(pr1._2).tyEqv(pr2._2))))
				.otherwise(() -> false).visitTy(ty);
	}
}