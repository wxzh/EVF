package fullsub;

import fullsub.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, moreextension.IsVal<Term, Ty>, typed.IsVal<Term, Ty> {}
