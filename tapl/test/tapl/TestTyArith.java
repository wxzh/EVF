package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arith.CTerm;
import arith.IsNumericVal;
import arith.IsVal;
import arith.TermAlgFactory;
import arith.TermAlgVisitor;
import tapl.TestArith.Eval1Impl;
import tapl.TestArith.PrintImpl;
import tyarith.CTy;
import tyarith.PrintTy;
import tyarith.TyAlg;
import tyarith.TyAlgFactory;
import tyarith.TyAlgMatcher;
import tyarith.TyAlgMatcherImpl;
import tyarith.TyAlgVisitor;
import tyarith.TyEqv;
import tyarith.Typeof;
import utils.BindingAlgFactory;
import utils.CBind;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.NoRuleApplies;

public class TestTyArith {

	static class IsNumericalValImpl implements IsNumericVal<CTerm>, TermAlgVisitor<Boolean> {}

	static class IsValImpl implements IsVal<CTerm>, TermAlgVisitor<Boolean> {}

	static class EvalImpl implements Eval<CTerm> {
		public CTerm eval1(CTerm e) {
			return e.accept(eval1);
		}

		public boolean isVal(CTerm e) {
			return e.accept(isVal);
		}
	}

	static class PrintTyImpl implements PrintTy<CTy, CBind>, TyAlgVisitor<IPrint<CBind>> {}
	static class TypeofImpl implements Typeof<CTerm, CTy, CBind>, TermAlgVisitor<ITypeof<CTy,CBind>> {
	  public boolean tyEqv(CTy ty1, CTy ty2) {
	    return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
	  }
	  public TyAlg<CTy> tyAlg() {
	    return tyFact;
	  }
	  public TyAlgMatcher<CTy, CTy> tyMatcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}
	static class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
	  public TyAlgMatcher<CTy, Boolean> matcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}

	static TermAlgFactory tmFact = new TermAlgFactory();
	static TyAlgFactory tyFact = new TyAlgFactory();
	static PrintImpl print = new PrintImpl();
	static IsNumericalValImpl isNumericalVal = new IsNumericalValImpl();
	static IsValImpl isVal = new IsValImpl();
	static Eval1Impl eval1 = new Eval1Impl();
	static EvalImpl eval = new EvalImpl();
	static BindingAlgFactory bindFact = new BindingAlgFactory();
	static Context<CBind> ctx = new Context<>(bindFact);

	static CTerm t = tmFact.TmTrue();
	static CTerm f = tmFact.TmFalse();
	static CTerm if_f_then_t_else_f = tmFact.TmIf(f, t, f);
	static CTerm zero = tmFact.TmZero();
	static CTerm pred_zero = tmFact.TmPred(zero);
	static CTerm succ_pred_0 = tmFact.TmSucc(tmFact.TmPred(zero));
	static CTerm succ_succ_0 = tmFact.TmSucc(tmFact.TmSucc(zero));
	static CTerm iszero_pred_succ_succ_0 = tmFact.TmIsZero(tmFact.TmPred(tmFact.TmSucc(tmFact.TmSucc(zero))));

	static String print(CTerm t) {
		return t.accept(print).print(ctx);
	}

	static void println(CTerm t) {
		System.out.println(t.accept(print).print(ctx));
	}

	@Test
	public void printTest() {
		assertEquals("if false then true else false", print(if_f_then_t_else_f));
		assertEquals("(iszero (pred 2))", print(iszero_pred_succ_succ_0));
	}

	@Test
	public void IsNumericalValTest() {
		assertTrue(zero.accept(isNumericalVal));
		assertTrue(succ_succ_0.accept(isNumericalVal));
		assertFalse(pred_zero.accept(isNumericalVal));
		assertFalse(f.accept(isNumericalVal));
	}

	@Test
	public void isValTest() {
		assertTrue(isVal.visitTerm(t));
		assertFalse(isVal.visitTerm(if_f_then_t_else_f));
	}

	@Test(expected=NoRuleApplies.class)
	public void eval1TestNoRuleApplies(){
		t.accept(eval1);
	}

	public void eval1Test(){
		assertEquals("false", print(if_f_then_t_else_f.accept(eval1)));
	}

	@Test
	public void testAll() {
		assertEquals("true", print(eval.eval(t)));
		assertEquals("false", print(eval.eval(if_f_then_t_else_f)));
		assertEquals("0", print(eval.eval(zero)));
		assertEquals("1", print(eval.eval(succ_pred_0)));
		assertEquals("false", print(eval.eval(iszero_pred_succ_succ_0)));
	}
	public static void main(String[] args) {
		println(eval.eval(t));
		println(eval.eval(if_f_then_t_else_f));
		println(eval.eval(zero));
		println(eval.eval(succ_pred_0));
		println(eval.eval(iszero_pred_succ_succ_0));
    }
}