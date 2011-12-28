package rocky.sizecounter;

public class SizeCounterFactory {
	
	public static ISizeCounter getDefaultInstance() {
		return new SizeCounterImpl();
	}
}
