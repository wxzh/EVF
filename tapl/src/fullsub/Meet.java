package fullsub;

import fullsub.tyalg.shared.TyAlgDefault;
import library.Zero;
import utils.IMeet;

public interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>>, JoinMeet<Ty>, top.Meet<Ty>, record.Meet<Ty> {
	@Override default Zero<IMeet<Ty>> m() {
		return top.Meet.super.m();
	}
}
