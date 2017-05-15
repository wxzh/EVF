package fulluntyped;

import java.util.Optional;

import library.Zero;

public interface GetBodyFromTmAbs<Term> extends TermAlgDefault<Term, Optional<Term>>, untyped.GetBodyFromTmAbs<Term> {
    @Override default Zero<Optional<Term>> m() {
        return untyped.GetBodyFromTmAbs.super.m();
    }
}