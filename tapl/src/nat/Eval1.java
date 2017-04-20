package nat;

import nat.termalg.external.TermAlgMatcher;
import nat.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, utils.Eval1<Term> {
	TermAlgMatcher<Term, Term> matcher();
	GTermAlg<Term, Term> alg();
	boolean isNumericVal(Term t);

	default Term TmPred(Term t) {
		return matcher()
				.TmZero(() -> t)
				.TmSucc(nv1 -> isNumericVal(nv1) ? nv1 : TmPred(visitTerm(t)))
				.otherwise(() -> alg().TmPred(visitTerm(t)))
				.visitTerm(t);
	}

	default Term TmSucc(Term t) {
		return alg().TmSucc(visitTerm(t));
	}

	default Term TmZero() {
	  return noRuleApplies();
	}
}