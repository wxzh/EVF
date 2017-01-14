package utils;

public interface ITypeof<Ty, Bind> {
	Ty typeof(Context<Bind> ctx);
}
