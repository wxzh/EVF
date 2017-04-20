package simplebool;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends typed.TermAlg<Term, Ty>, bool.TermAlg<Term> {}
