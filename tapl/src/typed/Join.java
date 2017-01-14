package typed;

import library.Zero;
import record.tyalg.shared.TyAlgDefault;
import utils.IJoin;

public interface Join<Ty> extends TyAlgDefault<Ty, IJoin<Ty>> {
	@Override default Zero<IJoin<Ty>> m() {
		throw new RuntimeException();
	}
}
