package varapp;

import annotation.Visitor;

@Visitor public interface TermAlg<Term> {
	Term TmVar(int x, int n); // x: de Bruijn index(> 0, the xth binder starting from the innermost); n: # of vars in the context
	Term TmApp(Term t1, Term t2);
}
