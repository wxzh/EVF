package rcdsubbot;

import typed.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, typed.TmMap<Term, Ty> {
}