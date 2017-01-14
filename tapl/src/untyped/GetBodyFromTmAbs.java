package untyped;

import java.util.Optional;

import untyped.termalg.shared.TermAlgDefault;

public interface GetBodyFromTmAbs<Term> extends TermAlgDefault<Term, Optional<Term>>, varapp.GetBodyFromTmAbs<Term> {
  @Override default Optional<Term> TmAbs(String x, Term t) {
    return Optional.of(t);
  }
}