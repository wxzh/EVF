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

	class IsNumericalValImpl implements IsNumericVal<Term>, TermAlgVisitor<Boolean> {
	}

	class IsValImpl implements IsVal<Term>, TermAlgVisitor<Boolean> {
	}

	class PrintImpl implements Print<Term, Bind>, TermAlgVisitor<IPrint<Bind>> {
		public TermAlgMatcher<Term, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	}

	class Eval1Impl implements Eval1<Term>, TermAlgVisitor<Term> {
		public GTermAlg<Term, Term> alg() {
			return alg;
		}

		@Override
		public TermAlgMatcher<Term, Term> matcher() {
			return new TermAlgMatcherImpl<>();
		}

		@Override
		public IsNumericVal<Term> isNumericVal() {
			return isNumericalVal;
		}
	}

	class EvalImpl implements Eval<Term> {
		@Override
		public Term eval1(Term e) {
			return e.accept(eval1);
		}

		@Override
		public boolean isVal(Term e) {
			return e.accept(isVal);
		}
	}

	private TermAlgFactory alg = new TermAlgFactory();
	private PrintImpl print = new PrintImpl();
	private IsNumericalValImpl isNumericalVal = new IsNumericalValImpl();
	private IsValImpl isVal = new IsValImpl();
	private Eval1Impl eval1 = new Eval1Impl();
	private EvalImpl eval = new EvalImpl();
	private BindingAlgFactory bindFact = new BindingAlgFactory();
	private Context<Bind> ctx = new Context<Bind>(bindFact);

	private Term t = alg.TmTrue();
	private Term f = alg.TmFalse();
	private Term if_f_then_t_else_f = alg.TmIf(f, t, f);
	private Term zero = alg.TmZero();
	private Term pred_zero = alg.TmPred(zero);
	private Term succ_pred_0 = alg.TmSucc(alg.TmPred(zero));
	private Term pred_succ_0 = alg.TmPred(alg.TmSucc(zero));
	private Term succ_succ_0 = alg.TmSucc(alg.TmSucc(zero));
	private Term iszero_pred_succ_succ_0 = alg.TmIsZero(alg.TmPred(alg.TmSucc(alg.TmSucc(zero))));

	String print(Term t) {
		return t.accept(print).print(ctx);
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
	public void evalTest() {
		assertEquals("true", print(eval.eval(t)));
		assertEquals("false", print(eval.eval(if_f_then_t_else_f)));
		assertEquals("1", print(eval.eval(succ_pred_0)));
		assertEquals("0", print(eval.eval(pred_succ_0)));
		assertEquals("false", print(eval.eval(iszero_pred_succ_succ_0)));
	}
}