package typed;

import typed.tyalg.external.TyAlgMatcher;
import typed.tyalg.shared.GTyAlg;
import utils.ISubtype;


public interface SubtypeAlg<Ty> extends GTyAlg<Ty, ISubtype<Ty>>, Subtype<Ty> {
	TyAlgMatcher<Ty, Boolean> matcher();

	@Override default ISubtype<Ty> TyArr(Ty tyS1, Ty tyS2) {
		return ty -> matcher()
				.TyArr(tyT1 -> tyT2 -> subtype(tyT1, tyS1) && subtype(tyS2, tyT2))
				.otherwise(() -> false)
				.visitTy(ty);
	}
}