package fullerror;

import fullerror.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term, Ty> extends TermAlgTransform<Term, Ty>, typed.ConstFunElim<Term, Ty> {}