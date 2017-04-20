package fulluntyped;

import fulluntyped.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Bind> extends GTermAlg<Term, IPrint<Bind>>, untyped.Print<Term, Bind>, extension.Print<Term, Bind> {}