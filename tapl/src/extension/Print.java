package extension;

import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>,
		arith.Print<Term, Bind>, record.Print<Term, Bind>, floatstring.Print<Term,Bind>, let.Print<Term,Bind> {}