package fulluntyped;

import fulluntyped.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term>, untyped.TmMap<Term> {
}
