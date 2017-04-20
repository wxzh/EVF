package fullsimple;

import java.util.Optional;

import fullsimple.bindingalg.shared.GBindingAlg;
import utils.Context;
import utils.IPrint;

public interface PrintBind<Bind, Term, Ty> extends GBindingAlg<Bind, Term, Ty, IPrint<Bind>>, typed.PrintBind<Bind, Ty> {
	String printTerm(Term t, Context<Bind> ctx);

	default IPrint<Bind> TmAbbBind(Term t, Optional<Ty> tyOpt) {
		return ctx -> "= " + printTerm(t, ctx) + tyOpt.map(ty -> ": " + printTy(ty,ctx)).orElse("");
	}

	default IPrint<Bind> TyAbbBind(Ty ty) {
		return ctx -> "= " + printTy(ty, ctx);
	}
}
