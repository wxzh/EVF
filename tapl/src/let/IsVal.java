package let;

import let.termalg.shared.GTermAlg;

public interface IsVal<Term> extends GTermAlg<Term, Boolean> {
  @Override default Boolean TmLet(String p1, Term p2, Term p3) {
    return false;
  }
}
