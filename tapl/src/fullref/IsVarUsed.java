package fullref;

import fullref.termalg.shared.TermAlgQueryWithCtx;
import library.Monoid;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, typed.IsVarUsed<Term, Ty> {
    @Override default Monoid<Boolean> m() {
        return typed.IsVarUsed.super.m();
    }
}
