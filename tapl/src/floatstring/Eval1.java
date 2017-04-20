package floatstring;

import floatstring.termalg.external.TermAlgMatcher;
import floatstring.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, utils.Eval1<Term> {
	TermAlgMatcher<Term, Term> matcher();
	GTermAlg<Term, Term> alg();

	default Term TmTimesFloat(Term t1, Term t2) {
		return matcher()
				.TmFloat(f1 -> matcher().TmFloat(f2 -> alg().TmFloat(f1 * f2))
						.otherwise(() -> alg().TmTimesFloat(t1, visitTerm(t2)))
						.visitTerm(t2))
				.otherwise(() -> alg().TmTimesFloat(visitTerm(t1), t2))
				.visitTerm(t1);
	}
	default Term TmFloat(float p1) {
	  return noRuleApplies();
	}
	default Term TmString(String p1) {
	  return noRuleApplies();
	}
}
