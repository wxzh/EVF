package fulluntyped;

import library.Monoid;

public interface IsVarUsed<Term> extends TermAlgQueryWithCtx<Integer, Boolean, Term>, untyped.IsVarUsed<Term> {
    @Override default Monoid<Boolean> m() {
        return untyped.IsVarUsed.super.m();
    }
}
