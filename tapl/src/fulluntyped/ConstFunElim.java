package fulluntyped;

import fulluntyped.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term> extends TermAlgTransform<Term>, untyped.ConstFunElim<Term> {
  @Override IsVarUsed<Term> isVarUsed();
  @Override TermShift<Term> termShift(int d);
  @Override GetBodyFromTmAbs<Term> getBodyFromTmAbs();
}