package fullsub;

import fullsub.termalg.external.TermAlgMatcher;
import fullsub.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty, Bind> extends GTermAlg<Term, Ty, Term>, moreextension.Eval1<Term, Ty, Bind>, typed.Eval1<Term, Ty> {
	@Override TermAlgMatcher<Term, Ty, Term> matcher();
	@Override GTermAlg<Term, Ty, Term> alg();
}
