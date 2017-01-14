package moreextension;

import extension.termalg.shared.TermAlgTransformWithCtx;
import varapp.TmMapCtx;

public interface TmMap<Term> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term>, extension.TmMap<Term> {
}
