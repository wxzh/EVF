package fullsimple;

import fullsimple.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>,
  typed.IsVal<Term, Ty>, variant.IsVal<Term, Ty>, moreextension.IsVal<Term, Ty> {}
