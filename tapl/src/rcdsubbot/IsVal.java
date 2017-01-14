package rcdsubbot;

import rcdsubbot.termalg.shared.GTermAlg;

public interface IsVal<Term, Ty> extends GTermAlg<Term, Ty, Boolean>, typed.IsVal<Term, Ty>, record.IsVal<Term> {
}
