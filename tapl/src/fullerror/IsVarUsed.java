package fullerror;

import library.Monoid;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, typed.IsVarUsed<Term, Ty> {
    default Monoid<Boolean> m() {
        return typed.IsVarUsed.super.m();
    }
}
