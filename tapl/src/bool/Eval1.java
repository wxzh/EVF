package bool;

import bool.termalg.external.TermAlgMatcher;
import bool.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, utils.Eval1<Term> {
	GTermAlg<Term, Term> alg();
	TermAlgMatcher<Term, Term> matcher();

	@Override default Term TmIf(Term t1, Term t2, Term t3) {
		return matcher()
				.TmTrue(() -> t2)
				.TmFalse(() -> t3)
				.otherwise(() -> alg().TmIf(visitTerm(t1), t2, t3))
				.visitTerm(t1);
	}
	@Override default Term TmTrue() {
	  return noRuleApplies();
	}
	@Override default Term TmFalse() {
	  return noRuleApplies();
	}
}
