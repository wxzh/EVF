package nat;

import nat.termalg.external.TermAlgMatcher;
import nat.termalg.shared.GTermAlg;
import utils.Context;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>> {
	TermAlgMatcher<Term, String> matcher();

	default IPrint<Bind> TmZero() {
		return ctx -> "0";
	}

	default IPrint<Bind> TmSucc(Term t) {
		return ctx -> printConsecutiveSuccs(1, t, ctx);
	}

	default IPrint<Bind> TmPred(Term t) {
		return ctx -> "(pred " + visitTerm(t).print(ctx) + ")";
	}

	default String printConsecutiveSuccs(int i, Term t, Context<Bind> ctx) {
		return matcher()
				.TmSucc(t1 -> printConsecutiveSuccs(i+1, t1, ctx))
				.TmZero(() -> String.valueOf(i))
				.otherwise(() -> "(succ " + visitTerm(t).print(ctx) + ")")
				.visitTerm(t);
	}
}
