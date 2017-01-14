package fullsimple;

import fullsimple.termalg.external.TermAlgMatcher;
import fullsimple.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>,
		variant.Print<Term, Ty, Bind>, moreextension.Print<Term, Ty, Bind> {
	@Override TermAlgMatcher<Term, Ty, String> matcher();
	@Override PrintTy<Ty, Bind> printTy();
}