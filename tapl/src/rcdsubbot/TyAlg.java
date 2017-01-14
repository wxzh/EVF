package rcdsubbot;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends bot.TyAlg<Ty>, record.TyAlg<Ty>{
}
