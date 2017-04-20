package tapl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

import fullref.Eval1;
import fullref.IsNumericVal;
import fullref.IsVal;
import fullref.JoinMeet;
import fullref.Print;
import fullref.PrintTy;
import fullref.Store;
import fullref.Subtype;
import fullref.TmMap;
import fullref.TyEqv;
import fullref.Typeof;
import fullref.termalg.external.Term;
import fullref.termalg.external.TermAlgFactory;
import fullref.termalg.external.TermAlgMatcher;
import fullref.termalg.external.TermAlgMatcherImpl;
import fullref.termalg.external.TermAlgVisitor;
import fullref.termalg.shared.GTermAlg;
import fullref.tyalg.external.Ty;
import fullref.tyalg.external.TyAlgFactory;
import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.external.TyAlgMatcherImpl;
import fullref.tyalg.external.TyAlgVisitor;
import fullref.tyalg.shared.GTyAlg;
import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import fullsimple.bindingalg.external.Bind;
import fullsimple.bindingalg.external.BindingAlgFactory;
import fullsimple.bindingalg.external.BindingAlgVisitor;
import library.Tuple2;
import typed.bindingalg.shared.GBindingAlg;
import utils.Context;
import utils.Eval;
import utils.IJoin;
import utils.IMeet;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;
import utils.TmMapCtx;

public class TestFullref {
	class PrintAll implements Print<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>, Ty>,
	    PrintTy<Ty, Bind<Term<Ty>,Ty>>, TyAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>>,
	    PrintBind<Bind<Term<Ty>,Ty>,Term<Ty>,Ty>, BindingAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>,Term<Ty>,Ty> {
	  @Override public String printBind(Bind<Term<Ty>, Ty> bind, Context<Bind<Term<Ty>, Ty>> ctx) {
	    return visitBind(bind).print(ctx);
	  }
	  @Override public String printTy(Ty ty, Context<Bind<Term<Ty>, Ty>> ctx) {
	    return visitTy(ty).print(ctx);
	  }
	  @Override public String printTerm(Term<Ty> t, Context<Bind<Term<Ty>, Ty>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	  @Override public TermAlgMatcher<Term<Ty>, Ty, String> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	}

	class EvalImpl implements Eval<Term<Ty>> {
	  Eval1Impl eval1 = new Eval1Impl();
    public Term<Ty> eval1(Term<Ty> t) {
      return eval1.visitTerm(t);
    }
    public boolean isVal(Term<Ty> t) {
      return new IsValImpl().visitTerm(t);
    }
	}

	class Eval1Impl implements Eval1<Term<Ty>,Ty,Bind<Term<Ty>,Ty>>, TermAlgVisitor<Term<Ty>,Ty> {
	  @Override public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	    return tmFact;
	  }
	  @Override public boolean isNumericVal(Term<Ty> t) {
	    return new IsNumericValImpl().visitTerm(t);
	  }
	  @Override public TermAlgMatcher<Term<Ty>, Ty, Term<Ty>> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	  Store<Term<Ty>> store = new Store<>();
	  @Override public Store<Term<Ty>> store() {
	    return store;
	  }
	  @Override public Term<Ty> termSubstTop(Term<Ty> s, Term<Ty> t) {
	    return new TmMapImpl().termSubstTop(s,t);
	  }
	  @Override public boolean isVal(Term<Ty> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	}

	class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean,Ty> {}
	class IsNumericValImpl implements IsNumericVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean,Ty> {}

  class TmMapImpl implements TmMap<Term<Ty>,Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>,Term<Ty>>, Ty> {
    public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
      return tmFact;
    }
  }

  class TypeofImpl implements Typeof<Term<Ty>,Ty,Bind<Term<Ty>,Ty>>, TermAlgVisitor<ITypeof<Ty,Bind<Term<Ty>,Ty>>, Ty> {
    public boolean tyEqv(Ty ty1, Ty ty2) {
      return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
    }
    public GBindingAlg<Bind<Term<Ty>, Ty>, Ty, Bind<Term<Ty>, Ty>> bindAlg() {
      return new BindingAlgFactory<>();
    }
    public Ty getTypeFromBind(Bind<Term<Ty>, Ty> bind) {
      return new GetTypeFromBindImpl().visitBind(bind);
    }
    public boolean subtype(Ty ty1, Ty ty2) {
      return new SubtypeImpl().subtype(ty1, ty2);
    }
    public GTyAlg<Ty, Ty> tyAlg() {
      return new TyAlgFactory();
    }
    public TyAlgMatcher<Ty, Ty> tyMatcher() {
      return new TyAlgMatcherImpl<>();
    }
    public Ty join(Ty ty1, Ty ty2) {
      return new JoinMeetImpl().join(ty1, ty2);
    }
  }

  class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Term<Ty>,Ty>,Term<Ty>,Ty>, BindingAlgVisitor<Ty,Term<Ty>,Ty> {}

  class SubtypeImpl implements Subtype<Ty>, TyAlgVisitor<ISubtype<Ty>> {
    public boolean tyEqv(Ty ty1, Ty ty2) {
      return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
    }
    public TyAlgMatcher<Ty, Boolean> matcher() {
      return new TyAlgMatcherImpl<>();
    }
  }

  class JoinMeetImpl implements JoinMeet<Ty> {
    public boolean subtype(Ty ty1, Ty ty2) {
      return new SubtypeImpl().visitTy(ty1).subtype(ty2);
    }
    class JoinImpl extends JoinMeetImpl implements JoinMeet.Join<Ty>, TyAlgVisitor<IJoin<Ty>> {}
    class MeetImpl extends JoinMeetImpl implements JoinMeet.Meet<Ty>, TyAlgVisitor<IMeet<Ty>> {}
    public Ty joinImpl(Ty ty1, Ty ty2) {
      return new JoinImpl().visitTy(ty1).join(ty2);
    }
    public Ty meetImpl(Ty ty1, Ty ty2) {
      return new MeetImpl().visitTy(ty1).meet(ty2);
    }
    public TyAlgMatcher<Ty, Ty> matcher() {
      return new TyAlgMatcherImpl<>();
    }
    public GTyAlg<Ty, Ty> alg() {
      return new TyAlgFactory();
    }
  }

  class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
    public TyAlgMatcher<Ty, Boolean> matcher() {
      return new TyAlgMatcherImpl<>();
    }
  }

	PrintAll print = new PrintAll();
	Context<Bind<Term<Ty>,Ty>> ctx = new Context<>(new BindingAlgFactory<>());
	EvalImpl eval = new EvalImpl();
	TypeofImpl typer = new TypeofImpl();

	TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	Term<Ty> x = tmFact.TmVar(1, 2);
	Term<Ty> one = tmFact.TmSucc(tmFact.TmZero());
	Term<Ty> get = tmFact.TmAbs("_", tyFact.TyUnit(), tmFact.TmDeref(x));
	Term<Ty> inc = tmFact.TmAbs("_", tyFact.TyUnit(), tmFact.TmAssign(x, tmFact.TmSucc(tmFact.TmDeref(x))));
	Term<Ty> term = tmFact.TmLet("x", tmFact.TmRef(one), tmFact.TmRecord(Arrays.asList(new Tuple2<>("get", get), new Tuple2<>("inc", inc))));
	@Test
	public void testEval() {
		assertEquals("let x=ref 1 in {get=lambda _:Unit. !x,inc=lambda _:Unit. x := (succ !x)}", print.printTerm(term, ctx));
		assertEquals("{get=lambda _:Unit. !<loc #0>,inc=lambda _:Unit. <loc #0> := (succ !<loc #0>)}", print.printTerm(eval.eval(term), ctx));
  }

	@Test
	public void testTypeof() {
	  assertEquals("Ref Nat",print.printTy(typer.visitTerm(tmFact.TmRef(one)).typeof(ctx),ctx));
	  //TODO:
	  //TySource
	  //TySink
    //let r = ref 1
    //!r
    //r := 0
    // !r
	}
}
