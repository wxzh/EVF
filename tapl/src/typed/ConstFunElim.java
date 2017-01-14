package typed;

import typed.termalg.shared.TermAlgTransform;

public interface ConstFunElim<Term, Ty> extends TermAlgTransform<Term, Ty>, varapp.ConstFunElim<Term> {
  @Override IsVarUsed<Term, Ty> isVarUsed();
  @Override TermShift<Term, Ty> termShift(int d);
  @Override GetBodyFromTmAbs<Term, Ty> getBodyFromTmAbs();
}