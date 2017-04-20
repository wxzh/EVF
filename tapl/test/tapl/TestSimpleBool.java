package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import simplebool.Eval1;
import simplebool.IsVal;
import simplebool.Print;
import simplebool.PrintTy;
import simplebool.TmMap;
import simplebool.TyEqv;
import simplebool.Typeof;
import simplebool.termalg.external.Term;
import simplebool.termalg.external.TermAlgFactory;
import simplebool.termalg.external.TermAlgMatcher;
import simplebool.termalg.external.TermAlgMatcherImpl;
import simplebool.termalg.external.TermAlgVisitor;
import simplebool.termalg.shared.GTermAlg;
import simplebool.tyalg.external.Ty;
import simplebool.tyalg.external.TyAlgFactory;
import simplebool.tyalg.external.TyAlgMatcher;
import simplebool.tyalg.external.TyAlgMatcherImpl;
import simplebool.tyalg.external.TyAlgVisitor;
import simplebool.tyalg.shared.GTyAlg;
import typed.GetTypeFromBind;
import typed.PrintBind;
import typed.bindingalg.external.Bind;
import typed.bindingalg.external.BindingAlgFactory;
import typed.bindingalg.external.BindingAlgVisitor;
import typed.bindingalg.shared.GBindingAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;
import utils.TypeError;

public class TestSimpleBool {
	static class IsValImpl implements IsVal<Term<Ty>,Ty>, TermAlgVisitor<Boolean,Ty> {}

	static class PrintAll implements Print<Term<Ty>,Ty,Bind<Ty>>, PrintTy<Ty, Bind<Ty>>, PrintBind<Bind<Ty>, Ty>,
	    TermAlgVisitor<IPrint<Bind<Ty>>,Ty>, TyAlgVisitor<IPrint<Bind<Ty>>>, BindingAlgVisitor<IPrint<Bind<Ty>>, Ty>{
		public TermAlgMatcher<Term<Ty>, Ty, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public String printBind(Bind<Ty> bind, Context<Bind<Ty>> ctx) {
		  return visitBind(bind).print(ctx);
		}
		public String printTy(Ty ty, utils.Context<Bind<Ty>> ctx) {
	    return visitTy(ty).print(ctx);
		}
	}

	static class Eval1Impl implements Eval1<Term<Ty>,Ty>, TermAlgVisitor<Term<Ty>,Ty> {
		public GTermAlg<Term<Ty>,Ty,Term<Ty>> alg() {
			return tmFact;
		}

		public TermAlgMatcher<Term<Ty>,Ty,Term<Ty>> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public Term<Ty> termSubstTop(Term<Ty> s, Term<Ty> t) {
		  return new TmMapImpl().termSubstTop(s, t);
		}
		public boolean isVal(Term<Ty> t) {
		  return isVal.visitTerm(t);
		}
	}

  static class TmMapImpl implements TmMap<Term<Ty>,Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>, Term<Ty>>, Ty> {
    public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
      return tmFact;
    }
  }

	static class EvalImpl implements Eval<Term<Ty>> {
		public Term<Ty> eval1(Term<Ty> e) {
			return e.accept(eval1);
		}

		public boolean isVal(Term<Ty> e) {
			return e.accept(isVal);
		}
	}

	static class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<ITypeof<Ty,Bind<Ty>>,Ty> {
	  public boolean tyEqv(Ty ty1, Ty ty2) {
	    return tyEqv.visitTy(ty1).tyEqv(ty2);
	  }
	  public GBindingAlg<Bind<Ty>, Ty, Bind<Ty>> bindAlg() {
	    return bindFact;
	  }
	  public Ty getTypeFromBind(Bind<Ty> bind) {
	    return new GetTypeFromBindImpl().visitBind(bind);
	  }
	  public GTyAlg<Ty, Ty> tyAlg() {
	    return tyFact;
	  }
	  public TyAlgMatcher<Ty, Ty> tyMatcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}

	static class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Ty>, Ty>, BindingAlgVisitor<Ty,Ty> {}

	static class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
	  public TyAlgMatcher<Ty, Boolean> matcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}

	static TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();
	static TyAlgFactory tyFact = new TyAlgFactory();

	static PrintAll print = new PrintAll();
	static IsValImpl isVal = new IsValImpl();
	static Eval1Impl eval1 = new Eval1Impl();
	static EvalImpl eval = new EvalImpl();
	static BindingAlgFactory<Ty> bindFact = new BindingAlgFactory<>();
	static Context<Bind<Ty>> ctx = new Context<>(bindFact);
	static TyEqvImpl tyEqv = new TyEqvImpl();
	static TypeofImpl type = new TypeofImpl();

	static Ty Bool = tyFact.TyBool();
	static Ty Bool2Bool = tyFact.TyArr(Bool, Bool);
	static Ty Bool2Bool2Bool = tyFact.TyArr(Bool2Bool, Bool);
	static Term<Ty> x = tmFact.TmVar(0, 1);
	static Term<Ty> t = tmFact.TmTrue();
	static Term<Ty> f = tmFact.TmFalse();
	static Term<Ty> t1 = tmFact.TmAbs("x", Bool, x);
	static Term<Ty> fn = tmFact.TmAbs("x", tyFact.TyArr(Bool, Bool), tmFact.TmIf(tmFact.TmApp(x, f), t, f));
	static Term<Ty> t2 = tmFact.TmApp(fn, tmFact.TmAbs("x", Bool, tmFact.TmIf(x, f, t)));

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