package tapl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fullsub.IsNumericVal;
import fullsub.IsVal;
import fullsub.Join;
import fullsub.JoinMeet;
import fullsub.Meet;
import fullsub.Print;
import fullsub.PrintBind;
import fullsub.PrintTy;
import fullsub.Subtype;
import fullsub.SubtypeAlg;
import fullsub.TyEqv;
import fullsub.Typeof;
import fullsub.termalg.external.Term;
import fullsub.termalg.external.TermAlgFactory;
import fullsub.termalg.external.TermAlgMatcher;
import fullsub.termalg.external.TermAlgMatcherImpl;
import fullsub.termalg.external.TermAlgVisitor;
import fullsub.tyalg.external.Ty;
import fullsub.tyalg.external.TyAlgFactory;
import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.external.TyAlgMatcherImpl;
import fullsub.tyalg.external.TyAlgVisitor;
import fullsub.tyalg.shared.GTyAlg;
import library.Tuple2;
import typed.GetTypeFromBind;
import typed.bindingalg.external.Bind;
import typed.bindingalg.external.BindingAlgFactory;
import typed.bindingalg.external.BindingAlgVisitor;
import typed.bindingalg.shared.GBindingAlg;
import utils.Context;
import utils.IJoin;
import utils.IMeet;
import utils.IPrint;
import utils.ISubtype;
import utils.ITyEqv;
import utils.ITypeof;

public class TestFullsub {

	class IsNumericValImpl implements IsNumericVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> { }
	class IsValImpl implements IsVal<Term<Ty>, Ty>, TermAlgVisitor<Boolean, Ty> {}
	class JoinMeetImpl implements JoinMeet<Ty> {

		@Override
		public Subtype<Ty> subtype() {
			return subtype;
		}

		@Override
		public TyAlgMatcher<Ty, Ty> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override
		public GTyAlg<Ty, Ty> alg() {
			return new TyAlgFactory();
		}

		class JoinImpl extends JoinMeetImpl implements Join<Ty>, TyAlgVisitor<IJoin<Ty>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<Ty>, TyAlgVisitor<IMeet<Ty>> {}

		@Override
		public Join<Ty> join() {
			return new JoinImpl();
		}

		@Override
		public Meet<Ty> meet() {
			return new MeetImpl();
		}
	}

	class SubtypeImpl implements Subtype<Ty> {
		@Override
		public TyEqv<Ty> tyEqv() {
			return new TyEqvImpl();
		}

		@Override
		public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		class SubtypeAlgImpl extends SubtypeImpl implements SubtypeAlg<Ty>, TyAlgVisitor<ISubtype<Ty>> { }

		@Override
		public SubtypeAlg<Ty> subtype() {
			return new SubtypeAlgImpl();
		}
	}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override
		public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class PrintTyImpl implements PrintTy<Ty, Bind<Ty>>, TyAlgVisitor<IPrint<Bind<Ty>>> {
	}

	class PrintImpl implements Print<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<IPrint<Bind<Ty>>, Ty> {
		@Override
		public PrintBind<Bind<Ty>, Ty> printBind() {
			return null;
		}

		@Override
		public TermAlgMatcher<Term<Ty>, Ty, String> matcher() {
			return new TermAlgMatcherImpl<>();
		}

		@Override
		public PrintTy<Ty, Bind<Ty>> printTy() {
			return printTy;
		}
	}

	class PrintBindImpl implements PrintBind<Bind<Ty>, Ty>, BindingAlgVisitor<IPrint<Bind<Ty>>, Ty> {
		@Override
		public PrintTy<Ty, Bind<Ty>> printTy() {
			return printTy;
		}
	}

	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<ITypeof<Ty, Bind<Ty>>, Ty>{

		@Override
		public GBindingAlg<Bind<Ty>, Ty, Bind<Ty>> bindAlg() {
			return bindFact;
		}

		class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Ty>, Ty>, BindingAlgVisitor<Ty, Ty> {}

		@Override
		public GetTypeFromBind<Bind<Ty>, Ty> getTypeFromBind() {
			return new GetTypeFromBindImpl();
		}

		@Override
		public TyEqv<Ty> tyEqv() {
			return tyEqv;
		}

		@Override
		public TyAlgMatcher<Ty, Ty> tyMatcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override
		public GTyAlg<Ty, Ty> tyAlg() {
			return tyFact;
		}

		@Override
		public Subtype<Ty> subtype() {
			return subtype;
		}

		@Override
		public JoinMeet<Ty> joinMeet() {
			return joinMeet;
		}
	}

	TermAlgFactory<Ty> termFact = new TermAlgFactory<>();
	TyAlgFactory tyFact = new TyAlgFactory();

	SubtypeImpl subtype = new SubtypeImpl();
	TyEqvImpl tyEqv = new TyEqvImpl();
	PrintTyImpl printTy = new PrintTyImpl();
	PrintImpl printTerm = new PrintImpl();
	JoinMeetImpl joinMeet = new JoinMeetImpl();
	TypeofImpl typeof = new TypeofImpl();

	BindingAlgFactory<Ty> bindFact = new BindingAlgFactory<>();
	Context<Bind<Ty>> ctx = new Context<Bind<Ty>>(bindFact);

	Ty top = tyFact.TyTop();
	Ty top2top = tyFact.TyArr(top, top);
	Ty ty_rcd = tyFact.TyRecord(asList(new Tuple2<>("x", top2top)));
	Ty rcd1 = tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", tyFact.TyBool())));
	Ty rcd2 = tyFact.TyRecord(asList(new Tuple2<>("y", tyFact.TyBool()), new Tuple2<>("x", top)));

	Term<Ty> var = termFact.TmVar(0, 1);
	Term<Ty> id = termFact.TmAbs("x", top, var);
	Term<Ty> id_top2top = termFact.TmAbs("x", top2top, var);
	Term<Ty> id_app_id = termFact.TmApp(id, id);
	Term<Ty> id_top2top_app_id = termFact.TmApp(id_top2top, id);
	Term<Ty> id_rcd = termFact.TmAbs("r", ty_rcd, termFact.TmApp(termFact.TmProj(var, "x"), termFact.TmProj(var, "x")));
	Term<Ty> id_rcd_app_rcd = termFact.TmApp(id_rcd, termFact.TmRecord(asList(new Tuple2<>("x", id), new Tuple2<>("y", id))));

	Term<Ty> t = termFact.TmTrue();
	Term<Ty> f = termFact.TmFalse();
	Term<Ty> if_rcd = termFact.TmIf(t,
			termFact.TmRecord(asList(new Tuple2<>("x", t), new Tuple2<>("y", f), new Tuple2<>("a", f))),
			termFact.TmRecord(asList(new Tuple2<>("y", f), new Tuple2<>("x", termFact.TmRecord(asList())), new Tuple2<>("b", f))));

	@Test
	public void printTest() {
		assertEquals("lambda x:Top. x", id.accept(printTerm).print(ctx));
		assertEquals("lambda x:(Top -> Top). x", id_top2top.accept(printTerm).print(ctx));
		assertEquals("lambda r:{x:(Top -> Top)}. r.x r.x {x=lambda x:Top. x,y=lambda x:Top. x}", id_rcd_app_rcd.accept(printTerm).print(ctx));
		assertEquals("if true then {x=true,y=false,a=false} else {y=false,x={},b=false}", if_rcd.accept(printTerm).print(ctx));
	}


	@Test
	public void tyEqvRecordTest() {
		assertTrue(rcd1.accept(tyEqv).tyEqv(rcd2));
		assertTrue(rcd2.accept(tyEqv).tyEqv(rcd1));
	}

	@Test
	public void subtypeRecordTest() {
		assertTrue(subtype.subtype(rcd1, rcd2));
		assertTrue(subtype.subtype(rcd2, rcd1));
	}

	@Test
	public void joinRecordTest() {
		assertTrue(joinMeet.join(rcd1, ty_rcd).accept(tyEqv).tyEqv(tyFact.TyRecord(asList(new Tuple2<>("x", top)))));
	}

	@Test
	public void meetTest() {
		assertTrue(tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", tyFact.TyBool()))).accept(tyEqv).tyEqv(if_rcd.accept(typeof).typeof(ctx)));
	}
	@Test
	public void typeofTest() {
		assertTrue(top2top.accept(tyEqv).tyEqv(id.accept(typeof).typeof(ctx)));
		assertTrue(tyFact.TyArr(top2top, top2top).accept(tyEqv).tyEqv(id_top2top.accept(typeof).typeof(ctx)));
		assertTrue(top.accept(tyEqv).tyEqv(id_app_id.accept(typeof).typeof(ctx)));
		assertTrue(top2top.accept(tyEqv).tyEqv(id_top2top_app_id.accept(typeof).typeof(ctx)));
		assertTrue(tyFact.TyArr(ty_rcd, top).accept(tyEqv).tyEqv(id_rcd.accept(typeof).typeof(ctx)));
//		assertTrue(tyFact.TyArr(ty_rcd, top).accept(tyEqv).tyEqv(if_rcd.accept(typeof).typeof(ctx)));
		assertTrue(tyFact.TyRecord(asList(new Tuple2<>("x", top), new Tuple2<>("y", tyFact.TyBool()))).accept(tyEqv).tyEqv(if_rcd.accept(typeof).typeof(ctx)));
	}
}
