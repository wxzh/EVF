package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

import library.Tuple2;
import rcdsubbot.Eval1;
import rcdsubbot.IsVal;
import rcdsubbot.Print;
import rcdsubbot.PrintTy;
import rcdsubbot.Subtype;
import rcdsubbot.TmMap;
import rcdsubbot.TyEqv;
import rcdsubbot.Typeof;
import rcdsubbot.termalg.external.Term;
import rcdsubbot.termalg.external.TermAlgFactory;
import rcdsubbot.termalg.external.TermAlgMatcher;
import rcdsubbot.termalg.external.TermAlgMatcherImpl;
import rcdsubbot.termalg.external.TermAlgVisitor;
import rcdsubbot.termalg.shared.GTermAlg;
import rcdsubbot.tyalg.external.Ty;
import rcdsubbot.tyalg.external.TyAlgFactory;
import rcdsubbot.tyalg.external.TyAlgMatcher;
import rcdsubbot.tyalg.external.TyAlgMatcherImpl;
import rcdsubbot.tyalg.external.TyAlgVisitor;
import rcdsubbot.tyalg.shared.GTyAlg;
import typed.GetTypeFromBind;
import typed.PrintBind;
import typed.bindingalg.external.Bind;
import typed.bindingalg.external.BindingAlgFactory;
import typed.bindingalg.external.BindingAlgVisitor;
import typed.bindingalg.shared.GBindingAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestRcdSubBot {
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
	Ty rec = tyFact.TyRecord(Arrays.asList(new Tuple2<>("x", top2top)));
	Term<Ty> x = tmFact.TmVar(0, 1);
	Term<Ty> r_x = tmFact.TmProj(x, "x");
	Term<Ty> t1 = tmFact.TmAbs("x", top, x);
	Term<Ty> t2 = tmFact.TmApp(t1, t1);
	Term<Ty> t3 = tmFact.TmApp(tmFact.TmAbs("x", top2top, x), t1);
//	Term<Ty>
	Term<Ty> t4 = tmFact.TmApp(tmFact.TmAbs("r", rec, tmFact.TmApp(r_x, r_x)), t1);
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
	  assertEquals("lambda r:{x:(Top -> Top)}. r.x r.x lambda x:Top. x", print.visitTerm(t4).print(ctx));
	  assertEquals("lambda x:Bot. x", print.visitTerm(t5).print(ctx));
	  assertEquals("lambda x:Bot. x x", print.visitTerm(t6).print(ctx));
	}

	@Test
	public void subtypeTest() {
		// S-REFL
		assertTrue(type.subtype(rec, rec));

		// S-BOT
		assertTrue(type.subtype(bot, rec));

		// S-TOP
		assertTrue(type.subtype(rec, top));

		// S-ARROW
		// T1 <: S1  S2 <: T2
		// ------------------
		//  S1->S2 <: T1->T2
		assertFalse(type.subtype(tyFact.TyArr(top, top), tyFact.TyArr(top, rec)));
		assertTrue(type.subtype(tyFact.TyArr(top, top), tyFact.TyArr(rec, top)));
		assertFalse(type.subtype(tyFact.TyArr(bot, bot), tyFact.TyArr(rec, bot)));
		assertTrue(type.subtype(tyFact.TyArr(bot, bot), tyFact.TyArr(bot, rec)));
		assertTrue(type.subtype(tyFact.TyArr(top, bot), tyFact.TyArr(rec, rec)));
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
//	@Test
//	public void testTypeof() {
//	  assertEquals("lambda x:Top. x", print.visitTerm(t1).print(ctx));
//	  assertEquals("lambda x:Top. x lambda x:Top. x", print.visitTerm(t2).print(ctx));
//	  assertEquals("lambda x:(Top -> Top). x lambda x:Top. x", print.visitTerm(t3).print(ctx));
//	  assertEquals("lambda r:{x:(Top -> Top)}. r.x r.x lambda x:Top. x", print.visitTerm(t4).print(ctx));
//	  assertEquals("lambda x:Bot. x", print.visitTerm(t5).print(ctx));
//	  assertEquals("lambda x:Bot. x x", print.visitTerm(t6).print(ctx));
//	}
}