package multimidia;

public final class Tuple<T1, T2, T3, T4> {
	public final T1 first;
	public final T2 second;
	public final T3 third;
	public final T4 fourth;

	public Tuple(T1 first, T2 second, T3 third, T4 fourth) {
		this.first = first;

		this.second = second;

		this.third = third;
		
		this.fourth = fourth;
	}
}