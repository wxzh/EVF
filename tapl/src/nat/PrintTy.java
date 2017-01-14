package nat;

import nat.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>> {
	default IPrint<Bind> TyNat() {
		return ctx -> "Nat";
	}
}