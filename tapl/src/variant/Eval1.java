package variant;

import java.util.List;

import library.Tuple3;
import variant.termalg.external.TermAlgMatcher;
import variant.termalg.shared.GTermAlg;

public interface Eval1<Term, Ty> extends GTermAlg<Term, Ty, Term>, utils.Eval1<Term> {
	GTermAlg<Term, Ty, Term> alg();
	TermAlgMatcher<Term, Ty, Term> matcher();

  Term termSubstTop(Term s, Term t);
  boolean isVal(Term t);

	@Override default Term TmTag(String label, Term t, Ty ty) {
		return alg().TmTag(label, visitTerm(t), ty);
	}

	default Term TmCase(Term t, List<Tuple3<String, String, Term>> branches) {
		return matcher()
				.TmTag(label -> ty -> t1 -> isVal(t) ?
				    branches.stream()
				      .filter(b -> b._1.equals(label))
				      .findFirst().map(b -> termSubstTop(t, b._3))
				        .orElseGet(() -> { throw new RuntimeException(); })
          : alg().TmCase(visitTerm(t), branches))
				.otherwise(() -> alg().TmCase(visitTerm(t), branches))
				.visitTerm(t);
	}
}