package rcdsubbot;

import utils.IPrint;

public interface PrintTy<Ty,Bind> extends GTyAlg<Ty, IPrint<Bind>>, bot.PrintTy<Ty,Bind>, record.PrintTy<Ty,Bind> {}
