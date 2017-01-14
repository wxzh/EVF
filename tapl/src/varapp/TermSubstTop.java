package varapp;

/*
public interface TermSubstTop<Term> {
  Term termShift(int d, Term t);
  Term termSubst(int j, Term s, Term t);
  default Term termSubstTop(Term s, Term t) {
      return termShift(-1, (termSubst(0, (termShift(1, s)), t)));
  }

  interface TermShift<Term> extends G_TermAlgTransform<Integer, Term> {
    int d();
    default Function<Integer, Term> TmVar(int x, int n) {
        return c -> x >= c ? alg().TmVar(x+d(), n+d()) : alg().TmVar(x, n+d());
    }
  }
  interface TermSubst<Term> extends G_TermAlgTransform<Integer, Term> {
    TermShift<Term> termShift();
    default Function<Integer, Term> TmVar(int p1, int p2) {
        return c -> x == j + c ?
    }
  }
}
*/