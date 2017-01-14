package fullerror;

import fullerror.termalg.shared.TermAlgTransformWithCtx;

public interface TermShift<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, typed.TermShift<Term, Ty> {
}