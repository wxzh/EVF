package tapl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

import fullref.CTerm;
import fullref.CTy;
import fullref.Eval1;
import fullref.IsNumericVal;
import fullref.IsVal;
import fullref.JoinMeet;
import fullref.Print;
import fullref.PrintTy;
import fullref.Store;
import fullref.Subtype;
import fullref.TermAlg;
import fullref.TermAlgFactory;
import fullref.TermAlgMatcher;
import fullref.TermAlgMatcherImpl;
import fullref.TermAlgVisitor;
import fullref.TmMap;
import fullref.TyAlg;
import fullref.TyAlgFactory;
import fullref.TyAlgMatcher;
import fullref.TyAlgMatcherImpl;
import fullref.TyAlgVisitor;
import fullref.TyEqv;
import fullref.Typeof;
import fullsimple.BindingAlg;
import fullsimple.BindingAlgFactory;
import fullsimple.BindingAlgVisitor;
import fullsimple.CBind;
import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import library.Tuple2;
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
	class PrintAll implements Print<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>, CTy>,
	    PrintTy<CTy, CBind<CTerm<CTy>,CTy>>, TyAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>>,
	    PrintBind<CBind<CTerm<CTy>,CTy>,CTerm<CTy>,CTy>, BindingAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>,CTerm<CTy>,CTy> {
	  @Override public String printBind(CBind<CTerm<CTy>, CTy> bind, Context<CBind<CTerm<CTy>, CTy>> ctx) {
	    return visitBind(bind).print(ctx);
	  }
	  @Override public String printTy(CTy ty, Context<CBind<CTerm<CTy>, CTy>> ctx) {
	    return visitTy(ty).print(ctx);
	  }
	  @Override public String printTerm(CTerm<CTy> t, Context<CBind<CTerm<CTy>, CTy>> ctx) {
	    return visitTerm(t).print(ctx);
	  }
	  @Override public TermAlgMatcher<CTerm<CTy>, CTy, String> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	}

	class EvalImpl implements Eval<CTerm<CTy>> {
	  Eval1Impl eval1 = new Eval1Impl();
    public CTerm<CTy> eval1(CTerm<CTy> t) {
      return eval1.visitTerm(t);
    }
    public boolean isVal(CTerm<CTy> t) {
      return new IsValImpl().visitTerm(t);
    }
	}

	class Eval1Impl implements Eval1<CTerm<CTy>,CTy,CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<CTerm<CTy>,CTy> {
	  @Override public TermAlg<CTerm<CTy>, CTy> alg() {
	    return tmFact;
	  }
	  @Override public boolean isNumericVal(CTerm<CTy> t) {
	    return new IsNumericValImpl().visitTerm(t);
	  }
	  @Override public TermAlgMatcher<CTerm<CTy>, CTy, CTerm<CTy>> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	  Store<CTerm<CTy>> store = new Store<>();
	  @Override public Store<CTerm<CTy>> store() {
	    return store;
	  }
	  @Override public CTerm<CTy> termSubstTop(CTerm<CTy> s, CTerm<CTy> t) {
	    return new TmMapImpl().termSubstTop(s,t);
	  }
	  @Override public boolean isVal(CTerm<CTy> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	}

	class IsValImpl implements IsVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean,CTy> {}
	class IsNumericValImpl implements IsNumericVal<CTerm<CTy>, CTy>, TermAlgVisitor<Boolean,CTy> {}

  class TmMapImpl implements TmMap<CTerm<CTy>,CTy>, TermAlgVisitor<Function<TmMapCtx<CTerm<CTy>>,CTerm<CTy>>, CTy> {
    public TermAlg<CTerm<CTy>, CTy> alg() {
      return tmFact;
    }
  }

  class TypeofImpl implements Typeof<CTerm<CTy>,CTy,CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<ITypeof<CTy,CBind<CTerm<CTy>,CTy>>, CTy> {
    public boolean tyEqv(CTy ty1, CTy ty2) {
      return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
    }
    public BindingAlg<CBind<CTerm<CTy>, CTy>, CTerm<CTy>, CTy> bindAlg() {
      return new BindingAlgFactory<>();
    }
    public CTy getTypeFromBind(CBind<CTerm<CTy>, CTy> bind) {
      return new GetTypeFromBindImpl().visitBind(bind);
    }
    public boolean subtype(CTy ty1, CTy ty2) {
      return new SubtypeImpl().subtype(ty1, ty2);
    }
    public TyAlg<CTy> tyAlg() {
      return new TyAlgFactory();
    }
    public TyAlgMatcher<CTy, CTy> tyMatcher() {
      return new TyAlgMatcherImpl<>();
    }
    public CTy join(CTy ty1, CTy ty2) {
      return new JoinMeetImpl().join(ty1, ty2);
    }
  }

  class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTerm<CTy>,CTy>,CTerm<CTy>,CTy>, BindingAlgVisitor<CTy,CTerm<CTy>,CTy> {}

  class SubtypeImpl implements Subtype<CTy>, TyAlgVisitor<ISubtype<CTy>> {
    public boolean tyEqv(CTy ty1, CTy ty2) {
      return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
    }
    public TyAlgMatcher<CTy, Boolean> matcher() {
      return new TyAlgMatcherImpl<>();
    }
  }

  class JoinMeetImpl implements JoinMeet<CTy> {
    public boolean subtype(CTy ty1, CTy ty2) {
      return new SubtypeImpl().visitTy(ty1).subtype(ty2);
    }
    class JoinImpl extends JoinMeetImpl implements JoinMeet.Join<CTy>, TyAlgVisitor<IJoin<CTy>> {}
    class MeetImpl extends JoinMeetImpl implements JoinMeet.Meet<CTy>, TyAlgVisitor<IMeet<CTy>> {}
    public CTy joinImpl(CTy ty1, CTy ty2) {
      return new JoinImpl().visitTy(ty1).join(ty2);
    }
    public CTy meetImpl(CTy ty1, CTy ty2) {
      return new MeetImpl().visitTy(ty1).meet(ty2);
    }
    public TyAlgMatcher<CTy, CTy> matcher() {
      return new TyAlgMatcherImpl<>();
    }
    public TyAlg<CTy> alg() {
      return new TyAlgFactory();
    }
  }

  class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
    public TyAlgMatcher<CTy, Boolean> matcher() {
      return new TyAlgMatcherImpl<>();
    }
  }

	PrintAll print = new PrintAll();
	Context<CBind<CTerm<CTy>,CTy>> ctx = new Context<>(new BindingAlgFactory<>());
	EvalImpl eval = new EvalImpl();
	TypeofImpl typer = new TypeofImpl();

	TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();
	CTerm<CTy> x = tmFact.TmVar(1, 2);
	CTerm<CTy> one = tmFact.TmSucc(tmFact.TmZero());
	CTerm<CTy> get = tmFact.TmAbs("_", tyFact.TyUnit(), tmFact.TmDeref(x));
	CTerm<CTy> inc = tmFact.TmAbs("_", tyFact.TyUnit(), tmFact.TmAssign(x, tmFact.TmSucc(tmFact.TmDeref(x))));
	CTerm<CTy> term = tmFact.TmLet("x", tmFact.TmRef(one), tmFact.TmRecord(Arrays.asList(new Tuple2<>("get", get), new Tuple2<>("inc", inc))));
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
