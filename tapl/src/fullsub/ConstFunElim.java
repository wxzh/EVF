package fullsub;

import fullsub.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term, Ty> extends TermAlgTransform<Term, Ty>, typed.ConstFunElim<Term, Ty> {
  @Override IsVarUsed<Term, Ty> isVarUsed();
  @Override TermShift<Term, Ty> termShift(int d);
  @Override GetBodyFromTmAbs<Term, Ty> getBodyFromTmAbs();
}