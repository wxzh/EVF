package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import simplebool.CTerm;
import simplebool.CTy;
import simplebool.Eval1;
import simplebool.IsVal;
import simplebool.Print;
import simplebool.PrintTy;
import simplebool.TermAlg;
import simplebool.TermAlgFactory;
import simplebool.TermAlgMatcher;
import simplebool.TermAlgMatcherImpl;
import simplebool.TermAlgVisitor;
import simplebool.TmMap;
import simplebool.TyAlg;
import simplebool.TyAlgFactory;
import simplebool.TyAlgMatcher;
import simplebool.TyAlgMatcherImpl;
import simplebool.TyAlgVisitor;
import simplebool.TyEqv;
import simplebool.Typeof;
import typed.BindingAlg;
import typed.BindingAlgFactory;
import typed.BindingAlgVisitor;
import typed.CBind;
import typed.GetTypeFromBind;
import typed.PrintBind;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;
import utils.TypeError;

public class TestSimpleBool {
	static class IsValImpl implements IsVal<CTerm<CTy>,CTy>, TermAlgVisitor<Boolean,CTy> {}

	static class PrintAll implements Print<CTerm<CTy>,CTy,CBind<CTy>>, PrintTy<CTy, CBind<CTy>>, PrintBind<CBind<CTy>, CTy>,
	    TermAlgVisitor<IPrint<CBind<CTy>>,CTy>, TyAlgVisitor<IPrint<CBind<CTy>>>, BindingAlgVisitor<IPrint<CBind<CTy>>, CTy>{
		public TermAlgMatcher<CTerm<CTy>, CTy, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public String printBind(CBind<CTy> bind, Context<CBind<CTy>> ctx) {
		  return visitBind(bind).print(ctx);
		}
		public String printTy(CTy ty, utils.Context<CBind<CTy>> ctx) {
	    return visitTy(ty).print(ctx);
		}
	}

	static class Eval1Impl implements Eval1<CTerm<CTy>,CTy>, TermAlgVisitor<CTerm<CTy>,CTy> {
		public TermAlg<CTerm<CTy>,CTy> alg() {
			return tmFact;
		}

		public TermAlgMatcher<CTerm<CTy>,CTy,CTerm<CTy>> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public CTerm<CTy> termSubstTop(CTerm<CTy> s, CTerm<CTy> t) {
		  return new TmMapImpl().termSubstTop(s, t);
		}
		public boolean isVal(CTerm<CTy> t) {
		  return isVal.visitTerm(t);
		}
	}

  static class TmMapImpl implements TmMap<CTerm<CTy>,CTy>, TermAlgVisitor<Function<TmMapCtx<CTerm<CTy>>, CTerm<CTy>>, CTy> {
    public TermAlg<CTerm<CTy>, CTy> alg() {
      return tmFact;
    }
  }

	static class EvalImpl implements Eval<CTerm<CTy>> {
		public CTerm<CTy> eval1(CTerm<CTy> e) {
			return e.accept(eval1);
		}

		public boolean isVal(CTerm<CTy> e) {
			return e.accept(isVal);
		}
	}

	static class TypeofImpl implements Typeof<CTerm<CTy>, CTy, CBind<CTy>>, TermAlgVisitor<ITypeof<CTy,CBind<CTy>>,CTy> {
	  public boolean tyEqv(CTy ty1, CTy ty2) {
	    return tyEqv.visitTy(ty1).tyEqv(ty2);
	  }
	  public BindingAlg<CBind<CTy>, CTy> bindAlg() {
	    return bindFact;
	  }
	  public CTy getTypeFromBind(CBind<CTy> bind) {
	    return new GetTypeFromBindImpl().visitBind(bind);
	  }
	  public TyAlg<CTy> tyAlg() {
	    return tyFact;
	  }
	  public TyAlgMatcher<CTy, CTy> tyMatcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}

	static class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTy>, CTy>, BindingAlgVisitor<CTy,CTy> {}

	static class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
	  public TyAlgMatcher<CTy, Boolean> matcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}

	static TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();
	static TyAlgFactory tyFact = new TyAlgFactory();

	static PrintAll print = new PrintAll();
	static IsValImpl isVal = new IsValImpl();
	static Eval1Impl eval1 = new Eval1Impl();
	static EvalImpl eval = new EvalImpl();
	static BindingAlgFactory<CTy> bindFact = new BindingAlgFactory<>();
	static Context<CBind<CTy>> ctx = new Context<>(bindFact);
	static TyEqvImpl tyEqv = new TyEqvImpl();
	static TypeofImpl type = new TypeofImpl();

	static CTy Bool = tyFact.TyBool();
	static CTy Bool2Bool = tyFact.TyArr(Bool, Bool);
	static CTy Bool2Bool2Bool = tyFact.TyArr(Bool2Bool, Bool);
	static CTerm<CTy> x = tmFact.TmVar(0, 1);
	static CTerm<CTy> t = tmFact.TmTrue();
	static CTerm<CTy> f = tmFact.TmFalse();
	static CTerm<CTy> t1 = tmFact.TmAbs("x", Bool, x);
	static CTerm<CTy> fn = tmFact.TmAbs("x", tyFact.TyArr(Bool, Bool), tmFact.TmIf(tmFact.TmApp(x, f), t, f));
	static CTerm<CTy> t2 = tmFact.TmApp(fn, tmFact.TmAbs("x", Bool, tmFact.TmIf(x, f, t)));

	@Test
	public void printTest() {
		assertEquals("lambda x:Bool. x", print.visitTerm(t1).print(ctx));
		assertEquals("lambda x:(Bool -> Bool). if x false then true else false lambda x:Bool. if x then false else true", print.visitTerm(t2).print(ctx));
	}

	@Test
	public void eval1Test(){
		assertEquals("if lambda x:Bool. if x then false else true false then true else false", print.visitTerm(eval1.visitTerm(t2)).print(ctx));
		assertEquals("if if false then false else true then true else false", print.visitTerm(eval1.visitTerm(eval1.visitTerm(t2))).print(ctx));
		assertEquals("if true then true else false", print.visitTerm(eval1.visitTerm(eval1.visitTerm(eval1.visitTerm(t2)))).print(ctx));
		assertEquals("true", print.visitTerm(eval.eval(t2)).print(ctx));
	}

	@Test
	public void typeofTest(){
	  assertEquals("(Bool -> Bool)", print.printTy(type.visitTerm(t1).typeof(ctx), ctx));
	  assertEquals("((Bool -> Bool) -> Bool)", print.printTy(type.visitTerm(fn).typeof(ctx), ctx));
	  assertEquals("Bool", print.printTy(type.visitTerm(t2).typeof(ctx), ctx));
	}

	@Test(expected=TypeError.class)
	public void illtypedTest(){
	  type.visitTerm(tmFact.TmApp(t, f)).typeof(ctx);
	}

	@Test
	public void tyEqvTest(){
	  assertTrue(type.tyEqv(Bool2Bool, type.visitTerm(t1).typeof(ctx)));
	  assertTrue(type.tyEqv(Bool2Bool2Bool, type.visitTerm(fn).typeof(ctx)));
	  assertTrue(type.tyEqv(Bool, type.visitTerm(t2).typeof(ctx)));
	  assertFalse(type.tyEqv(Bool2Bool, Bool));
	}
}