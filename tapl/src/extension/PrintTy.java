package extension;

import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>,
    tyarith.PrintTy<Ty, Bind>, record.PrintTy<Ty, Bind>, floatstring.PrintTy<Ty, Bind> {}
