package fullsimple;

import fullsimple.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>,
		variant.Print<Term, Ty, Bind>, moreextension.Print<Term, Ty, Bind>, simplebool.Print<Term, Ty, Bind> {
}