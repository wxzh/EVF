package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

import library.Tuple2;
import rcdsubbot.CTerm;
import rcdsubbot.CTy;
import rcdsubbot.Eval1;
import rcdsubbot.IsVal;
import rcdsubbot.Print;
import rcdsubbot.PrintTy;
import rcdsubbot.Subtype;
import rcdsubbot.TermAlg;
import rcdsubbot.TermAlgFactory;
import rcdsubbot.TermAlgMatcher;
import rcdsubbot.TermAlgMatcherImpl;
import rcdsubbot.TermAlgVisitor;
import rcdsubbot.TmMap;
import rcdsubbot.TyAlg;
import rcdsubbot.TyAlgFactory;
import rcdsubbot.TyAlgMatcher;
import rcdsubbot.TyAlgMatcherImpl;
import rcdsubbot.TyAlgVisitor;
import rcdsubbot.TyEqv;
import rcdsubbot.Typeof;
import typed.BindingAlg;
import typed.BindingAlgFactory;
import typed.BindingAlgVisitor;
import typed.CBind;
import typed.GetTypeFromBind;
import typed.PrintBind;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestRcdSubBot {
	class PrintAll implements Print<CTerm<CTy>,CTy,CBind<CTy>>, PrintTy<CTy,CBind<CTy>>, PrintBind<CBind<CTy>, CTy>,
	    TermAlgVisitor<IPrint<CBind<CTy>>,CTy>, TyAlgVisitor<IPrint<CBind<CTy>>>, BindingAlgVisitor<IPrint<CBind<CTy>>, CTy> {

	  public String printBind(CBind<CTy> bind, Context<CBind<CTy>> ctx) {
	    return visitBind(bind).print(ctx);
	  }

		public String printTy(CTy ty, Context<CBind<CTy>> ctx) {
			return visitTy(ty).print(ctx);
		}
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTy>, CTy>, BindingAlgVisitor<CTy, CTy> {}

	class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class SubtypeImpl implements Subtype<CTy>, TyAlgVisitor<ISubtype<CTy>> {
	  public boolean tyEqv(CTy ty1, CTy ty2) {
	    return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
	  }
		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TypeofImpl implements Typeof<CTerm<CTy>, CTy, CBind<CTy>>, TermAlgVisitor<ITypeof<CTy, CBind<CTy>>, CTy> {
	  @Override public boolean subtype(CTy ty1, CTy ty2) {
      return new SubtypeImpl().subtype(ty1,ty2);
    }
	  @Override public CTy getTypeFromBind(CBind<CTy> bind) {
			return new GetTypeFromBindImpl().visitBind(bind);
	  }

		@Override public boolean tyEqv(CTy ty1, CTy ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override public TyAlgMatcher<CTy, CTy> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public TyAlg<CTy> tyAlg() {
			return tyFact;
		}
		@Override public BindingAlg<CBind<CTy>, CTy> bindAlg() {
		  return bindFact;
		}
	}

	class Eval1Impl implements Eval1<CTerm<CTy>, CTy>, TermAlgVisitor<CTerm<CTy>, CTy> {
    public TermAlg<CTerm<CTy>, CTy> alg() {
      return tmFact;
    }
    @Override public TermAlgMatcher<CTerm<CTy>, CTy, CTerm<CTy>> matcher() {
      return new TermAlgMatcherImpl<>();
    }
    @Override public CTerm<CTy> termSubstTop(CTerm<CTy> s, CTerm<CTy> t) {
      return new TmMapImpl().termSubstTop(s, t);
    }
    @Override public boolean isVal(CTerm<CTy> t) {
      return new IsValImpl().visitTerm(t);
    }
	}

	class EvalImpl implements Eval<CTerm<CTy>> {
	  public boolean isVal(CTerm<CTy> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	  public CTerm<CTy> eval1(CTerm<CTy> t) {
	    return new Eval1Impl().visitTerm(t);
	  }
	}

	class IsValImpl implements IsVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean, CTy> {}

	class TmMapImpl implements TmMap<CTerm<CTy>, CTy>, TermAlgVisitor<Function<TmMapCtx<CTerm<CTy>>,CTerm<CTy>>, CTy> {
	  @Override public TermAlg<CTerm<CTy>, CTy> alg() {
	    return tmFact;
	  }
	}

	// printers
	PrintAll print = new PrintAll();
	TypeofImpl type = new TypeofImpl();

	// elements
	CTy ty;
	CBind<CTy> bind;
	CTerm<CTy> term;

	// factories
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<CTy> bindFact = new BindingAlgFactory<>();
	TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();

	Context<CBind<CTy>> ctx = new Context<>(bindFact);

	CTy top = tyFact.TyTop();
	CTy bot = tyFact.TyBot();
	CTy top2top = tyFact.TyArr(top, top);
	CTy arr = tyFact.TyArr(bot, top);
	CTy rec = tyFact.TyRecord(Arrays.asList(new Tuple2<>("x", top2top)));
	CTerm<CTy> x = tmFact.TmVar(0, 1);
	CTerm<CTy> r_x = tmFact.TmProj(x, "x");
	CTerm<CTy> t1 = tmFact.TmAbs("x", top, x);
	CTerm<CTy> t2 = tmFact.TmApp(t1, t1);
	CTerm<CTy> t3 = tmFact.TmApp(tmFact.TmAbs("x", top2top, x), t1);
//	Term<CTy>
	CTerm<CTy> t4 = tmFact.TmApp(tmFact.TmAbs("r", rec, tmFact.TmApp(r_x, r_x)), t1);
	CTerm<CTy> t5 = tmFact.TmAbs("x", bot, x);
	CTerm<CTy> t6 = tmFact.TmAbs("x", bot, tmFact.TmApp(x, x));

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