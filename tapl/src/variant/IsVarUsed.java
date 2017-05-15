package variant;

public interface IsVarUsed<Term, Ty> extends TermAlgQueryWithCtx<Integer, Boolean, Term, Ty>, typed.IsVarUsed<Term, Ty> {
}
