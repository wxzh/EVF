package fullsub;

import fullsub.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, typed.Print<Term, Ty, Bind>, moreextension.Print<Term, Ty, Bind> {}
