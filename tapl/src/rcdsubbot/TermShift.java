package rcdsubbot;

import rcdsubbot.termalg.shared.TermAlgTransformWithCtx;

public interface TermShift<Term, Ty> extends TermAlgTransformWithCtx<Integer, Term, Ty>, typed.TermShift<Term, Ty> {
}