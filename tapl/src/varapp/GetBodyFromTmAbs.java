package varapp;

import java.util.Optional;

import library.Zero;
import varapp.termalg.shared.TermAlgDefault;

public interface GetBodyFromTmAbs<Term> extends TermAlgDefault<Term, Optional<Term>> {
  @Override default Zero<Optional<Term>> m() {
    return () -> Optional.empty();
  }
}