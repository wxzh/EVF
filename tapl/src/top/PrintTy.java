package top;

import top.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, typed.PrintTy<Ty, Bind> {
	@Override default IPrint<Bind> TyTop() {
		return ctx -> "Top";
	}
}