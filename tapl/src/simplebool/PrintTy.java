package simplebool;

import simplebool.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, typed.PrintTy<Ty, Bind>, bool.PrintTy<Ty, Bind> {
	default IPrint<Bind> TyArr(Ty t1, Ty t2) {
		return ctx -> "(" + visitTy(t1).print(ctx) + " -> " + visitTy(t2).print(ctx) + ")";
	}
}