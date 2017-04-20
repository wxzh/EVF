package simplebool;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends typed.TyAlg<Ty>, bool.TyAlg<Ty> {}