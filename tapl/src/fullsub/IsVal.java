package fullsub;

import fullsub.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, moreextension.IsVal<Term, Ty>, typed.IsVal<Term, Ty> {
}
