package arith;

import arith.termalg.external.TermAlgMatcher;
import arith.termalg.shared.GTermAlg;

public interface Eval1<Term> extends GTermAlg<Term, Term>, bool.Eval1<Term>, nat.Eval1<Term> {
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override GTermAlg<Term, Term> alg();

	default Term TmIsZero(Term t) {
		return matcher()
				.TmZero(() -> alg().TmTrue())
				.TmSucc(nv1 -> isNumericVal(nv1) ? alg().TmFalse() : alg().TmIsZero(visitTerm(t)))
				.otherwise(() -> alg().TmIsZero(visitTerm(t)))
				.visitTerm(t);
	}
}