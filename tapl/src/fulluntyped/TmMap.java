package fulluntyped;

import utils.TmMapCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term>, untyped.TmMap<Term> {
}
