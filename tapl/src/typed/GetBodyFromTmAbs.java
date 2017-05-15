package typed;

import java.util.Optional;

public interface GetBodyFromTmAbs<Term, Ty> extends TermAlgDefault<Term, Ty, Optional<Term>>, varapp.GetBodyFromTmAbs<Term> {
  @Override default Optional<Term> TmAbs(String x, Ty ty, Term t) {
    return Optional.of(t);
  }
}