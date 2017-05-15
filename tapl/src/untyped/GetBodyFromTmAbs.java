package untyped;

import java.util.Optional;

public interface GetBodyFromTmAbs<Term> extends TermAlgDefault<Term, Optional<Term>>, varapp.GetBodyFromTmAbs<Term> {
  @Override default Optional<Term> TmAbs(String x, Term t) {
    return Optional.of(t);
  }
}