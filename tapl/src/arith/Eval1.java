package arith;

import arith.termalg.external.TermAlgMatcher;
import arith.termalg.shared.GTermAlg;
import arith.termalg.shared.TermAlgDefault;
import library.Zero;
import nat.IsNumericVal;

public interface Eval1<Term> extends TermAlgDefault<Term, Term>, bool.Eval1<Term>, nat.Eval1<Term> {
	@Override TermAlgMatcher<Term, Term> matcher();
	@Override GTermAlg<Term, Term> alg();
	@Override IsNumericVal<Term> isNumericVal();

	@Override default Term TmIsZero(Term t) {
		return matcher()
				.TmZero(() -> alg().TmTrue())
				.TmSucc(nv1 -> isNumericVal().visitTerm(nv1) ? alg().TmFalse() : alg().TmIsZero(visitTerm(t)))
				.otherwise(() -> alg().TmIsZero(visitTerm(t)))
				.visitTerm(t);
	}

	@Override default Zero<Term> m() {
		return bool.Eval1.super.m();
	}
}