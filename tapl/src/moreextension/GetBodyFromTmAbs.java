package moreextension;

import java.util.Optional;

import library.Zero;

public interface GetBodyFromTmAbs<Term, Ty> extends TermAlgDefault<Term, Ty, Optional<Term>>, varapp.GetBodyFromTmAbs<Term> {
    @Override default Zero<Optional<Term>> m() {
        return varapp.GetBodyFromTmAbs.super.m();
    }
}