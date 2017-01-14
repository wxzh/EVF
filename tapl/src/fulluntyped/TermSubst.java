package fulluntyped;

import fulluntyped.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term> extends TermAlgTransformWithCtx<Integer, Term>, untyped.TermSubst<Term> {
  @Override TermShift<Term> termShift(int d);
}