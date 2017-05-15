package typed;

import library.Zero;

public interface GetTypeFromBind<Bind, Ty> extends BindingAlgDefault<Bind, Ty, Ty> {
	@Override default Zero<Ty> m() {
		throw new RuntimeException();
	}

	@Override default Ty VarBind(Ty ty) {
		return ty;
	}
}
