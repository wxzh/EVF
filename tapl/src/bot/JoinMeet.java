package bot;

import bot.tyalg.external.TyAlgMatcher;
import bot.tyalg.shared.GTyAlg;

public interface JoinMeet<Ty> extends top.JoinMeet<Ty>{
	@Override Subtype<Ty> subtype();
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override GTyAlg<Ty, Ty> alg();
	@Override Join<Ty> join();
	@Override Meet<Ty> meet();
}