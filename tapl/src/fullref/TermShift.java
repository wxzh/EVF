package fullref;

import fullref.termalg.shared.TermAlgTransformWithCtx;

public interface TermShift<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, typed.TermShift<Term, Ty> {
}