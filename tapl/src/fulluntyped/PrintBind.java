package fulluntyped;

import fulluntyped.bindingalg.shared.BindingAlgDefault;
import utils.IPrint;

public interface PrintBind<Bind, Term> extends BindingAlgDefault<Bind, Term, IPrint<Bind>>, utils.PrintBind<Bind>{
	Print<Term, Bind> printTerm();

	default IPrint<Bind> TmAbbBind(Term t) {
		return ctx -> printTerm().visitTerm(t).print(ctx);
	}
}
