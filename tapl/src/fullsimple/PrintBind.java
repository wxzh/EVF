package fullsimple;

import java.util.Optional;

import fullsimple.bindingalg.shared.GBindingAlg;
import utils.IPrint;

public interface PrintBind<Bind, Term, Ty> extends GBindingAlg<Bind, Term, Ty, IPrint<Bind>>, typed.PrintBind<Bind, Ty> {
	Print<Term, Ty, Bind> printTerm();
	@Override PrintTy<Ty, Bind> printTy();

	default IPrint<Bind> TmAbbBind(Term t, Optional<Ty> tyOpt) {
		return ctx -> "= " + printTerm().visitTerm(t).print(ctx) + tyOpt.map(ty -> ": " + printTy().visitTy(ty).print(ctx)).orElse("");
	}

	default IPrint<Bind> TyAbbBind(Ty ty) {
		return ctx -> "= " + printTy().visitTy(ty).print(ctx);
	}
}
