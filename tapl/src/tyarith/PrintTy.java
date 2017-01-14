package tyarith;

import tyarith.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, bool.PrintTy<Ty, Bind>, nat.PrintTy<Ty, Bind> {
}