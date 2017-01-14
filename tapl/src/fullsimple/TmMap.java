package fullsimple;

import fullsimple.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, variant.TmMap<Term, Ty>, moreextension.TmMap<Term> {
}