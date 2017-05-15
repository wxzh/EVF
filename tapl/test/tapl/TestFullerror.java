package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import fullerror.CTerm;
import fullerror.CTy;
import fullerror.Eval1;
import fullerror.IsVal;
import fullerror.JoinMeet;
import fullerror.Print;
import fullerror.PrintTy;
import fullerror.Subtype;
import fullerror.TermAlg;
import fullerror.TermAlgFactory;
import fullerror.TermAlgMatcher;
import fullerror.TermAlgMatcherImpl;
import fullerror.TermAlgVisitor;
import fullerror.TmMap;
import fullerror.TyAlg;
import fullerror.TyAlgFactory;
import fullerror.TyAlgMatcher;
import fullerror.TyAlgMatcherImpl;
import fullerror.TyAlgVisitor;
import fullerror.TyEqv;
import fullerror.Typeof;
import fullsimple.BindingAlg;
import fullsimple.BindingAlgFactory;
import fullsimple.BindingAlgVisitor;
import fullsimple.CBind;
import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import utils.Context;
import utils.Eval;
import utils.IJoin;
import utils.IMeet;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestFullerror {
	class PrintAll implements Print<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>, CTy>,
	    PrintTy<CTy, CBind<CTerm<CTy>,CTy>>, TyAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>>,
      PrintBind<CBind<CTerm<CTy>,CTy>,CTerm<CTy>,CTy>, BindingAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>,CTerm<CTy>,CTy> {

	  public String printTy(CTy ty, Context<CBind<CTerm<CTy>,CTy>> ctx) {
	    return visitTy(ty).print(ctx);
	  }
	  public String printBind(CBind<CTerm<CTy>,CTy> bind, Context<CBind<CTerm<CTy>,CTy>> ctx) {
	    return visitBind(bind).print(ctx);
	  }
	  public String printTerm(CTerm<CTy> t, Context<CBind<CTerm<CTy>, CTy>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	}

	class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class SubtypeImpl implements Subtype<CTy>, TyAlgVisitor<ISubtype<CTy>> {
		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public boolean tyEqv(CTy ty1, CTy ty2) {
		  return tyEqv.visitTy(ty1).tyEqv(ty2);
		}
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTerm<CTy>,CTy>,CTerm<CTy>,CTy>, BindingAlgVisitor<CTy,CTerm<CTy>,CTy> {}

	class JoinMeetImpl implements JoinMeet<CTy> {
	  @Override public boolean subtype(CTy ty1, CTy ty2) {
	    return subtype.visitTy(ty1).subtype(ty2);
	  }

		@Override public TyAlgMatcher<CTy, CTy> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public TyAlg<CTy> alg() {
			return tyFact;
		}

		class JoinImpl extends JoinMeetImpl implements Join<CTy>, TyAlgVisitor<IJoin<CTy>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<CTy>, TyAlgVisitor<IMeet<CTy>> {}

		public CTy meetImpl(CTy ty1, CTy ty2) {
			return new MeetImpl().visitTy(ty1).meet(ty2);
		}

		public CTy joinImpl(CTy ty1, CTy ty2) {
			return new JoinImpl().visitTy(ty1).join(ty2);
		}
	}

	class TypeofImpl implements Typeof<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<ITypeof<CTy, CBind<CTerm<CTy>,CTy>>, CTy> {
	  public CTy join(CTy ty1, CTy ty2) {
	    return new JoinMeetImpl().join(ty1, ty2);
	  }
	  public boolean subtype(CTy ty1, CTy ty2) {
	    return subtype.visitTy(ty1).subtype(ty2);
	  }
    public CTy getTypeFromBind(CBind<CTerm<CTy>,CTy> bind) {
      return new GetTypeFromBindImpl().visitBind(bind);
    }
		public BindingAlg<CBind<CTerm<CTy>,CTy>,CTerm<CTy>,CTy> bindAlg() {
			return bindFact;
		}
		public TyAlg<CTy> tyAlg() {
			return tyFact;
		}
		public boolean tyEqv(CTy ty1, CTy ty2) {
			return tyEqv.visitTy(ty1).tyEqv(ty2);
		}
		public TyAlgMatcher<CTy, CTy> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	 class Eval1Impl implements Eval1<CTerm<CTy>, CTy>, TermAlgVisitor<CTerm<CTy>, CTy> {
	    public TermAlgMatcher<CTerm<CTy>, CTy, CTerm<CTy>> matcher() {
	      return new TermAlgMatcherImpl<>();
	    }
	    public TermAlg<CTerm<CTy>, CTy> alg() {
	      return tmFact;
	    }
	    public CTerm<CTy> termSubstTop(CTerm<CTy> s, CTerm<CTy> t) {
	      return new TmMapImpl().termSubstTop(s, t);
	    }
	    public boolean isVal(CTerm<CTy> t) {
	      return new IsValImpl().visitTerm(t);
	    }
	  }
	  class EvalImpl implements Eval<CTerm<CTy>> {
	    public CTerm<CTy> eval1(CTerm<CTy> t) {
	      return new Eval1Impl().visitTerm(t);
	    }
	    public boolean isVal(CTerm<CTy> t) {
	      return new IsValImpl().visitTerm(t);
	    }
	  }
	  class IsValImpl implements IsVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean, CTy> {}
	  class TmMapImpl implements TmMap<CTerm<CTy>, CTy>, TermAlgVisitor<Function<TmMapCtx<CTerm<CTy>>,CTerm<CTy>>, CTy> {
	    public TermAlg<CTerm<CTy>, CTy> alg() {
	      return tmFact;
	    }
	  }

	PrintAll print = new PrintAll();

	// elements
	CTy ty;
	CBind<CTerm<CTy>,CTy> bind;
	CTerm<CTy> term;

	// factories
	TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<CTerm<CTy>,CTy> bindFact = new BindingAlgFactory<>();
	TermAlgFactory<CTy> termFact = new TermAlgFactory<>();

	Context<CBind<CTerm<CTy>,CTy>> ctx = new Context<CBind<CTerm<CTy>,CTy>>(bindFact);

	CTy bool = tyFact.TyBool();
	CTy top = tyFact.TyTop();
	CTy bot = tyFact.TyBot();

	CTerm<CTy> t = termFact.TmTrue();
	CTerm<CTy> f = termFact.TmFalse();
	CTerm<CTy> error = termFact.TmError();
	CTerm<CTy> if_error_true_false = termFact.TmIf(error, t, f);
	CTerm<CTy> tryErrorWithTrue = termFact.TmTry(error, t);

	// typer
	TypeofImpl typeof = new TypeofImpl();
	TyEqvImpl tyEqv = new TyEqvImpl();
	SubtypeImpl subtype = new SubtypeImpl();

	@Test
	public void printTest() {
		assertEquals("if error then true else false", print.printTerm(if_error_true_false, ctx));
		assertEquals("try error with true", print.printTerm(tryErrorWithTrue, ctx));
	}

	@Test
	public void typeofTest() {
		assertTrue(bot.accept(tyEqv).tyEqv(error.accept(typeof).typeof(ctx)));
	}
}