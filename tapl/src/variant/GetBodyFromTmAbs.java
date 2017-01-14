package variant;

import java.util.Optional;

import variant.termalg.shared.TermAlgDefault;

public interface GetBodyFromTmAbs<Term, Ty> extends TermAlgDefault<Term, Ty, Optional<Term>>, typed.GetBodyFromTmAbs<Term, Ty> {
}