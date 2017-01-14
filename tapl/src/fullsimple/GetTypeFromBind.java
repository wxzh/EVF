package fullsimple;

import java.util.Optional;

import fullsimple.bindingalg.shared.BindingAlgDefault;

public interface GetTypeFromBind<Bind, Term, Ty> extends BindingAlgDefault<Bind, Term, Ty, Ty>, typed.GetTypeFromBind<Bind, Ty> {
	@Override default Ty TmAbbBind(Term t, Optional<Ty> tyOpt) {
		return tyOpt.orElseGet(() -> m().empty());
	}

	@Override default Ty VarBind(Ty ty) {
		return typed.GetTypeFromBind.super.VarBind(ty);
	}
}
