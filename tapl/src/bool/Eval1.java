package bool;

import bool.termalg.external.TermAlgMatcher;
import bool.termalg.shared.GTermAlg;
import bool.termalg.shared.TermAlgDefault;
import library.Zero;
import utils.NoRuleApplies;

public interface Eval1<Term> extends TermAlgDefault<Term, Term> {
	GTermAlg<Term, Term> alg();
	TermAlgMatcher<Term, Term> matcher();

	@Override default Zero<Term> m() {
		return () -> { throw new NoRuleApplies(); };
	}

	@Override default Term TmIf(Term t1, Term t2, Term t3) {
		return matcher()
				.TmTrue(() -> t2)
				.TmFalse(() -> t3)
				.otherwise(() -> alg().TmIf(visitTerm(t1), t2, t3))
				.visitTerm(t1);
	}
}
