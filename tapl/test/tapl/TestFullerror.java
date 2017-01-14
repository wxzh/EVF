package tapl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fullerror.Join;
import fullerror.JoinMeet;
import fullerror.Meet;
import fullerror.Print;
import fullerror.PrintTy;
import fullerror.Subtype;
import fullerror.SubtypeAlg;
import fullerror.TyEqv;
import fullerror.Typeof;
import fullerror.termalg.external.Term;
import fullerror.termalg.external.TermAlgFactory;
import fullerror.termalg.external.TermAlgVisitor;
import fullerror.tyalg.external.Ty;
import fullerror.tyalg.external.TyAlgFactory;
import fullerror.tyalg.external.TyAlgMatcher;
import fullerror.tyalg.external.TyAlgMatcherImpl;
import fullerror.tyalg.external.TyAlgVisitor;
import fullerror.tyalg.shared.GTyAlg;
import typed.GetTypeFromBind;
import typed.PrintBind;
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

public class TestFullerror {
	class PrintTyImpl implements PrintTy<Ty, Bind<Ty>>, TyAlgVisitor<IPrint<Bind<Ty>>> {
	}

	class PrintBindImpl implements PrintBind<Bind<Ty>, Ty>,
			BindingAlgVisitor<IPrint<Bind<Ty>>, Ty> {

		@Override
		public PrintTy<Ty, Bind<Ty>> printTy() {
			return printTy;
		}
	}

	class PrintImpl implements Print<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<IPrint<Bind<Ty>>, Ty> {
		@Override
		public PrintTy<Ty, Bind<Ty>> printTy() {
			return printTy;
		}

		@Override
		public PrintBind<Bind<Ty>, Ty> printBind() {
			return printBind;
		}
	}

	class TyEqvImpl implements TyEqv<Ty>, TyAlgVisitor<ITyEqv<Ty>> {
		@Override
		public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}
	}

	class SubtypeImpl implements Subtype<Ty> {
		@Override
		public TyAlgMatcher<Ty, Boolean> matcher() {
			return new TyAlgMatcherImpl<>();
		}

		@Override
		public TyEqv<Ty> tyEqv() {
			return tyEqv;
		}

		class SubtypeAlgImpl extends SubtypeImpl implements SubtypeAlg<Ty>, TyAlgVisitor<ISubtype<Ty>> {
		}

		@Override
		public SubtypeAlg<Ty> subtype() {
			return new SubtypeAlgImpl();
		}
	}

	class GetTypeFromBindImpl implements GetTypeFromBind<Bind<Ty>, Ty>, BindingAlgVisitor<Ty, Ty> {}

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
			return tyFact;
		}

		class JoinImpl extends JoinMeetImpl implements Join<Ty>, TyAlgVisitor<IJoin<Ty>> {}
		class MeetImpl extends JoinMeetImpl implements Meet<Ty>, TyAlgVisitor<IMeet<Ty>> {}

		@Override
		public Meet<Ty> meet() {
			return new MeetImpl();
		}

		@Override
		public Join<Ty> join() {
			return new JoinImpl();
		}
	}


	class TypeofImpl implements Typeof<Term<Ty>, Ty, Bind<Ty>>, TermAlgVisitor<ITypeof<Ty, Bind<Ty>>, Ty> {
		@Override
		public GBindingAlg<Bind<Ty>, Ty, Bind<Ty>> bindAlg() {
			return bindFact;
		}

		@Override
		public JoinMeet<Ty> joinMeet() {
			return new JoinMeetImpl();
		}

		@Override
		public GTyAlg<Ty, Ty> tyAlg() {
			return tyFact;
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
		public GetTypeFromBind<Bind<Ty>, Ty> getTypeFromBind() {
			return new GetTypeFromBindImpl();
		}

		@Override
		public Subtype<Ty> subtype() {
			return subtype;
		}
	}

	// printers
	PrintTyImpl printTy = new PrintTyImpl();
	PrintImpl printTerm = new PrintImpl();
	PrintBindImpl printBind = new PrintBindImpl();

	// elements
	Ty ty;
	Bind<Ty> bind;
	Term<Ty> term;

	// factories
	TyAlgFactory tyFact = new TyAlgFactory();
	BindingAlgFactory<Ty> bindFact = new BindingAlgFactory<>();
	TermAlgFactory<Ty> termFact = new TermAlgFactory<>();

	Context<Bind<Ty>> ctx = new Context<Bind<Ty>>(bindFact);

	Ty bool = tyFact.TyBool();
	Ty top = tyFact.TyTop();
	Ty bot = tyFact.TyBot();
	Ty arr = tyFact.TyArr(bool, top);

	Term<Ty> t = termFact.TmTrue();
	Term<Ty> error = termFact.TmError();
	Term<Ty> tryErrorWithTrue = termFact.TmTry(error, t);

	// typer
	TypeofImpl typeof = new TypeofImpl();
	TyEqvImpl tyEqv = new TyEqvImpl();
	SubtypeImpl subtype = new SubtypeImpl();

	@Test
	public void printTest() {
		assertEquals("error", error.accept(printTerm).print(ctx));
		assertEquals("try error with true", tryErrorWithTrue.accept(printTerm).print(ctx));
	}

	@Test
	public void printTyTest() {
		assertEquals("Top", top.accept(printTy).print(ctx));
		assertEquals("Bot", bot.accept(printTy).print(ctx));
		assertEquals("(Bool -> Top)", arr.accept(printTy).print(ctx));
	}

	@Test
	public void subtypeTest() {
		// S-REFL
		assertTrue(subtype.subtype(bot, bot));
		assertTrue(subtype.subtype(top, top));
		assertTrue(subtype.subtype(arr, arr));

		// S-BOT
		assertTrue(subtype.subtype(bot, bool));
		assertTrue(subtype.subtype(bot, arr));
		assertTrue(subtype.subtype(bot, top));

		// S-TOP
		assertTrue(subtype.subtype(bot, top));
		assertTrue(subtype.subtype(bool, top));
		assertTrue(subtype.subtype(arr, top));

		// S-ARROW
		// T1 <: S1  S2 <: T2
		// ------------------
		//  S1->S2 <: T1->T2
		assertFalse(subtype.subtype(tyFact.TyArr(top, top), tyFact.TyArr(top, bool)));
		assertTrue(subtype.subtype(tyFact.TyArr(top, top), tyFact.TyArr(bool, top)));
		assertFalse(subtype.subtype(tyFact.TyArr(bot, bot), tyFact.TyArr(bool, bot)));
		assertTrue(subtype.subtype(tyFact.TyArr(bot, bot), tyFact.TyArr(bot, bool)));
		assertTrue(subtype.subtype(tyFact.TyArr(top, bot), tyFact.TyArr(bool, bool)));
	}

	@Test
	public void joinTest(){

	}

	@Test
	public void typeofTest() {
		assertTrue(bot.accept(tyEqv).tyEqv(error.accept(typeof).typeof(ctx)));
//		assertTrue(tyEqv"error", error.accept(printTerm).apply(ctx));
//		assertEquals("try error with true", tryErrorWithTrue.accept(printTerm).apply(ctx));
	}
}