package fullsub;

import fullsub.tyalg.external.TyAlgMatcher;
import fullsub.tyalg.shared.GTyAlg;

public interface JoinMeet<Ty> extends top.JoinMeet<Ty>, record.JoinMeet<Ty> {
	@Override Subtype<Ty> subtype();
	@Override TyAlgMatcher<Ty, Ty> matcher();
	@Override GTyAlg<Ty, Ty> alg();
	@Override Join<Ty> join();
	@Override Meet<Ty> meet();

	@Override default Ty meet(Ty ty1, Ty ty2) {
		return record.JoinMeet.super.meet(ty1, ty2);
	}

	@Override default Ty join(Ty ty1, Ty ty2) {
		return record.JoinMeet.super.join(ty1, ty2);
	}
}
