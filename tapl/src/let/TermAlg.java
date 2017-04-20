package let;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> {
	Term TmLet(String x, Term t1, Term t2);
}
