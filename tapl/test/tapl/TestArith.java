package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import arith.Eval1;
import arith.IsNumericVal;
import arith.IsVal;
import arith.Print;
import arith.termalg.external.Term;
import arith.termalg.external.TermAlgFactory;
import arith.termalg.external.TermAlgMatcher;
import arith.termalg.external.TermAlgMatcherImpl;
import arith.termalg.external.TermAlgVisitor;
import arith.termalg.shared.GTermAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.NoRuleApplies;
import utils.bindingalg.external.Bind;
import utils.bindingalg.external.BindingAlgFactory;

public class TestArith {

	static class IsNumericalValImpl implements IsNumericVal<Term>, TermAlgVisitor<Boolean> {}

	static class IsValImpl implements IsVal<Term>, TermAlgVisitor<Boolean> {}

	static class PrintImpl implements Print<Term, Bind>, TermAlgVisitor<IPrint<Bind>> {
		public TermAlgMatcher<Term, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	}

	static class Eval1Impl implements Eval1<Term>, TermAlgVisitor<Term> {
		public GTermAlg<Term, Term> alg() {
			return alg;
		}

		public TermAlgMatcher<Term, Term> matcher() {
			return new TermAlgMatcherImpl<>();
		}

		public boolean isNumericVal(Term t) {
			return isNumericalVal.visitTerm(t);
		}
	}

	static class EvalImpl implements Eval<Term> {
		public Term eval1(Term e) {
			return e.accept(eval1);
		}

		public boolean isVal(Term e) {
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
	static Context<Bind> ctx = new Context<Bind>(bindFact);

	static Term t = alg.TmTrue();
	static Term f = alg.TmFalse();
	static Term if_f_then_t_else_f = alg.TmIf(f, t, f);
	static Term zero = alg.TmZero();
	static Term pred_zero = alg.TmPred(zero);
	static Term succ_pred_0 = alg.TmSucc(alg.TmPred(zero));
	static Term succ_succ_0 = alg.TmSucc(alg.TmSucc(zero));
	static Term iszero_pred_succ_succ_0 = alg.TmIsZero(alg.TmPred(alg.TmSucc(alg.TmSucc(zero))));

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