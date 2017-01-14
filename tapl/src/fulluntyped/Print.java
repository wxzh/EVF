package fulluntyped;

import fulluntyped.termalg.external.TermAlgMatcher;
import fulluntyped.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>, untyped.Print<Term, Bind>, extension.Print<Term, Bind> {
	@Override PrintBind<Bind, Term> printBind();
	@Override TermAlgMatcher<Term, String> matcher();
}