package extension;

import extension.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term> extends TermAlgTransformWithCtx<Integer, Term>, varapp.TermSubst<Term> {
  @Override TermShift<Term> termShift(int d);
}