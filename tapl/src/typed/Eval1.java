package typed;

import typed.termalg.external.TermAlgMatcher;
import typed.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty> extends GTermAlg<Term, Ty, Term>, varapp.Eval1<Term> {
	GTermAlg<Term, Ty, Term> alg();
	TermAlgMatcher<Term, Ty, Term> matcher();

	default Term TmApp(Term t1, Term t2) {
		return matcher()
				.TmAbs(x -> ty -> t -> isVal(t2) ? termSubstTop(t2, t)
						: (isVal(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2)))
				.otherwise(() -> isVal(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2))
				.visitTerm(t1);
	}
	default Term TmAbs(String p1, Ty p2, Term p3) {
	  return noRuleApplies();
	}
}