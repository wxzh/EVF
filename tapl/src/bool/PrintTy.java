package bool;

import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>> {
	default IPrint<Bind> TyBool() {
		return ctx -> "Bool";
	}
}