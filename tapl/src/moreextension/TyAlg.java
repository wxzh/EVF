package moreextension;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends extension.TyAlg<Ty> {
	Ty TyUnit();
}