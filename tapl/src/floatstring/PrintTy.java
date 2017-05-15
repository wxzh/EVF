package floatstring;

import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>> {
	default IPrint<Bind> TyFloat() {
		return ctx -> "Float";
	}

	default IPrint<Bind> TyString() {
		return ctx -> "String";
	}
}
