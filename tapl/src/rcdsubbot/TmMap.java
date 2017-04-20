package rcdsubbot;

import rcdsubbot.termalg.shared.TermAlgTransformWithCtx;
import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, typed.TmMap<Term, Ty> {}