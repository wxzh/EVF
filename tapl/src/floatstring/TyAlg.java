package floatstring;

import annotation.Visitor;

@Visitor public interface TyAlg<Ty> {
	Ty TyFloat();
	Ty TyString();
}