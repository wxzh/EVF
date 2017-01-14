package moreextension;

import moreextension.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, extension.PrintTy<Ty, Bind> {
	default IPrint<Bind> TyUnit() {
		return ctx -> "Unit";
	}
}