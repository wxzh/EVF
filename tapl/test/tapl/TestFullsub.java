package tapl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import fullsimple.GetTypeFromBind;
import fullsimple.PrintBind;
import fullsimple.bindingalg.external.Bind;
import fullsimple.bindingalg.external.BindingAlgFactory;
import fullsimple.bindingalg.external.BindingAlgVisitor;
import fullsimple.bindingalg.shared.GBindingAlg;
import fullsub.Eval1;
import fullsub.IsNumericVal;
import fullsub.IsVal;
import fullsub.JoinMeet;
import fullsub.Print;
import fullsub.PrintTy;
import fullsub.Subtype;
import fullsub.TmMap;
import fullsub.TyEqv;
import fullsub.Typeof;
import fullsub.termalg.external.Term;
import fullsub.termalg.external.TermAlgFactory;
import fullsub.termalg.external.TermAlgMatcher;
import fullsub.termalg.external.TermAlgMatcherImpl;
import fullsub.termalg.external.TermAlgVisitor;
import fullsub.termalg.shared.GTermAlg;
import fullsub.tyalg.external.Ty;
import fullsub.tyalg.external.TyAlgFactory;
import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.external.TyAlgMatcherImpl;
import fullsub.tyalg.external.TyAlgVisitor;
import fullsub.tyalg.shared.GTyAlg;
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
	class JoinMeetImpl implements JoinMeet<Ty> {
	  public boolean subtype(Ty ty1, Ty ty2) {
	    return typer.subtype(ty1, ty2);
	  }

		@Override public TyAlgMatcher<Ty, Ty> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override public GTyAlg<Ty, Ty> alg() {
			return new TyAlgFactory();
		}

		class JoinImpl extends JoinMeetImpl implements Join<Ty>, TyAlgVisitor<IJoin<Ty>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<Ty>, TyAlgVisitor<IMeet<Ty>> {}

		@Override public Ty joinImpl(Ty ty1, Ty ty2) {
		  return new JoinImpl().visitTy(ty1).join(ty2);
		}

		@Override public Ty meetImpl(Ty ty1, Ty ty2) {
		  return new MeetImpl().visitTy(ty1).meet(ty2);
		}
	}

	class SubtypeImpl implements Subtype<Ty>, TyAlgVisitor<ISubtype<Ty>> {
		@Override public boolean tyEqv(Ty ty1, Ty ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class PrintAll implements Print<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, PrintTy<Ty, Bind<Term<Ty>,Ty>>, PrintBind<Bind<Term<Ty>,Ty>, Term<Ty>, Ty>,
	    TermAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>, Ty>, TyAlgVisitor<IPrint<Bind<Term<Ty>, Ty>>>, BindingAlgVisitor<IPrint<Bind<Term<Ty>,Ty>>, Term<Ty>, Ty> {
		public TermAlgMatcher<Term<Ty>, Ty, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}
		public String printTy(Ty ty, Context<Bind<Term<Ty>,Ty>> ctx) {
		  return visitTy(ty).print(ctx);
		}
		public String printBind(Bind<Term<Ty>,Ty> bind, Context<Bind<Term<Ty>, Ty>> ctx) {
		  return visitBind(bind).print(ctx);
		}
		public String printTerm(Term<Ty> t, Context<Bind<Term<Ty>, Ty>> ctx) {
		  return visitTerm(t).print(ctx);
		}
	}

	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<ITypeof<Ty, Bind<Term<Ty>,Ty>>, Ty>{
	  public Ty join(Ty ty1, Ty ty2) {
	    return joinMeet.join(ty1, ty2);
	  }

		public GBindingAlg<Bind<Term<Ty>,Ty>, Term<Ty>, Ty, Bind<Term<Ty>,Ty>> bindAlg() {
			return bindFact;
		}

		class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Term<Ty>,Ty>, Term<Ty>, Ty>, BindingAlgVisitor<Ty, Term<Ty>,Ty> {}

		public Ty getTypeFromBind(Bind<Term<Ty>,Ty> bind) {
		  return new GetTypeFromBindImpl().visitBind(bind);
		}

		public boolean tyEqv(Ty ty1, Ty ty2) {
			return new TyEqvImpl().visitTy(ty1).tyEqv(ty2);
		}

		public TyAlgMatcher<Ty, Ty> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		public GTyAlg<Ty, Ty> tyAlg() {
			return tyFact;
		}

		@Override public boolean subtype(Ty ty1, Ty ty2) {
		  return new SubtypeImpl().subtype(ty1, ty2);
		}
	}

	class Eval1Impl implements Eval1<Term<Ty>, Ty, Bind<Term<Ty>,Ty>>, TermAlgVisitor<Term<Ty>, Ty> {
	  public boolean isNumericVal(Term<Ty> t) {
	    return new IsNumericValImpl().visitTerm(t);
	  }
	  public TermAlgMatcher<Term<Ty>, Ty, Term<Ty>> matcher() {
	    return new TermAlgMatcherImpl<>();
	  }
	  public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	    return tmFact;
	  }
	  public Term<Ty> termSubstTop(Term<Ty> s, Term<Ty> t) {
	    return new TmMapImpl().termSubstTop(s, t);
	  }
	  public boolean isVal(Term<Ty> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	}
	class EvalImpl implements Eval<Term<Ty>> {
	  public Term<Ty> eval1(Term<Ty> t) {
	    return new Eval1Impl().visitTerm(t);
	  }
	  public boolean isVal(Term<Ty> t) {
	    return new IsValImpl().visitTerm(t);
	  }
	}
	class IsNumericValImpl implements IsNumericVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	class TmMapImpl implements TmMap<Term<Ty>, Ty>, TermAlgVisitor<Function<TmMapCtx<Term<Ty>>,Term<Ty>>, Ty> {
	  public GTermAlg<Term<Ty>, Ty, Term<Ty>> alg() {
	    return tmFact;
	  }
	}

	TermAlgFactory<Ty> tmFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();

	PrintAll print = new PrintAll();
	JoinMeetImpl joinMeet = new JoinMeetImpl();
	TypeofImpl typer = new TypeofImpl();

	BindingAlgFactory<Term<Ty>,Ty> bindFact = new BindingAlgFactory<>();
	Context<Bind<Term<Ty>,Ty>> ctx = new Context<Bind<Term<Ty>,Ty>>(bindFact);

	Ty top = tyFact.TyTop();
	Ty top2top = tyFact.TyArr(top, top);
	Ty bool = tyFact.TyBool();
	Ty ty_rcd = tyFact.TyRecord(asList(new Tuple2<>("x", top2top)));
	Ty rcd1 = tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", bool)));
	Ty rcd2 = tyFact.TyRecord(asList(new Tuple2<>("y", bool), new Tuple2<>("x", top)));

	Term<Ty> var = tmFact.TmVar(0, 1);
	Term<Ty> id = tmFact.TmAbs("x", top, var);
	Term<Ty> id_top2top = tmFact.TmAbs("x", top2top, var);
	Term<Ty> id_app_id = tmFact.TmApp(id, id);
	Term<Ty> id_top2top_app_id = tmFact.TmApp(id_top2top, id);
	Term<Ty> id_rcd = tmFact.TmAbs("r", ty_rcd, tmFact.TmApp(tmFact.TmProj(var, "x"), tmFact.TmProj(var, "x")));
	Term<Ty> rcd = tmFact.TmRecord(asList(new Tuple2<>("x", id), new Tuple2<>("y", id)));
	Term<Ty> id_rcd_app_rcd = tmFact.TmApp(id_rcd, rcd);

	Term<Ty> t = tmFact.TmTrue();
	Term<Ty> f = tmFact.TmFalse();
	// if true {x: true, y: false, a: false} else ...
	Term<Ty> if_rcd = tmFact.TmIf(t,
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

		Ty rcd = tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("z", tyFact.TyBool())));
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
