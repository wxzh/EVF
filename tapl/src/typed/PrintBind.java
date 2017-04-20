package typed;

import typed.bindingalg.shared.BindingAlgDefault;
import utils.Context;
import utils.IPrint;

public interface PrintBind<Bind, Ty> extends BindingAlgDefault<Bind, Ty, IPrint<Bind>>, utils.PrintBind<Bind> {
	String printTy(Ty ty, Context<Bind> ctx);

	@Override default IPrint<Bind> VarBind(Ty ty) {
		return ctx -> ": " + printTy(ty, ctx);
	}
}
