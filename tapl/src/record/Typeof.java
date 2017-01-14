package record;

import java.util.List;
import java.util.stream.Collectors;

import library.Tuple2;
import record.termalg.shared.GTermAlg;
import record.tyalg.external.TyAlgMatcher;
import record.tyalg.shared.GTyAlg;
import utils.ITypeof;

public interface Typeof<Term, Ty, Bind> extends GTermAlg<Term, ITypeof<Ty, Bind>>, typed.Typeof<Term, Ty, Bind> {
	@Override GTyAlg<Ty, Ty> tyAlg();
	@Override TyAlgMatcher<Ty, Ty> tyMatcher();

	@Override default ITypeof<Ty, Bind> TmRecord(List<Tuple2<String, Term>> fields) {
		return ctx -> tyAlg().TyRecord(fields.stream().map(pr -> new Tuple2<>(pr._1, visitTerm(pr._2).typeof(ctx)))
				.collect(Collectors.toList()));
	}

	@Override default ITypeof<Ty, Bind> TmProj(Term t, String l) {
		return ctx -> {
			Ty tyT = visitTerm(t).typeof(ctx);
			return tyMatcher()
					.TyRecord(fieldTys -> fieldTys.stream().filter(pr -> pr._1.equals(l)).findFirst().map(pr -> pr._2)
							.orElseGet(() -> m().empty().typeof(ctx)))
					.otherwise(() -> m().empty().typeof(ctx)).visitTy(tyT);
		};
	}
}