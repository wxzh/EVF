package fullsimple;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends typed.TermAlg<Term, Ty>, variant.TermAlg<Term, Ty>, moreextension.TermAlg<Term,Ty> {}