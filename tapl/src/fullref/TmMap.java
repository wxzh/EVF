package fullref;

import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, fullsimple.TmMap<Term, Ty> {
}
