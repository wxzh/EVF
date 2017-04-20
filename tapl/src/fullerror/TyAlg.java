package fullerror;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends simplebool.TyAlg<Ty>, bot.TyAlg<Ty> {}