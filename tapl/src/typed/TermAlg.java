package typed;

import annotation.Visitor;

@Visitor public interface TermAlg<Term, Ty> extends varapp.TermAlg<Term> {
	Term TmAbs(String x, Ty ty, Term t);
}