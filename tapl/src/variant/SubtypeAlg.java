package variant;

import java.util.List;

import library.Tuple2;
import utils.ISubtype;
import variant.tyalg.external.TyAlgMatcher;
import variant.tyalg.shared.GTyAlg;

public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty>, typed.SubtypeAlg<Ty> {
	@Override TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ISubtype<Ty> TyVariant(List<Tuple2<String, Ty>> fS) {
		return ty -> matcher()
				.TyVariant(
						fT -> fS.size() == fT.size() && fS.stream()
								.allMatch(pr2 -> fT.stream()
										.anyMatch(pr1 -> pr1._1.equals(pr2._1) && subtype(pr1._2, pr2._2))))
				.otherwise(() -> false)
				.visitTy(ty);
	}
}