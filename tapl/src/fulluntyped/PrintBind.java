package fulluntyped;

import fulluntyped.bindingalg.shared.BindingAlgDefault;
import utils.Context;
import utils.IPrint;

public interface PrintBind<Bind, Term> extends BindingAlgDefault<Bind, Term, IPrint<Bind>>, utils.PrintBind<Bind>{
	String printTerm(Term t, Context<Bind> ctx);

	default IPrint<Bind> TmAbbBind(Term t) {
		return ctx -> printTerm(t, ctx);
	}
}
