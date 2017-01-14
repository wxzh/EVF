package fullref;

import fullref.tyalg.external.TyAlgMatcher;
import fullref.tyalg.shared.GTyAlg;

public interface JoinMeet<Ty> extends bot.JoinMeet<Ty>, fullsub.JoinMeet<Ty> {
	@Override Subtype<Ty> subtype();
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override GTyAlg<Ty, Ty> alg();
	@Override Join<Ty> join();
	@Override Meet<Ty> meet();
}
