package fullsimple;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends variant.TermAlg<Term, Ty>, moreextension.TermAlg<Term, Ty> {
}