package untyped;

import untyped.termalg.external.TermAlgMatcher;
import untyped.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, varapp.Eval1<Term> {
	GTermAlg<Term, Term> alg();
	TermAlgMatcher<Term, Term> matcher();

	default Term TmApp(Term t1, Term t2) {
		return matcher()
				.TmAbs(x -> t -> isVal(t2) ? termSubstTop(t2, t)
						: (isVal(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2)))
				.otherwise( () -> isVal(t1) ? alg().TmApp(t1, visitTerm(t2)) : alg().TmApp(visitTerm(t1), t2))
				.visitTerm(t1);
	}
	default Term TmAbs(String p1, Term p2) {
	  return noRuleApplies();
	}
}