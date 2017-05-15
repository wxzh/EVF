package fullerror;

import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, simplebool.TmMap<Term, Ty> {
}
