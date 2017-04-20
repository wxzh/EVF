package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import fullerror.Eval1;
import fullerror.IsVal;
import fullerror.JoinMeet;
import fullerror.Print;
import fullerror.PrintTy;
import fullerror.Subtype;
import fullerror.TmMap;
import fullerror.TyEqv;
import fullerror.Typeof;
import fullerror.termalg.external.Term;
import fullerror.termalg.external.TermAlgFactory;
import fullerror.termalg.external.TermAlgMatcher;
import fullerror.termalg.external.TermAlgMatcherImpl;
import fullerror.termalg.external.TermAlgVisitor;
import fullerror.termalg.shared.GTermAlg;
import fullerror.tyalg.external.Ty;
import fullerror.tyalg.external.TyAlgFactory;
import fullerror.tyalg.external.TyAlgMatcher;
import fullerror.tyalg.external.TyAlgMatcherImpl;
import fullerror.tyalg.external.TyAlgVisitor;
import fullerror.tyalg.shared.GTyAlg;
import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import fullsimple.bindingalg.external.Bind;
import fullsimple.bindingalg.external.BindingAlgFactory;
import fullsimple.bindingalg.external.BindingAlgVisitor;
import fullsimple.bindingalg.shared.GBindingAlg;
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
	class PrintAll implements Print<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>, Ty>,
	    PrintTy<Ty, Bind<Term<Ty>,Ty>>, TyAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>>,
      PrintBind<Bind<Term<Ty>,Ty>,Term<Ty>,Ty>, BindingAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>,Term<Ty>,Ty> {

	  public String printTy(Ty ty, Context<Bind<Term<Ty>,Ty>> ctx) {
	    return visitTy(ty).print(ctx);
	  }
	  public String printBind(Bind<Term<Ty>,Ty> bind, Context<Bind<Term<Ty>,Ty>> ctx) {
	    return visitBind(bind).print(ctx);
	  }
	  public String printTerm(Term<Ty> t, Context<Bind<Term<Ty>, Ty>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class SubtypeImpl implements Subtype<Ty>, TyAlgVisitor<ISubtype<Ty>> {
		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public boolean tyEqv(Ty ty1, Ty ty2) {
		  return tyEqv.visitTy(ty1).tyEqv(ty2);
		}
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Term<Ty>,Ty>,Term<Ty>,Ty>, BindingAlgVisitor<Ty,Term<Ty>,Ty> {}

	class JoinMeetImpl implements JoinMeet<Ty> {
	  @Override public boolean subtype(Ty ty1, Ty ty2) {
	    return subtype.visitTy(ty1).subtype(ty2);
	  }

		@Override public TyAlgMatcher<Ty, Ty> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public GTyAlg<Ty, Ty> alg() {
			return tyFact;
		}

		class JoinImpl extends JoinMeetImpl implements Join<Ty>, TyAlgVisitor<IJoin<Ty>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<Ty>, TyAlgVisitor<IMeet<Ty>> {}

		public Ty meetImpl(Ty ty1, Ty ty2) {
			return new MeetImpl().visitTy(ty1).meet(ty2);
		}

		public Ty joinImpl(Ty ty1, Ty ty2) {
			return new JoinImpl().visitTy(ty1).join(ty2);
		}
	}

	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<ITypeof<Ty, Bind<Term<Ty>,Ty>>, Ty> {
	  public Ty join(Ty ty1, Ty ty2) {
	    return new JoinMeetImpl().join(ty1, ty2);
	  }
	  public boolean subtype(Ty ty1, Ty ty2) {
	    return subtype.visitTy(ty1).subtype(ty2);
	  }
    public Ty getTypeFromBind(Bind<Term<Ty>,Ty> bind) {
      return new GetTypeFromBindImpl().visitBind(bind);
    }
		public GBindingAlg<Bind<Term<Ty>,Ty>,Term<Ty>,Ty,Bind<Term<Ty>,Ty>> bindAlg() {
			return bindFact;
		}
		public GTyAlg<Ty, Ty> tyAlg() {
			return tyFact;
		}
		public boolean tyEqv(Ty ty1, Ty ty2) {
			return tyEqv.visitTy(ty1).tyEqv(ty2);
		}
		public TyAlgMatcher<Ty, Ty> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	 class Eval1Impl implements Eval1<Term<Ty>, Ty>, TermAlgVisitor<Term<Ty>, Ty> {
	    public TermAlgMatcher<Term<Ty>, Ty, Term<Ty>> matcher() {
	      return new TermAlgMatcherImpl<>();
	    }
	    public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	      return tmFact;
	    }
	    public Term<Ty> termSubstTop(Term<Ty> s, Term<Ty> t) {
	      return new TmMapImpl().termSubstTop(s, t);
	    }
	    public boolean isVal(Term<Ty> t) {
	      return new IsValImpl().visitTerm(t);
	    }
	  }
	  class EvalImpl implements Eval<Term<Ty>> {
	    public Term<Ty> eval1(Term<Ty> t) {
	      return new Eval1Impl().visitTerm(t);
	    }
	    public boolean isVal(Term<Ty> t) {
	      return new IsValImpl().visitTerm(t);
	    }
	  }
	  class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	  class TmMapImpl implements TmMap<Term<Ty>, Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>,Term<Ty>>, Ty> {
	    public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	      return tmFact;
	    }
	  }

	PrintAll print = new PrintAll();

	// elements
	Ty ty;
	Bind<Term<Ty>,Ty> bind;
	Term<Ty> term;

	// factories
	TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<Term<Ty>,Ty> bindFact = new BindingAlgFactory<>();
	TermAlgFactory<Ty> termFact = new TermAlgFactory<>();

	Context<Bind<Term<Ty>,Ty>> ctx = new Context<Bind<Term<Ty>,Ty>>(bindFact);

	Ty bool = tyFact.TyBool();
	Ty top = tyFact.TyTop();
	Ty bot = tyFact.TyBot();

	Term<Ty> t = termFact.TmTrue();
	Term<Ty> f = termFact.TmFalse();
	Term<Ty> error = termFact.TmError();
	Term<Ty> if_error_true_false = termFact.TmIf(error, t, f);
	Term<Ty> tryErrorWithTrue = termFact.TmTry(error, t);

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