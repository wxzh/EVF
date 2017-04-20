package fullerror;

import fullerror.termalg.shared.TermAlgTransformWithCtx;
import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, simplebool.TmMap<Term, Ty> {
}
