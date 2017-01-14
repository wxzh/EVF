package nat;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> {
	Ty TyNat();
}