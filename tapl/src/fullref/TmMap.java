package fullref;

import fullref.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, fullsimple.TmMap<Term, Ty> {
}
