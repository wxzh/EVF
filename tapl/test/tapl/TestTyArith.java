package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arith.IsNumericVal;
import arith.IsVal;
import arith.termalg.external.Term;
import arith.termalg.external.TermAlgFactory;
import arith.termalg.external.TermAlgVisitor;
import tapl.TestArith.Eval1Impl;
import tapl.TestArith.PrintImpl;
import tyarith.PrintTy;
import tyarith.TyEqv;
import tyarith.Typeof;
import tyarith.tyalg.external.Ty;
import tyarith.tyalg.external.TyAlgFactory;
import tyarith.tyalg.external.TyAlgMatcher;
import tyarith.tyalg.external.TyAlgMatcherImpl;
import tyarith.tyalg.external.TyAlgVisitor;
import tyarith.tyalg.shared.GTyAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.ITyEqv;
import utils.ITypeof;
import utils.NoRuleApplies;
import utils.bindingalg.external.Bind;
import utils.bindingalg.external.BindingAlgFactory;

public class TestTyArith {

	static class IsNumericalValImpl implements IsNumericVal<Term>, TermAlgVisitor<Boolean> {}

	static class IsValImpl implements IsVal<Term>, TermAlgVisitor<Boolean> {}

	static class EvalImpl implements Eval<Term> {
		public Term eval1(Term e) {
			return e.accept(eval1);
		}

		public boolean isVal(Term e) {
			return e.accept(isVal);
		}
	}

	static class PrintTyImpl implements PrintTy<Ty, Bind>, TyAlgVisitor<IPrint<Bind>> {}
	static class TypeofImpl implements Typeof<Term, Ty, Bind>, TermAlgVisitor<ITypeof<Ty,Bind>> {
	  public boolean tyEqv(Ty ty1, Ty ty2) {
	    return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
	  }
	  public GTyAlg<Ty, Ty> tyAlg() {
	    return tyFact;
	  }
	  public TyAlgMatcher<Ty, Ty> tyMatcher() {
	    return new TyAlgMatcherImpl<>();
	  }
	}
	static class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
	  public TyAlgMatcher<Ty, Boolean> matcher() {
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
	static Context<Bind> ctx = new Context<Bind>(bindFact);

	static Term t = tmFact.TmTrue();
	static Term f = tmFact.TmFalse();
	static Term if_f_then_t_else_f = tmFact.TmIf(f, t, f);
	static Term zero = tmFact.TmZero();
	static Term pred_zero = tmFact.TmPred(zero);
	static Term succ_pred_0 = tmFact.TmSucc(tmFact.TmPred(zero));
	static Term succ_succ_0 = tmFact.TmSucc(tmFact.TmSucc(zero));
	static Term iszero_pred_succ_succ_0 = tmFact.TmIsZero(tmFact.TmPred(tmFact.TmSucc(tmFact.TmSucc(zero))));

	static String print(Term t) {
		return t.accept(print).print(ctx);
	}

	static void println(Term t) {
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