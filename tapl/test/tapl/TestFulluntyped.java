package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import org.junit.Test;

import fulluntyped.Eval1;
import fulluntyped.IsNumericVal;
import fulluntyped.Print;
import fulluntyped.PrintBind;
import fulluntyped.TmMap;
import fulluntyped.bindingalg.external.Bind;
import fulluntyped.bindingalg.external.BindingAlgFactory;
import fulluntyped.bindingalg.external.BindingAlgMatcher;
import fulluntyped.bindingalg.external.BindingAlgMatcherImpl;
import fulluntyped.bindingalg.external.BindingAlgVisitor;
import fulluntyped.termalg.external.Term;
import fulluntyped.termalg.external.TermAlgFactory;
import fulluntyped.termalg.external.TermAlgMatcher;
import fulluntyped.termalg.external.TermAlgMatcherImpl;
import fulluntyped.termalg.external.TermAlgVisitor;
import fulluntyped.termalg.shared.GTermAlg;
import library.Tuple2;
import utils.Context;
import utils.Eval;
import utils.IPrint;
import utils.TmMapCtx;

public class TestFulluntyped {
	class PrintImpl implements Print<Term, Bind<Term>>, TermAlgVisitor<IPrint<Bind<Term>>> {
		public TermAlgMatcher<Term, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}

		public String printBind(Bind<Term> bind, Context<Bind<Term>> ctx) {
		  return new PrintBindImpl().visitBind(bind).print(ctx);
		}
	}

	class PrintBindImpl implements PrintBind<Bind<Term>, Term>, BindingAlgVisitor<IPrint<Bind<Term>>, Term> {
	  public String printTerm(Term t, Context<Bind<Term>> ctx) {
	    return new PrintImpl().visitTerm(t).print(ctx);
	  }
	}

	class IsNumericValImpl implements IsNumericVal<Term>, TermAlgVisitor<Boolean> {}

	class IsValImpl implements fulluntyped.IsVal<Term>, TermAlgVisitor<Boolean> {}

  class TmMapImpl implements TmMap<Term>, TermAlgVisitor<Function<TmMapCtx<Term>, Term>> {
    public GTermAlg<Term, Term> alg() {
      return fact;
    }
  }

	abstract class Eval1Impl implements Eval1<Term, Bind<Term>>, TermAlgVisitor<Term> {
	  @Override public Term termSubstTop(Term s, Term t) {
	    return deBruijn.termSubstTop(s, t);
	  }

		@Override public boolean isNumericVal(Term e) {
			return new IsNumericValImpl().visitTerm(e);
		}

		@Override public boolean isVal(Term e) {
			return isVal.visitTerm(e);
		}

		@Override public BindingAlgMatcher<Bind<Term>, Term, Term> bindMatcher() {
			return new BindingAlgMatcherImpl<>();
		}

		@Override public GTermAlg<Term, Term> alg() {
			return fact;
		}

		public TermAlgMatcher<Term, Term> matcher() {
			return new TermAlgMatcherImpl<>();
		}
	}

	abstract class EvalImpl implements Eval<Term> {
		@Override public boolean isVal(Term t) {
			return t.accept(isVal);
		}
	}

	EvalImpl evalCtx(Context<Bind<Term>> ctx) {
		return new EvalImpl() {
			public Term eval1(Term t) {
				return t.accept(new Eval1Impl() {
					public Context<Bind<Term>> ctx() {
						return ctx;
        }});
    }};
	}

	TmMapImpl deBruijn = new TmMapImpl();
	TermAlgFactory fact = new TermAlgFactory();
	BindingAlgFactory<Term> bindFact = new BindingAlgFactory<>();
	Context<Bind<Term>> ctx = new Context<>(new BindingAlgFactory<>());
	PrintImpl print = new PrintImpl();
	PrintBindImpl printBind = new PrintBindImpl();
	IsValImpl isVal = new IsValImpl();
	EvalImpl eval = evalCtx(ctx);

	Term t = fact.TmTrue();
	Term f = fact.TmFalse();
	Term if_f_then_t_else_f = fact.TmIf(f, t, f);
	Term x = fact.TmVar(0, 1);
	Term id = fact.TmAbs("x", x);
	Term lam_x_xx = fact.TmAbs("x", fact.TmApp(x, x));
	Term id_lam_x_xx = fact.TmApp(id, fact.TmAbs("x", fact.TmApp(x, x)));
	Term record = fact.TmRecord(Arrays.asList(new Tuple2<>("x", id), new Tuple2<>("y", id_lam_x_xx)));
	Term proj = fact.TmProj(record, "x");
	Term hello = fact.TmString("hello");
	Term timesfloat = fact.TmTimesFloat(fact.TmTimesFloat(fact.TmFloat(2f), fact.TmFloat(3f)),
			fact.TmTimesFloat(fact.TmFloat(4f), fact.TmFloat(5f)));
	Term o = fact.TmZero();
	Term succ_pred_0 = fact.TmSucc(fact.TmPred(o));
	Term let_x_t_in_x = fact.TmLet("x", t, x);
	Term mixed = fact.TmLet("t", fact.TmApp(proj, t), fact.TmIf(fact.TmVar(0, 1), o, succ_pred_0));

	Context<Bind<Term>> ctx2 = ctx.addBinding("x", bindFact.TmAbbBind(t)).addName("y");

	@Test
	public void testPrint() {
		assertEquals("true", t.accept(print).print(ctx));
		assertEquals("if false then true else false", if_f_then_t_else_f.accept(print).print(ctx));
		assertEquals("x", x.accept(print).print(ctx.addName("x")));
		assertEquals("\\x.x", id.accept(print).print(ctx));
		assertEquals("\\x.x \\x.x x", id_lam_x_xx.accept(print).print(ctx));
		assertEquals("{x=\\x.x,y=\\x.x \\x.x x}", record.accept(print).print(ctx));
		assertEquals("{x=\\x.x,y=\\x.x \\x.x x}.x", proj.accept(print).print(ctx));
		assertEquals("hello", hello.accept(print).print(ctx));
		assertEquals("timesfloat timesfloat 2.0 3.0 timesfloat 4.0 5.0", timesfloat.accept(print).print(ctx));
		assertEquals("0", o.accept(print).print(ctx));

		assertEquals("(succ (pred 0))", succ_pred_0.accept(print).print(ctx));
		assertEquals("let x=true in x", let_x_t_in_x.accept(print).print(ctx));
		assertEquals("let t={x=\\x.x,y=\\x.x \\x.x x}.x true in if t then 0 else (succ (pred 0))", mixed.accept(print).print(ctx));
	}

	@Test
	public void testIsVal() {
		assertTrue(fact.TmRecord(Collections.emptyList()).accept(isVal));
		assertTrue(fact.TmRecord(Collections.singletonList(new Tuple2<>("x", id))).accept(isVal));
		assertFalse(fact.TmRecord(Collections.singletonList(new Tuple2<>("x", id_lam_x_xx))).accept(isVal));
		assertFalse(record.accept(isVal));
		assertTrue(fact.TmRecord(Collections.singletonList(new Tuple2<>("x", t))).accept(isVal));
		assertTrue(fact.TmRecord(Arrays.asList(new Tuple2<>("x", t), new Tuple2<>("y", f))).accept(isVal));
	}

	@Test
	public void testEval() {
		assertEquals("true", eval.eval(t).accept(print).print(ctx));
		assertEquals("false", eval.eval(if_f_then_t_else_f).accept(print).print(ctx));
		assertEquals("true", evalCtx(ctx.addBinding("x", bindFact.TmAbbBind(t))).eval(x).accept(print).print(ctx));
		assertEquals("\\x.x", eval.eval(id).accept(print).print(ctx));
		assertEquals("0", eval.eval(fact.TmApp(id, o)).accept(print).print(ctx));
		assertEquals("\\x.x x", eval.eval(id_lam_x_xx).accept(print).print(ctx));
		assertEquals("{x=\\x.x,y=\\x.x x}", eval.eval(record).accept(print).print(ctx));
		assertEquals("\\x.x", eval.eval(proj).accept(print).print(ctx));
		assertEquals("hello", eval.eval(hello).accept(print).print(ctx));
		assertEquals("0.0", eval.eval(fact.TmFloat(0l)).accept(print).print(ctx));
		assertEquals("120.0", eval.eval(timesfloat).accept(print).print(ctx));
		assertEquals("0", eval.eval(o).accept(print).print(ctx));
		assertEquals("1", eval.eval(succ_pred_0).accept(print).print(ctx));
		assertEquals("true", eval.eval(let_x_t_in_x).accept(print).print(ctx));
		assertEquals("0", eval.eval(mixed).accept(print).print(ctx));
	}

	@Test
	public void testContext() throws Exception {
		assertEquals("{}", ctx.toString(bind -> bind.accept(printBind).print(ctx)));
		assertEquals("{(x,)}", ctx.addName("x").toString(bind -> bind.accept(printBind).print(ctx)));
		assertEquals("{(x,true)}", ctx.addBinding("x", bindFact.TmAbbBind(t)).toString(bind -> bind.accept(printBind).print(ctx)));
		assertEquals("{(y,), (x,true)}", ctx2.toString(bind -> bind.accept(printBind).print(ctx)));
		assertEquals("y", ctx2.index2Name(0));
		assertEquals("x", ctx2.index2Name(1));
		assertEquals(0, ctx2.name2Index("y"));
		assertEquals(1, ctx2.name2Index("x"));
		assertTrue(ctx2.isNameBound("y"));
		assertTrue(ctx2.isNameBound("x"));
		assertFalse(ctx2.isNameBound("z"));
		assertEquals("x_", ctx2.pickFreshName("x")._2);
		assertEquals(3, ctx2.pickFreshName("x")._1.length());
		assertEquals("z", ctx2.pickFreshName("z")._2);
		assertEquals(3, ctx2.pickFreshName("z")._1.length());
	}

	@Test
	public void testShift() {
		Term x2 = fact.TmVar(1, 2);
		Term y = fact.TmVar(0, 2);
		assertEquals("if x then y else if y then x else x", deBruijn.termShift(0, fact.TmIf(x2, y, fact.TmIf(y, x2, x2))).accept(print).print(ctx2));
		assertEquals("x", deBruijn.termShift(1, x).accept(print).print(ctx2));

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