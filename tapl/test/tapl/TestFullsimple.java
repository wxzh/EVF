package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Test;

import fullsimple.BindingAlg;
import fullsimple.BindingAlgFactory;
import fullsimple.BindingAlgVisitor;
import fullsimple.CBind;
import fullsimple.CTerm;
import fullsimple.CTy;
import fullsimple.Eval1;
import fullsimple.GetTypeFromBind;
import fullsimple.IsNumericVal;
import fullsimple.IsVal;
import fullsimple.Print;
import fullsimple.PrintBind;
import fullsimple.PrintTy;
import fullsimple.TermAlg;
import fullsimple.TermAlgFactory;
import fullsimple.TermAlgMatcher;
import fullsimple.TermAlgMatcherImpl;
import fullsimple.TermAlgVisitor;
import fullsimple.TmMap;
import fullsimple.TyAlg;
import fullsimple.TyAlgFactory;
import fullsimple.TyAlgMatcher;
import fullsimple.TyAlgMatcherImpl;
import fullsimple.TyAlgVisitor;
import fullsimple.TyEqv;
import fullsimple.Typeof;
import library.Tuple2;
import library.Tuple3;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestFullsimple {
  class PrintAll implements Print<CTerm<CTy>, CTy, CBind<CTerm<CTy>, CTy>>, TermAlgVisitor<IPrint<CBind<CTerm<CTy>, CTy>>, CTy>,
      PrintTy<CTy, CBind<CTerm<CTy>, CTy>>, TyAlgVisitor<IPrint<CBind<CTerm<CTy>, CTy>>>,
      PrintBind<CBind<CTerm<CTy>, CTy>, CTerm<CTy>, CTy>, BindingAlgVisitor<IPrint<CBind<CTerm<CTy>, CTy>>, CTerm<CTy>, CTy> {
    public String printBind(CBind<CTerm<CTy>, CTy> bind, Context<CBind<CTerm<CTy>, CTy>> ctx) {
      return visitBind(bind).print(ctx);
    }
		public String printTy(CTy ty, Context<CBind<CTerm<CTy>, CTy>> ctx) {
			return visitTy(ty).print(ctx);
		}
		public TermAlgMatcher<CTerm<CTy>, CTy, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	  public String printTerm(CTerm<CTy> t, Context<CBind<CTerm<CTy>, CTy>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTerm<CTy>, CTy>, CTerm<CTy>, CTy>, BindingAlgVisitor<CTy, CTerm<CTy>, CTy> {}

	class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
		@Override
		public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TypeofImpl implements Typeof<CTerm<CTy>, CTy, CBind<CTerm<CTy>, CTy>>,
			TermAlgVisitor<ITypeof<CTy, CBind<CTerm<CTy>, CTy>>, CTy> {
	  @Override
	  public CTy getTypeFromBind(CBind<CTerm<CTy>, CTy> bind) {
			return new GetTypeFromBindImpl().visitBind(bind);
	  }

		@Override
		public boolean tyEqv(CTy ty1, CTy ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override
		public TyAlgMatcher<CTy, CTy> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override
		public TyAlg<CTy> tyAlg() {
			return tyFact;
		}

		@Override
		public BindingAlg<CBind<CTerm<CTy>, CTy>, CTerm<CTy>, CTy> bindAlg() {
			return bindFact;
		}
	}

	class Eval1Impl implements Eval1<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<CTerm<CTy>, CTy> {
	  public boolean isNumericVal(CTerm<CTy> t) {
	    return new IsNumericValImpl().visitTerm(t);
	  }
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
	class IsNumericValImpl implements IsNumericVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean, CTy> {}
	class IsValImpl implements IsVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean, CTy> {}
	class TmMapImpl implements TmMap<CTerm<CTy>, CTy>, TermAlgVisitor<Function<TmMapCtx<CTerm<CTy>>,CTerm<CTy>>, CTy> {
	  public TermAlg<CTerm<CTy>, CTy> alg() {
	    return tmFact;
	  }
	}

	// printers
	PrintAll print = new PrintAll();

	// elements
	CTy ty;
	CBind<CTerm<CTy>, CTy> bind;
	CTerm<CTy> term;

	// factories
	TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<CTerm<CTy>, CTy> bindFact = new BindingAlgFactory<>();

	Context<CBind<CTerm<CTy>, CTy>> ctx = new Context<>(bindFact);

	CTy bool = tyFact.TyBool();
	CTerm<CTy> t = tmFact.TmTrue();
	CTerm<CTy> f = tmFact.TmFalse();
	CTerm<CTy> x = tmFact.TmVar(0, 1);
	CTerm<CTy> eo = tmFact.TmVar(1, 2);
	CTerm<CTy> iszerox = tmFact.TmIsZero(tmFact.TmVar(0, 2));
	CTerm<CTy> predx = tmFact.TmPred(tmFact.TmVar(0, 2));
	CTy nat = tyFact.TyNat();
	CTy nat2bool = tyFact.TyArr(nat, tyFact.TyBool());
	CTy eoTy = tyFact.TyRecord(Arrays.asList(new Tuple2<>("even", nat2bool), new Tuple2<>("odd", nat2bool)));
	CTerm<CTy> even = tmFact.TmAbs("x", nat, tmFact.TmIf(iszerox, t, tmFact.TmApp(tmFact.TmProj(eo, "odd"), predx)));
	CTerm<CTy> odd = tmFact.TmAbs("x", nat, tmFact.TmIf(iszerox, f, tmFact.TmApp(tmFact.TmProj(eo, "even"), predx)));
	CTerm<CTy> evenodd = tmFact.TmFix(tmFact.TmAbs("eo", eoTy, tmFact.TmRecord(Arrays.asList(new Tuple2<>("even", even), new Tuple2<>("odd", odd)))));

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