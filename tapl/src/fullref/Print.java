package fullref;

import fullref.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, fullsimple.Print<Term, Ty, Bind> {
	@Override default IPrint<Bind> TmDeref(Term t) {
		return ctx -> "!" + visitTerm(t).print(ctx);
	}

	@Override default IPrint<Bind> TmLoc(int l) {
		return ctx -> "<loc #" + l + ">";
	}

	@Override default IPrint<Bind> TmAssign(Term t1, Term t2) {
		return ctx -> visitTerm(t1).print(ctx) + " := " + visitTerm(t2).print(ctx);
	}

	@Override default IPrint<Bind> TmRef(Term t) {
		return ctx -> "ref " + visitTerm(t).print(ctx);
	}
}