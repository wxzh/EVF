package rcdsubbot;

import java.util.Optional;

import library.Zero;
import rcdsubbot.termalg.shared.TermAlgDefault;

public interface GetBodyFromTmAbs<Term, Ty> extends TermAlgDefault<Term, Ty, Optional<Term>>, typed.GetBodyFromTmAbs<Term, Ty> {
    @Override default Zero<Optional<Term>> m() {
        return typed.GetBodyFromTmAbs.super.m();
    }
}