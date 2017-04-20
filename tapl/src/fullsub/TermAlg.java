package fullsub;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends typed.TermAlg<Term, Ty>, moreextension.TermAlg<Term, Ty> {}