package bool;

import bool.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	default IPrint<Bind> TmTrue() {
		return ctx -> "true";
	}

	default IPrint<Bind> TmFalse() {
		return ctx -> "false";
	}

	default IPrint<Bind> TmIf(Term t1, Term t2, Term t3) {
		return ctx -> "if " + visitTerm(t1).print(ctx) + " then " + visitTerm(t2).print(ctx) + " else "
				+ visitTerm(t3).print(ctx);
	}
}
