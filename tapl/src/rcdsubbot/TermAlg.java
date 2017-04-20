package rcdsubbot;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends typed.TermAlg<Term, Ty>, record.TermAlg<Term> {}