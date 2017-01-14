package fullsub;

import fullsub.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, moreextension.TmMap<Term>, typed.TmMap<Term, Ty> {
}
