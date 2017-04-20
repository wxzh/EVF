package variant;

import java.util.List;

import library.Tuple3;
import variant.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, typed.IsVal<Term, Ty> {
  default Boolean TmTag(String x, Term t, Ty ty) {
		return visitTerm(t);
	}
  default Boolean TmCase(Term p1, List<Tuple3<String, String, Term>> p2) {
    return false;
  }
}
