package fullsub;

import fullsub.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, typed.TermSubst<Term, Ty> {
  @Override TermShift<Term, Ty> termShift(int d);
}