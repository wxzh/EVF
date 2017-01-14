package typed;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> {
	Ty TyArr(Ty ty1, Ty ty2);
}