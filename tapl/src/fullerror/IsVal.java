package fullerror;

import fullerror.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, simplebool.IsVal<Term, Ty> {
  default Boolean TmError() {
    return false;
  }
  default Boolean TmTry(Term p1, Term p2) {
    return false;
  }
}