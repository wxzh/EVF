package untyped;

import untyped.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term> extends TermAlgTransform<Term>, varapp.ConstFunElim<Term> {
  @Override IsVarUsed<Term> isVarUsed();
  @Override TermShift<Term> termShift(int d);
  @Override GetBodyFromTmAbs<Term> getBodyFromTmAbs();
}