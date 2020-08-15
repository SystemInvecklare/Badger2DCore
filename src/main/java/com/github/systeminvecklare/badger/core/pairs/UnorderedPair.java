package com.github.systeminvecklare.badger.core.pairs;


public class UnorderedPair<A,B> implements IPair<A,B> {
	private A a;
	private B b;
	
	public UnorderedPair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public A getFirst() {
		return a;
	}

	@Override
	public B getSecond() {
		return b;
	}
	
	@Override
	public int hashCode() {
		return (a == null ? 0 : a.hashCode()) ^ (b == null ? 0 : b.hashCode());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof UnorderedPair) && equalsOwn((UnorderedPair<A, B>) obj);
	}

	private boolean equalsOwn(UnorderedPair<A, B> obj) {
		return (this.a.equals(obj.a) && this.b.equals(obj.b)) || (this.b.equals(obj.a) && this.a.equals(obj.b));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(String.valueOf(a));
		builder.append(",");
		builder.append(String.valueOf(b));
		builder.append(")");
		return builder.toString();
	}
	
	public static <A,B> UnorderedPair<A, B> of(A a,B b)
	{
		return new UnorderedPair<A, B>(a, b);
	}
}
