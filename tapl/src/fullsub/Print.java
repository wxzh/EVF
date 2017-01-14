package fullsub;

import fullsub.termalg.external.TermAlgMatcher;
import fullsub.termalg.shared.GTermAlg;
import utils.IPrint;

public interface Print<Term, Ty, Bind> extends GTermAlg<Term, Ty, IPrint<Bind>>, typed.Print<Term, Ty, Bind>, moreextension.Print<Term, Ty, Bind> {
	@Override TermAlgMatcher<Term, Ty, String> matcher();
	@Override PrintTy<Ty, Bind> printTy();
}
