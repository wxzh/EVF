package fullsimple;

import fullsimple.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind>
		extends GTyAlg<Ty, IPrint<Bind>>, moreextension.PrintTy<Ty, Bind>, variant.PrintTy<Ty, Bind>, simplebool.PrintTy<Ty, Bind> {
}