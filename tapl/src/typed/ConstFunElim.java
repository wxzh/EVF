package typed;

import typed.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term, Ty> extends TermAlgTransform<Term, Ty>, varapp.ConstFunElim<Term> {}