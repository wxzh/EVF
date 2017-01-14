package fullsub;

public interface PrintBind<Bind, Ty> extends typed.PrintBind<Bind, Ty> {
	@Override PrintTy<Ty, Bind> printTy();
}
