package let;

import let.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	@Override default IPrint<Bind> TmLet(String x, Term t1, Term t2) {
		return ctx -> "let " + x + "=" + visitTerm(t1).print(ctx) + " in " + visitTerm(t2).print(ctx.addName(x));
	}
}