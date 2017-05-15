package tyarith;

import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, bool.PrintTy<Ty, Bind>, nat.PrintTy<Ty, Bind> {
}