package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import bot.PrintTy;
import bot.Subtype;
import bot.TyEqv;
import bot.Typeof;
import bot.tyalg.external.Ty;
import bot.tyalg.external.TyAlgFactory;
import bot.tyalg.external.TyAlgMatcher;
import bot.tyalg.external.TyAlgMatcherImpl;
import bot.tyalg.external.TyAlgVisitor;
import bot.tyalg.shared.GTyAlg;
import typed.Eval1;
import typed.GetTypeFromBind;
import typed.IsVal;
import typed.Print;
import typed.PrintBind;
import typed.TmMap;
import typed.bindingalg.external.Bind;
import typed.bindingalg.external.BindingAlgFactory;
import typed.bindingalg.external.BindingAlgVisitor;
import typed.bindingalg.shared.GBindingAlg;
import typed.termalg.external.Term;
import typed.termalg.external.TermAlgFactory;
import typed.termalg.external.TermAlgMatcher;
import typed.termalg.external.TermAlgMatcherImpl;
import typed.termalg.external.TermAlgVisitor;
import typed.termalg.shared.GTermAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestBot {
	class PrintAll implements Print<Term<Ty>,Ty,Bind<Ty>>, PrintTy<Ty,Bind<Ty>>, PrintBind<Bind<Ty>, Ty>,
	    TermAlgVisitor<IPrint<Bind<Ty>>,Ty>, TyAlgVisitor<IPrint<Bind<Ty>>>, BindingAlgVisitor<IPrint<Bind<Ty>>, Ty> {

	  public String printBind(Bind<Ty> bind, Context<Bind<Ty>> ctx) {
	    return visitBind(bind).print(ctx);
	  }

		public String printTy(Ty ty, Context<Bind<Ty>> ctx) {
			return visitTy(ty).print(ctx);
		}
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Ty>, Ty>, BindingAlgVisitor<Ty, Ty> {}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class SubtypeImpl implements Subtype<Ty>, TyAlgVisitor<ISubtype<Ty>> {
	  public boolean tyEqv(Ty ty1, Ty ty2) {
	    return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
	  }
		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<ITypeof<Ty, Bind<Ty>>, Ty> {
	  @Override public boolean subtype(Ty ty1, Ty ty2) {
      return new SubtypeImpl().subtype(ty1,ty2);
    }
	  @Override public Ty getTypeFromBind(Bind<Ty> bind) {
			return new GetTypeFromBindImpl().visitBind(bind);
	  }

		@Override public boolean tyEqv(Ty ty1, Ty ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override public TyAlgMatcher<Ty, Ty> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public GTyAlg<Ty, Ty> tyAlg() {
			return tyFact;
		}
		@Override public GBindingAlg<Bind<Ty>, Ty, Bind<Ty>> bindAlg() {
		  return bindFact;
		}
	}

	class Eval1Impl implements Eval1<Term<Ty>, Ty>, TermAlgVisitor<Term<Ty>, Ty> {
    public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
      return tmFact;
    }
    @Override public TermAlgMatcher<Term<Ty>, Ty, Term<Ty>> matcher() {
      return new TermAlgMatcherImpl<>();
    }
    @Override public Term<Ty> termSubstTop(Term<Ty> s, Term<Ty> t) {
      return new TmMapImpl().termSubstTop(s, t);
    }
    @Override public boolean isVal(Term<Ty> t) {
      return new IsValImpl().visitTerm(t);
    }
	}

	class EvalImpl implements Eval<Term<Ty>> {
	  public boolean isVal(Term<Ty> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	  public Term<Ty> eval1(Term<Ty> t) {
	    return new Eval1Impl().visitTerm(t);
	  }
	}

	class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}

	class TmMapImpl implements TmMap<Term<Ty>, Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>,Term<Ty>>, Ty> {
	  @Override public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	    return tmFact;
	  }
	}

	// printers
	PrintAll print = new PrintAll();
	TypeofImpl type = new TypeofImpl();

	// elements
	Ty ty;
	Bind<Ty> bind;
	Term<Ty> term;

	// factories
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<Ty> bindFact = new BindingAlgFactory<>();
	TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();

	Context<Bind<Ty>> ctx = new Context<Bind<Ty>>(bindFact);

	Ty top = tyFact.TyTop();
	Ty bot = tyFact.TyBot();
	Ty top2top = tyFact.TyArr(top, top);
	Ty arr = tyFact.TyArr(bot, top);
	Term<Ty> x = tmFact.TmVar(0, 1);
	Term<Ty> t1 = tmFact.TmAbs("x", top, x);
	Term<Ty> t2 = tmFact.TmApp(t1, t1);
	Term<Ty> t3 = tmFact.TmApp(tmFact.TmAbs("x", top2top, x), t1);
	Term<Ty> t5 = tmFact.TmAbs("x", bot, x);
	Term<Ty> t6 = tmFact.TmAbs("x", bot, tmFact.TmApp(x, x));

	// typer
	TypeofImpl typeof = new TypeofImpl();
	TyEqvImpl tyEqual = new TyEqvImpl();

	@Test
	public void testPrint() {
	  assertEquals("lambda x:Top. x", print.visitTerm(t1).print(ctx));
	  assertEquals("lambda x:Top. x lambda x:Top. x", print.visitTerm(t2).print(ctx));
	  assertEquals("lambda x:(Top -> Top). x lambda x:Top. x", print.visitTerm(t3).print(ctx));
	  assertEquals("lambda x:Bot. x", print.visitTerm(t5).print(ctx));
	  assertEquals("lambda x:Bot. x x", print.visitTerm(t6).print(ctx));
	}

	@Test
	public void subtypeTest() {
		// S-REFL
		assertTrue(type.subtype(bot, bot));
		assertTrue(type.subtype(top, top));
		assertTrue(type.subtype(arr, arr));

		// S-BOT
		assertTrue(type.subtype(bot, arr));
		assertTrue(type.subtype(bot, top));

		// S-TOP
		assertTrue(type.subtype(bot, top));
		assertTrue(type.subtype(arr, top));

		// S-ARROW
		// T1 <: S1  S2 <: T2
		// ------------------
		//  S1->S2 <: T1->T2
		assertFalse(type.subtype(tyFact.TyArr(top, top), tyFact.TyArr(top, bot)));
		assertTrue(type.subtype(tyFact.TyArr(top, top), tyFact.TyArr(bot, top)));
		assertTrue(type.subtype(tyFact.TyArr(bot, bot), tyFact.TyArr(bot, top)));
		assertTrue(type.subtype(tyFact.TyArr(top, bot), tyFact.TyArr(top, top)));
	}

//	@Test
//	public void testEval() {
//	  assertEquals("lambda x:Top. x", print.visitTerm(t1).print(ctx));
//	  assertEquals("lambda x:Top. x lambda x:Top. x", print.visitTerm(t2).print(ctx));
//	  assertEquals("lambda x:(Top -> Top). x lambda x:Top. x", print.visitTerm(t3).print(ctx));
//	  assertEquals("lambda r:{x:(Top -> Top)}. r.x r.x lambda x:Top. x", print.visitTerm(t4).print(ctx));
//	  assertEquals("lambda x:Bot. x", print.visitTerm(t5).print(ctx));
//	  assertEquals("lambda x:Bot. x x", print.visitTerm(t6).print(ctx));
//	}
//
	@Test
	public void testTypeof() {
	  assertEquals("lambda x:Top. x", print.visitTerm(t1).print(ctx));
	  assertEquals("lambda x:Top. x lambda x:Top. x", print.visitTerm(t2).print(ctx));
	  assertEquals("lambda x:(Top -> Top). x lambda x:Top. x", print.visitTerm(t3).print(ctx));
	  assertEquals("lambda x:Bot. x", print.visitTerm(t5).print(ctx));
	  assertEquals("lambda x:Bot. x x", print.visitTerm(t6).print(ctx));
	}
}