package typed;

import library.Zero;
import typed.tyalg.shared.TyAlgDefault;
import utils.IMeet;

public interface Meet<Ty> extends TyAlgDefault<Ty, IMeet<Ty>> {
	default Zero<IMeet<Ty>> m() {
		throw new RuntimeException();
	}
}
