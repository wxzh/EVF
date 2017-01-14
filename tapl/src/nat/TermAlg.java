package nat;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> {
	Term TmZero();
	Term TmSucc(Term t);
	Term TmPred(Term t);
}
