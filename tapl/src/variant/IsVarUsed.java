package variant;

import variant.termalg.shared.TermAlgQueryWithCtx;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, typed.IsVarUsed<Term, Ty> {
}
