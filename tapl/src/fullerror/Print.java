package fullerror;

import fullerror.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, simplebool.Print<Term, Ty, Bind> {
	@Override default IPrint<Bind> TmTry(Term t1, Term t2) {
		return ctx -> "try " + visitTerm(t1).print(ctx) + " with " + visitTerm(t2).print(ctx);
	}

	@Override default IPrint<Bind> TmError() {
		return ctx -> "error";
	}
}