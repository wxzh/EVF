package fullerror;

import fullerror.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, simplebool.TmMap<Term, Ty> {
}
