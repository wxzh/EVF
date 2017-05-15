package tapl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import fullsimple.BindingAlg;
import fullsimple.BindingAlgFactory;
import fullsimple.BindingAlgVisitor;
import fullsimple.CBind;
import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import fullsub.CTerm;
import fullsub.CTy;
import fullsub.Eval1;
import fullsub.IsNumericVal;
import fullsub.IsVal;
import fullsub.JoinMeet;
import fullsub.Print;
import fullsub.PrintTy;
import fullsub.Subtype;
import fullsub.TermAlg;
import fullsub.TermAlgFactory;
import fullsub.TermAlgMatcher;
import fullsub.TermAlgMatcherImpl;
import fullsub.TermAlgVisitor;
import fullsub.TmMap;
import fullsub.TyAlg;
import fullsub.TyAlgFactory;
import fullsub.TyAlgMatcher;
import fullsub.TyAlgMatcherImpl;
import fullsub.TyAlgVisitor;
import fullsub.TyEqv;
import fullsub.Typeof;
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

public class TestFullsub {
	class JoinMeetImpl implements JoinMeet<CTy> {
	  public boolean subtype(CTy ty1, CTy ty2) {
	    return typer.subtype(ty1, ty2);
	  }

		@Override public TyAlgMatcher<CTy, CTy> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public TyAlg<CTy> alg() {
			return new TyAlgFactory();
		}

		class JoinImpl extends JoinMeetImpl implements Join<CTy>, TyAlgVisitor<IJoin<CTy>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<CTy>, TyAlgVisitor<IMeet<CTy>> {}

		@Override public CTy joinImpl(CTy ty1, CTy ty2) {
		  return new JoinImpl().visitTy(ty1).join(ty2);
		}

		@Override public CTy meetImpl(CTy ty1, CTy ty2) {
		  return new MeetImpl().visitTy(ty1).meet(ty2);
		}
	}

	class SubtypeImpl implements Subtype<CTy>, TyAlgVisitor<ISubtype<CTy>> {
		@Override public boolean tyEqv(CTy ty1, CTy ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TyEqvImpl implements TyEqv<CTy>, TyAlgVisitor<ITyEqv<CTy>> {
		@Override public TyAlgMatcher<CTy, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class PrintAll implements Print<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, PrintTy<CTy, CBind<CTerm<CTy>,CTy>>, PrintBind<CBind<CTerm<CTy>,CTy>, CTerm<CTy>, CTy>,
	    TermAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>, CTy>, TyAlgVisitor<IPrint<CBind<CTerm<CTy>, CTy>>>, BindingAlgVisitor<IPrint<CBind<CTerm<CTy>,CTy>>, CTerm<CTy>, CTy> {
		public TermAlgMatcher<CTerm<CTy>, CTy, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public String printTy(CTy ty, Context<CBind<CTerm<CTy>,CTy>> ctx) {
		  return visitTy(ty).print(ctx);
		}
		public String printBind(CBind<CTerm<CTy>,CTy> bind, Context<CBind<CTerm<CTy>, CTy>> ctx) {
		  return visitBind(bind).print(ctx);
		}
		public String printTerm(CTerm<CTy> t, Context<CBind<CTerm<CTy>, CTy>> ctx) {
		  return visitTerm(t).print(ctx);
		}
	}

	class TypeofImpl implements Typeof<CTerm<CTy>, CTy, CBind<CTerm<CTy>,CTy>>, TermAlgVisitor<ITypeof<CTy, CBind<CTerm<CTy>,CTy>>, CTy>{
	  public CTy join(CTy ty1, CTy ty2) {
	    return joinMeet.join(ty1, ty2);
	  }

		public BindingAlg<CBind<CTerm<CTy>,CTy>, CTerm<CTy>, CTy> bindAlg() {
			return bindFact;
		}

		class GetTypeFromBindImpl implements GetTypeFromBind<CBind<CTerm<CTy>,CTy>, CTerm<CTy>, CTy>, BindingAlgVisitor<CTy, CTerm<CTy>,CTy> {}

		public CTy getTypeFromBind(CBind<CTerm<CTy>,CTy> bind) {
		  return new GetTypeFromBindImpl().visitBind(bind);
		}

		public boolean tyEqv(CTy ty1, CTy ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		public TyAlgMatcher<CTy, CTy> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		public TyAlg<CTy> tyAlg() {
			return tyFact;
		}

		@Override public boolean subtype(CTy ty1, CTy ty2) {
		  return new SubtypeImpl().subtype(ty1, ty2);
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

	TermAlgFactory<CTy> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();

	PrintAll print = new PrintAll();
	JoinMeetImpl joinMeet = new JoinMeetImpl();
	TypeofImpl typer = new TypeofImpl();

	BindingAlgFactory<CTerm<CTy>,CTy> bindFact = new BindingAlgFactory<>();
	Context<CBind<CTerm<CTy>,CTy>> ctx = new Context<CBind<CTerm<CTy>,CTy>>(bindFact);

	CTy top = tyFact.TyTop();
	CTy top2top = tyFact.TyArr(top, top);
	CTy bool = tyFact.TyBool();
	CTy ty_rcd = tyFact.TyRecord(asList(new Tuple2<>("x", top2top)));
	CTy rcd1 = tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", bool)));
	CTy rcd2 = tyFact.TyRecord(asList(new Tuple2<>("y", bool), new Tuple2<>("x", top)));

	CTerm<CTy> var = tmFact.TmVar(0, 1);
	CTerm<CTy> id = tmFact.TmAbs("x", top, var);
	CTerm<CTy> id_top2top = tmFact.TmAbs("x", top2top, var);
	CTerm<CTy> id_app_id = tmFact.TmApp(id, id);
	CTerm<CTy> id_top2top_app_id = tmFact.TmApp(id_top2top, id);
	CTerm<CTy> id_rcd = tmFact.TmAbs("r", ty_rcd, tmFact.TmApp(tmFact.TmProj(var, "x"), tmFact.TmProj(var, "x")));
	CTerm<CTy> rcd = tmFact.TmRecord(asList(new Tuple2<>("x", id), new Tuple2<>("y", id)));
	CTerm<CTy> id_rcd_app_rcd = tmFact.TmApp(id_rcd, rcd);

	CTerm<CTy> t = tmFact.TmTrue();
	CTerm<CTy> f = tmFact.TmFalse();
	// if true {x: true, y: false, a: false} else ...
	CTerm<CTy> if_rcd = tmFact.TmIf(t,
			tmFact.TmRecord(asList(new Tuple2<>("x", t), new Tuple2<>("y", f), new Tuple2<>("a", f))),
			tmFact.TmRecord(asList(new Tuple2<>("y", f), new Tuple2<>("x", tmFact.TmRecord(asList())), new Tuple2<>("b", f))));

	@Test
	public void printTest() {
		assertEquals("lambda x:Top. x", print.printTerm(id, ctx));
		assertEquals("lambda x:(Top -> Top). x", print.printTerm(id_top2top, ctx));
		assertEquals("lambda r:{x:(Top -> Top)}. r.x r.x {x=lambda x:Top. x,y=lambda x:Top. x}", print.printTerm(id_rcd_app_rcd, ctx));
		assertEquals("if true then {x=true,y=false,a=false} else {y=false,x={},b=false}", print.printTerm(if_rcd,ctx));
		assertEquals("({x:(Top -> Top)} -> Top)", print.printTy(id_rcd.accept(typer).typeof(ctx),ctx));
	}


	@Test
	public void tyEqvRecordTest() {
		assertTrue(typer.tyEqv(rcd1, rcd2));
		assertTrue(typer.tyEqv(rcd2, rcd1));
	}

	@Test
	public void subtypeRecordTest() {
	  // S-RcdTop
		assertTrue(typer.subtype(rcd1, top));
	  // S-RcdRefl
		assertTrue(typer.subtype(rcd1, rcd1));
		// S-RcdPerm
		assertTrue(typer.subtype(rcd1, rcd2));
		assertTrue(typer.subtype(rcd2, rcd1));
	  // S-RcdDepth
		assertTrue(typer.subtype(tyFact.TyRecord(asList(new Tuple2<>("x", bool), new Tuple2<>("y", bool))),
		    tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", top)))));
		assertFalse(typer.subtype(tyFact.TyRecord(asList(new Tuple2<>("x", bool), new Tuple2<>("y", top))),
		    tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", bool)))));
		// S-RcdWidth
		assertTrue(typer.subtype(tyFact.TyRecord(asList(new Tuple2<>("x", bool))), tyFact.TyRecord(asList())));

		// {x:{a:Bool,b:Bool},y:Bool} <: {x:{a:Bool}}
		assertTrue(typer.subtype(tyFact.TyRecord(asList(new Tuple2<>("x",tyFact.TyRecord(asList(new Tuple2<>("a",bool), new Tuple2<>("b",bool)))), new Tuple2<>("y",bool))),
		    tyFact.TyRecord(asList(new Tuple2<>("x", tyFact.TyRecord(asList(new Tuple2<>("a", bool))))))));
	}

	@Test
	public void joinRecordTest() {
		assertTrue(typer.tyEqv(typer.join(rcd1, rcd1), rcd1));
		assertTrue(typer.tyEqv(typer.join(rcd1, top2top), top));

		CTy rcd = tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("z", tyFact.TyBool())));
		assertTrue(typer.tyEqv(typer.join(rcd, rcd1), tyFact.TyRecord(asList(new Tuple2<>("x", top)))));
		assertTrue(typer.tyEqv(
		    typer.join(tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("z", tyFact.TyBool()))), rcd1),
		    tyFact.TyRecord(asList(new Tuple2<>("x", top)))));
	}

	@Test
	public void meetTest() {
		assertTrue(typer.tyEqv(tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", tyFact.TyBool()))), typer.visitTerm(if_rcd).typeof(ctx)));
	}
	@Test
	public void typeofTest() {
		assertTrue(typer.tyEqv(top2top, id.accept(typer).typeof(ctx)));
		assertTrue(typer.tyEqv(tyFact.TyArr(top2top, top2top), id_top2top.accept(typer).typeof(ctx)));
		assertTrue(typer.tyEqv(top, id_app_id.accept(typer).typeof(ctx)));
		assertTrue(typer.tyEqv(top2top, id_top2top_app_id.accept(typer).typeof(ctx)));
	}
}
