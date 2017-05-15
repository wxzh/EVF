package fullsub;

import utils.IPrint;

public interface PrintTy<Ty, Bind>
		extends GTyAlg<Ty, IPrint<Bind>>, moreextension.PrintTy<Ty, Bind>, top.PrintTy<Ty, Bind> {
}
