package top;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends typed.TyAlg<Ty> {
	Ty TyTop();
}
