package rcdsubbot;

import rcdsubbot.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term, Ty> extends TermAlgTransform<Term, Ty>, typed.ConstFunElim<Term, Ty> {}