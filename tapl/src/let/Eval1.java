package let;

import let.termalg.shared.GTermAlg;

public interface Eval1<Term, Bind> extends GTermAlg<Term,Term>, varapp.Eval1<Term> {
	GTermAlg<Term, Term> alg();

	@Override default Term TmLet(String x, Term t1, Term t2) {
		return isVal(t1) ? termSubstTop(t1, t2) : alg().TmLet(x, visitTerm(t1), t2);
	}
}
