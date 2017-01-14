package rcdsubbot;

import library.Monoid;
import rcdsubbot.termalg.shared.TermAlgQueryWithCtx;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, typed.IsVarUsed<Term, Ty> {
    @Override default Monoid<Boolean> m() {
        return typed.IsVarUsed.super.m();
    }
}
