package record;

import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;

public interface JoinMeet<Ty> extends typed.JoinMeet<Ty>{
	@Override Subtype<Ty> subtype();
	@Override Join<Ty> join();
	@Override Meet<Ty> meet();

	TyAlgMatcher<Ty, Ty> matcher();
	GTyAlg<Ty, Ty> alg();
}