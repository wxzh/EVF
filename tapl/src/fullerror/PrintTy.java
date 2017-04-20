package fullerror;

import fullerror.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, simplebool.PrintTy<Ty, Bind>, bot.PrintTy<Ty, Bind> {}
