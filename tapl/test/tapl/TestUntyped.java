package tapl;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

import untyped.Eval1;
import untyped.IsVal;
import untyped.Print;
import untyped.TmMap;
import untyped.termalg.external.Term;
import untyped.termalg.external.TermAlgFactory;
import untyped.termalg.external.TermAlgMatcher;
import untyped.termalg.external.TermAlgMatcherImpl;
import untyped.termalg.external.TermAlgVisitor;
import untyped.termalg.shared.GTermAlg;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.PrintBind;
import utils.TmMapCtx;
import utils.bindingalg.external.Bind;
import utils.bindingalg.external.BindingAlgFactory;
import utils.bindingalg.external.BindingAlgVisitor;

public class TestUntyped {
	class PrintImpl implements Print<Term, Bind>, TermAlgVisitor<IPrint<Bind>> {
		@Override public String printBind(Bind bind, Context<Bind> ctx) {
		  return new PrintBindImpl().visitBind(bind).print(ctx);
		}
	}

	class PrintBindImpl implements PrintBind<Bind>, BindingAlgVisitor<IPrint<Bind>> {}

	class IsValImpl implements IsVal<Term>, TermAlgVisitor<Boolean> {}

  class TmMapImpl implements TmMap<Term>, TermAlgVisitor<Function<TmMapCtx<Term>, Term>> {
    @Override public GTermAlg<Term, Term> alg() {
      return fact;
    }
  }

	class Eval1Impl implements Eval1<Term>, TermAlgVisitor<Term> {
	  @Override public Term termSubstTop(Term s, Term t) {
	    return deBruijn.termSubstTop(s, t);
	  }
		@Override public boolean isVal(Term e) {
			return isVal.visitTerm(e);
		}
		@Override public GTermAlg<Term, Term> alg() {
			return fact;
		}
		@Override public TermAlgMatcher<Term, Term> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	}

	class EvalImpl implements Eval<Term> {
		@Override public boolean isVal(Term t) {
			return t.accept(isVal);
		}
		@Override public Term eval1(Term t) {
		  return new Eval1Impl().visitTerm(t);
		}
	}

	TmMapImpl deBruijn = new TmMapImpl();
	TermAlgFactory fact = new TermAlgFactory();
	BindingAlgFactory bindFact = new BindingAlgFactory();
	Context<Bind> ctx = new Context<>(new BindingAlgFactory());
	PrintImpl print = new PrintImpl();
	PrintBindImpl printBind = new PrintBindImpl();
	IsValImpl isVal = new IsValImpl();
	EvalImpl eval = new EvalImpl();

	Term x = fact.TmVar(0, 1);
	Term id = fact.TmAbs("x", x);
	Term lam_x_xx = fact.TmAbs("x", fact.TmApp(x, x));
	Term id_lam_x_xx = fact.TmApp(id, fact.TmAbs("x", fact.TmApp(x, x)));

	@Test
	public void testPrint() {
		assertEquals("x", x.accept(print).print(ctx.addName("x")));
		assertEquals("\\x.x", id.accept(print).print(ctx));
		assertEquals("\\x.x \\x.x x", id_lam_x_xx.accept(print).print(ctx));
	}

	@Test
	public void testEval() {
		assertEquals("\\x.x", eval.eval(id).accept(print).print(ctx));
		assertEquals("\\x.x x", eval.eval(id_lam_x_xx).accept(print).print(ctx));
	}

	@Test
	public void testContext() throws Exception {
		assertEquals("{}", ctx.toString(bind -> bind.accept(printBind).print(ctx)));
		assertEquals("{(x,)}", ctx.addName("x").toString(bind -> bind.accept(printBind).print(ctx)));
	}

	@Test
	public void testShift() {
		// (\.\.1 (0 2)) -> (\.\.1 (0 4))
		Term e = fact.TmAbs("x", fact.TmAbs("y", fact.TmApp(fact.TmVar(1, 3), fact.TmApp(fact.TmVar(0, 3), fact.TmVar(2, 3)))));
		assertEquals("\\x.\\y.[bad index: 1/5 in {(y,), (x,)}] [bad index: 0/5 in {(y,), (x,)}] [bad index: 4/5 in {(y,), (x,)}]", deBruijn.termShift(2, e).accept(print).print(ctx));
	}

	// Exercise 6.2.5
	@Test
	public void testTermSubst() {
		Term e;

		e = fact.TmApp(fact.TmVar(0, 2), fact.TmVar(0, 2));
		assertEquals("b b", e.accept(print).print(ctx.addName("a").addName("b")));
		assertEquals("a a", deBruijn.termSubst(0, fact.TmVar(1, 2), e).accept(print).print(ctx.addName("a").addName("b")));

		e = fact.TmApp(fact.TmVar(0, 2), fact.TmAbs("x", fact.TmAbs("y", fact.TmVar(2, 4))));
		// [b -> a] (b \.x\.y b) = a (\.x\.y a)
		assertEquals("b \\x.\\y.b", e.accept(print).print(ctx.addName("a").addName("b")));
		assertEquals("a \\x.\\y.a", deBruijn.termSubst(0, fact.TmVar(1, 2), e).accept(print).print(ctx.addName("a").addName("b")));

		// [b -> a (\z.a)] (b (\x.b)) = (a (\z.a)) (\x.(a (\z.a)))
		e = fact.TmApp(fact.TmVar(0, 2), fact.TmAbs("x", fact.TmVar(1, 3)));
		assertEquals("b \\x.b", e.accept(print).print(ctx.addName("a").addName("b")));
		assertEquals("a \\z.a \\x.a \\z.a", deBruijn.termSubst(0, fact.TmApp(fact.TmVar(1, 2), fact.TmAbs("z", fact.TmVar(2, 3))), e).accept(print).print(ctx.addName("a").addName("b")));

		// [b -> a] (\b. b a) = (\.b b a)
		assertEquals("\\b.b a", fact.TmAbs("b", fact.TmApp(fact.TmVar(0, 2), fact.TmVar(1, 2))).accept(print).print(ctx.addName("a")));
		assertEquals("\\b_.b_ a", deBruijn.termSubst(0, fact.TmVar(1, 2), fact.TmAbs("b", fact.TmApp(fact.TmVar(0, 3), fact.TmVar(2, 3)))).accept(print).print(ctx.addName("a").addName("b")));

		// [b -> a] (\a. b a) = (\a_. a a_)
		assertEquals("\\a.b a", fact.TmAbs("a", fact.TmApp(fact.TmVar(1, 2), fact.TmVar(0, 2))).accept(print).print(ctx.addName("b")));
		assertEquals("\\a_.a a_", deBruijn.termSubst(0, fact.TmVar(1, 2), fact.TmAbs("a", fact.TmApp(fact.TmVar(1, 3), fact.TmVar(0, 3)))).accept(print).print(ctx.addName("a").addName("b")));
	}
}