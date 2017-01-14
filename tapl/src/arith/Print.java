package arith;

import arith.termalg.external.TermAlgMatcher;
import arith.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>, bool.Print<Term, Bind>, nat.Print<Term, Bind> {
	@Override TermAlgMatcher<Term, String> matcher();

	default IPrint<Bind> TmIsZero(Term t) {
		return ctx -> "(iszero " + visitTerm(t).print(ctx) + ")";
	}
}