package fullref;

import fullref.tyalg.shared.GTyAlg;
import utils.IPrint;

public interface PrintTy<Ty, Bind> extends GTyAlg<Ty, IPrint<Bind>>, fullsimple.PrintTy<Ty, Bind>, fullerror.PrintTy<Ty, Bind> {
	@Override default IPrint<Bind> TyRef(Ty ty) {
		return ctx -> "Ref " + visitTy(ty).print(ctx);
	}

	@Override default IPrint<Bind> TySource(Ty ty) {
		return ctx -> "Source " + visitTy(ty).print(ctx);
	}

	@Override default IPrint<Bind> TySink(Ty ty) {
		return ctx -> "Sink " + visitTy(ty).print(ctx);
	}
}