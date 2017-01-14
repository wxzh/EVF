package extension;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> extends tyarith.TyAlg<Ty>, record.TyAlg<Ty> {
	Ty TyFloat();
	Ty TyString();
}