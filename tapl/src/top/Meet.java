package top;

import library.Zero;
import top.tyalg.shared.TyAlgDefault;
import utils.IMeet;

public interface Meet<Ty> extends JoinMeet<Ty>, TyAlgDefault<Ty, IMeet<Ty>> {
	default Zero<IMeet<Ty>> m() {
		throw new RuntimeException();
	}

	@Override default IMeet<Ty> TyArr(Ty tyS1, Ty tyS2) {
		return ty -> matcher()
				.TyArr(tyT1 -> tyT2 -> alg().TyArr(join(tyS1, tyT1), meet(tyS2, tyT2)))
				.otherwise(() -> m().empty().meet(ty)).visitTy(ty);
	}
}