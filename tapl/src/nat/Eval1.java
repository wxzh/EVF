package nat;

import library.Zero;
import nat.termalg.external.TermAlgMatcher;
import nat.termalg.shared.GTermAlg;
import nat.termalg.shared.TermAlgDefault;
import utils.NoRuleApplies;

public interface Eval1<Term> extends TermAlgDefault<Term, Term> {
	TermAlgMatcher<Term, Term> matcher();
	IsNumericVal<Term> isNumericVal();
	GTermAlg<Term, Term> alg();

	@Override default Zero<Term> m() {
		return () -> { throw new NoRuleApplies(); };
	}

	@Override default Term TmPred(Term t) {
		return matcher()
				.TmZero(() -> t)
				.TmSucc(nv1 -> isNumericVal().visitTerm(nv1) ? nv1 : TmPred(visitTerm(t)))
				.otherwise(() -> alg().TmPred(visitTerm(t)))
				.visitTerm(t);
	}

	@Override default Term TmSucc(Term t) {
		return alg().TmSucc(visitTerm(t));
	}
}