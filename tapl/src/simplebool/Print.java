package simplebool;

import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, typed.Print<Term, Ty, Bind>, bool.Print<Term, Bind> {}