package floatstring;

import floatstring.termalg.shared.GTermAlg;
import floatstring.tyalg.external.TyAlgMatcher;
import floatstring.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, utils.Typeof<Ty> {
	TyAlgMatcher<Ty, Ty> tyMatcher();
	GTyAlg<Ty, Ty> tyAlg();

	@Override default ITypeof<Ty, Bind> TmTimesFloat(Term t1, Term t2) {
		return ctx -> {
			Ty tyT1 = visitTerm(t1).typeof(ctx);
			Ty tyT2 = visitTerm(t2).typeof(ctx);
			Ty tyFloat = tyAlg().TyFloat();
			return tyEqv(tyT1,tyFloat) && tyEqv(tyT2,tyFloat) ? tyFloat : typeError("argument of timesfloat is not a number");
		};
	}


	@Override default ITypeof<Ty, Bind> TmFloat(float p1) {
		return ctx -> tyAlg().TyFloat();
	}

	@Override default ITypeof<Ty, Bind> TmString(String p1) {
		return ctx -> tyAlg().TyString();
	}
}