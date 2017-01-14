package typed;

import typed.bindingalg.shared.BindingAlgDefault;
import utils.IPrint;

public interface PrintBind<Bind, Ty> extends BindingAlgDefault<Bind, Ty, IPrint<Bind>>, utils.PrintBind<Bind> {
	PrintTy<Ty, Bind> printTy();

	@Override default IPrint<Bind> VarBind(Ty ty) {
		return ctx -> ": " + printTy().visitTy(ty).print(ctx);
	}
}
