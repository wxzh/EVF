package variant;

import varapp.TmMapCtx;
import variant.termalg.shared.TermAlgTransformWithCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, typed.TmMap<Term, Ty> {
}