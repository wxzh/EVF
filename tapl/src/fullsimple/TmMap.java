package fullsimple;

import java.util.function.Function;

import utils.TmMapCtx;

public interface TmMap<Term, Ty> extends TermAlgTransformWithCtx<TmMapCtx<Term>, Term, Ty>, let.TmMap<Term>, typed.TmMap<Term,Ty> {
  default Function<TmMapCtx<Term>, Term> TmLet(String x, Term bind, Term body) {
    return let.TmMap.super.TmLet(x, bind, body);
  }
}