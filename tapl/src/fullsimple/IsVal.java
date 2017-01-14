package fullsimple;

import fullsimple.termalg.shared.TermAlgDefault;

public interface IsVal<Term, Ty> extends TermAlgDefault<Term, Ty, Boolean>, moreextension.IsVal<Term, Ty>,
    typed.IsVal<Term, Ty>, variant.IsVal<Term, Ty> {
}
