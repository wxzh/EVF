package fullsub;

import java.util.Optional;

import fullsub.termalg.shared.TermAlgDefault;
import library.Zero;

public interface GetBodyFromTmAbs<Term, Ty> extends TermAlgDefault<Term, Ty, Optional<Term>>, typed.GetBodyFromTmAbs<Term, Ty> {
    @Override default Zero<Optional<Term>> m() {
        return typed.GetBodyFromTmAbs.super.m();
    }
}