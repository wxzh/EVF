package fulluntyped;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> extends untyped.TermAlg<Term>, extension.TermAlg<Term> {}