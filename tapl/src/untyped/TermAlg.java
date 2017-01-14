package untyped;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> extends varapp.TermAlg<Term> {
	Term TmAbs(String x, Term t);
}