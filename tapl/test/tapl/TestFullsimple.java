package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import fullsimple.BindingAlg;
import fullsimple.Eval1;
import fullsimple.GetTypeFromBind;
import fullsimple.IsNumericVal;
import fullsimple.IsVal;
import fullsimple.Print;
import fullsimple.PrintBind;
import fullsimple.PrintTy;
import fullsimple.TermAlg;
import fullsimple.TmMap;
import fullsimple.TyAlg;
import fullsimple.TyEqv;
import fullsimple.Typeof;
import fullsimple.bindingalg.external.Bind;
import fullsimple.bindingalg.external.BindingAlgFactory;
import fullsimple.bindingalg.external.BindingAlgVisitor;
import fullsimple.termalg.external.Term;
import fullsimple.termalg.external.TermAlgFactory;
import fullsimple.termalg.external.TermAlgMatcher;
import fullsimple.termalg.external.TermAlgMatcherImpl;
import fullsimple.termalg.external.TermAlgVisitor;
import fullsimple.tyalg.external.Ty;
import fullsimple.tyalg.external.TyAlgFactory;
import fullsimple.tyalg.external.TyAlgMatcher;
import fullsimple.tyalg.external.TyAlgMatcherImpl;
import fullsimple.tyalg.external.TyAlgVisitor;
import library.Tuple2;
import library.Tuple3;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestFullsimple {
  class PrintAll implements Print<Term<Ty>, Ty, Bind<Term<Ty>, Ty>>, TermAlgVisitor<IPrint<Bind<Term<Ty>, Ty>>, Ty>,
      PrintTy<Ty, Bind<Term<Ty>, Ty>>, TyAlgVisitor<IPrint<Bind<Term<Ty>, Ty>>>,
      PrintBind<Bind<Term<Ty>, Ty>, Term<Ty>, Ty>, BindingAlgVisitor<IPrint<Bind<Term<Ty>, Ty>>, Term<Ty>, Ty> {
    public String printBind(Bind<Term<Ty>, Ty> bind, Context<Bind<Term<Ty>, Ty>> ctx) {
      return visitBind(bind).print(ctx);
    }
		public String printTy(Ty ty, Context<Bind<Term<Ty>, Ty>> ctx) {
			return visitTy(ty).print(ctx);
		}
		public TermAlgMatcher<Term<Ty>, Ty, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	  public String printTerm(Term<Ty> t, Context<Bind<Term<Ty>, Ty>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Term<Ty>, Ty>, Term<Ty>, Ty>, BindingAlgVisitor<Ty, Term<Ty>, Ty> {}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override
		public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Term<Ty>, Ty>>,
			TermAlgVisitor<ITypeof<Ty, Bind<Term<Ty>, Ty>>, Ty> {
	  @Override
	  public Ty getTypeFromBind(Bind<Term<Ty>, Ty> bind) {
			return new GetTypeFromBindImpl().visitBind(bind);
	  }

		@Override
		public boolean tyEqv(Ty ty1, Ty ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override
		public TyAlgMatcher<Ty, Ty> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override
		public TyAlg<Ty> tyAlg() {
			return tyFact;
		}

		@Override
		public BindingAlg<Bind<Term<Ty>, Ty>, Term<Ty>, Ty> bindAlg() {
			return bindFact;
		}
	}

	class Eval1Impl implements Eval1<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<Term<Ty>, Ty> {
	  public boolean isNumericVal(Term<Ty> t) {
	    return new IsNumericValImpl().visitTerm(t);
	  }
	  public TermAlgMatcher<Term<Ty>, Ty, Term<Ty>> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	  public TermAlg<Term<Ty>, Ty> alg() {
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
	class IsNumericValImpl implements IsNumericVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	class TmMapImpl implements TmMap<Term<Ty>, Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>,Term<Ty>>, Ty> {
	  public TermAlg<Term<Ty>, Ty> alg() {
	    return tmFact;
	  }
	}

	// printers
	PrintAll print = new PrintAll();

	// elements
	Ty ty;
	Bind<Term<Ty>, Ty> bind;
	Term<Ty> term;

	// factories
	TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<Term<Ty>, Ty> bindFact = new BindingAlgFactory<>();

	Context<Bind<Term<Ty>, Ty>> ctx = new Context<Bind<Term<Ty>, Ty>>(bindFact);

	Ty bool = tyFact.TyBool();
	Term<Ty> t = tmFact.TmTrue();
	Term<Ty> f = tmFact.TmFalse();
	Term<Ty> x = tmFact.TmVar(0, 1);
	Term<Ty> eo = tmFact.TmVar(1, 2);
	Term<Ty> iszerox = tmFact.TmIsZero(tmFact.TmVar(0, 2));
	Term<Ty> predx = tmFact.TmPred(tmFact.TmVar(0, 2));
	Ty nat = tyFact.TyNat();
	Ty nat2bool = tyFact.TyArr(nat, tyFact.TyBool());
	Ty eoTy = tyFact.TyRecord(Arrays.asList(new Tuple2<>("even", nat2bool), new Tuple2<>("odd", nat2bool)));
	Term<Ty> even = tmFact.TmAbs("x", nat, tmFact.TmIf(iszerox, t, tmFact.TmApp(tmFact.TmProj(eo, "odd"), predx)));
	Term<Ty> odd = tmFact.TmAbs("x", nat, tmFact.TmIf(iszerox, f, tmFact.TmApp(tmFact.TmProj(eo, "even"), predx)));
	Term<Ty> evenodd = tmFact.TmFix(tmFact.TmAbs("eo", eoTy, tmFact.TmRecord(Arrays.asList(new Tuple2<>("even", even), new Tuple2<>("odd", odd)))));

	// typer
	TypeofImpl typeof = new TypeofImpl();
	TyEqvImpl tyEqual = new TyEqvImpl();

	@Test
	public void testEvenOdd() {
	  assertEquals("fix lambda eo:{even:(Nat -> Bool),odd:(Nat -> Bool)}. {even=lambda x:Nat. if (iszero x) then true else eo.odd (pred x),odd=lambda x:Nat. if (iszero x) then false else eo.even (pred x)}",
	      print.printTerm(evenodd, ctx));
//	  tmFact.TmApp(, p2)
	}

	@Test
	public void testPrintTyFloat() {
		assertEquals("Float", print.printTy(tyFact.TyFloat(), ctx));
	}

	@Test
	public void testPrintTyUnit() {
		assertEquals("Unit", print.printTy(tyFact.TyUnit(), ctx));
	}

	@Test
	public void testPrintTyRecord() {
		ty = tyFact.TyRecord(Arrays.asList(new Tuple2<>("bool", tyFact.TyBool()), new Tuple2<>("nat", tyFact.TyNat())));
		assertEquals("{bool:Bool,nat:Nat}", print.printTy(ty, ctx));
	}

	@Test
	public void testPrintTyVariant() {
		ty = tyFact.TyVariant(Arrays.asList(new Tuple2<>("bool", tyFact.TyBool()), new Tuple2<>("nat", tyFact.TyNat())));
		assertEquals("<bool:Bool,nat:Nat>", print.printTy(ty, ctx));
	}

	@Test
	public void testPrintTyArr() {
		ty = tyFact.TyArr(tyFact.TyString(), tyFact.TyArr(tyFact.TyNat(), tyFact.TyBool()));
		assertEquals("(String -> (Nat -> Bool))", print.printTy(ty, ctx));
	}

	@Test
	public void testPrintVarBind() {
		assertEquals(": Bool", print.printBind(bindFact.VarBind(bool), ctx));
	}

	@Test
	public void testPrintTyAbbBind() {
		assertEquals("= Bool", print.printBind(bindFact.TyAbbBind(bool), ctx));
	}

	@Test
	public void testPrintTmAbbBind() {
		bind = bindFact.TmAbbBind(t, Optional.of(tyFact.TyBool()));
		assertEquals("= true: Bool", print.printBind(bind, ctx));
		bind = bindFact.TmAbbBind(t, Optional.empty());
		assertEquals("= true", print.printBind(bind, ctx));
	}

	@Test
	public void testPrintTmUnit() {
		assertEquals("Unit", print.printTerm(tmFact.TmUnit(), ctx));
	}

	@Test
	public void testPrintTmInert() {
		assertEquals("inert[Bool]", print.printTerm(tmFact.TmInert(bool), ctx));
	}

	@Test
	public void testPrintTmFix() {
		assertEquals("fix true", print.printTerm(tmFact.TmFix(t), ctx));
	}

	@Test
	public void testPrintTmTag() {
		assertEquals("<x=true> as Bool", print.printTerm(tmFact.TmTag("x", t, bool), ctx));
	}

	@Test
	public void testPrintTmCase() {
		term = tmFact.TmCase(t, Arrays.asList(new Tuple3<>("X", "x", tmFact.TmVar(0, 2)),
				new Tuple3<>("Y", "y", tmFact.TmVar(0, 2))));
		assertEquals("case true of <X=x>==>x| <Y=y_>==>y_", print.printTerm(term, ctx.addName("y")));
	}

	@Test
	public void testPrintLet() {
		term = tmFact.TmLet("x", t, x);
		assertEquals("let x=true in x", print.printTerm(term, ctx));
	}

	@Test
	public void testTypeofTmTrue() {
		ty = t.accept(typeof).typeof(ctx);
		assertTrue(bool.accept(tyEqual).tyEqv(ty));
	}

	@Test
	public void testTypeofTmAscribe() {
		ty = tyFact.TyUnit();
		term = tmFact.TmAscribe(tmFact.TmUnit(), ty);
		assertTrue(ty.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
		assertFalse(bool.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
	}

	@Test
	public void testTypeofTmTag() {
		ty = tyFact.TyUnit();
		term = tmFact.TmAscribe(tmFact.TmUnit(), ty);
		assertTrue(ty.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
		assertFalse(bool.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
	}

	@Test
	public void test() {
    term = tmFact.TmString("hello");
    assertEquals("hello", print.printTerm(term, ctx));
		ty = tyFact.TyUnit();
		term = tmFact.TmAscribe(tmFact.TmUnit(), ty);
		assertTrue(ty.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
		assertFalse(bool.accept(tyEqual).tyEqv(term.accept(typeof).typeof(ctx)));
	}
}