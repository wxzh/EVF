package utils;

import library.Zero;
import utils.bindingalg.shared.BindingAlgDefault;

public interface PrintBind<Bind> extends BindingAlgDefault<Bind, IPrint<Bind>> {
	@Override default Zero<IPrint<Bind>> m() {
		return () -> ctx -> "";
	}
}
