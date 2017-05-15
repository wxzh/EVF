package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arith.CTerm;
import arith.Eval1;
import arith.IsNumericVal;
import arith.IsVal;
import arith.Print;
import arith.TermAlg;
import arith.TermAlgFactory;
import arith.TermAlgMatcher;
import arith.TermAlgMatcherImpl;
import arith.TermAlgVisitor;
import utils.BindingAlgFactory;
import utils.CBind;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.NoRuleApplies;

public class TestArith {

	static class IsNumericalValImpl implements IsNumericVal<CTerm>, TermAlgVisitor<Boolean> {}

	static class IsValImpl implements IsVal<CTerm>, TermAlgVisitor<Boolean> {}

	static class PrintImpl implements Print<CTerm, CBind>, TermAlgVisitor<IPrint<CBind>> {
		public TermAlgMatcher<CTerm, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	}

	static class Eval1Impl implements Eval1<CTerm>, TermAlgVisitor<CTerm> {
		public TermAlg<CTerm> alg() {
			return alg;
		}

		public TermAlgMatcher<CTerm, CTerm> matcher() {
			return new TermAlgMatcherImpl<>();
		}

		public boolean isNumericVal(CTerm t) {
			return isNumericalVal.visitTerm(t);
		}
	}

	static class EvalImpl implements Eval<CTerm> {
		public CTerm eval1(CTerm e) {
			return e.accept(eval1);
		}

		public boolean isVal(CTerm e) {
			return e.accept(isVal);
		}
	}

	static TermAlgFactory alg = new TermAlgFactory();
	static PrintImpl print = new PrintImpl();
	static IsNumericalValImpl isNumericalVal = new IsNumericalValImpl();
	static IsValImpl isVal = new IsValImpl();
	static Eval1Impl eval1 = new Eval1Impl();
	static EvalImpl eval = new EvalImpl();
	static BindingAlgFactory bindFact = new BindingAlgFactory();
	static Context<CBind> ctx = new Context<CBind>(bindFact);

	static CTerm t = alg.TmTrue();
	static CTerm f = alg.TmFalse();
	static CTerm if_f_then_t_else_f = alg.TmIf(f, t, f);
	static CTerm zero = alg.TmZero();
	static CTerm pred_zero = alg.TmPred(zero);
	static CTerm succ_pred_0 = alg.TmSucc(alg.TmPred(zero));
	static CTerm succ_succ_0 = alg.TmSucc(alg.TmSucc(zero));
	static CTerm iszero_pred_succ_succ_0 = alg.TmIsZero(alg.TmPred(alg.TmSucc(alg.TmSucc(zero))));

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