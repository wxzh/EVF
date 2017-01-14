package moreextension;

import moreextension.termalg.shared.TermAlgTransformWithCtx;

public interface TermSubst<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, varapp.TermSubst<Term> {
  @Override TermShift<Term, Ty> termShift(int d);
}