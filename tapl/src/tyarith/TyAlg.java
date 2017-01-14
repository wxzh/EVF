package tyarith;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends bool.TyAlg<Ty>, nat.TyAlg<Ty> {
}