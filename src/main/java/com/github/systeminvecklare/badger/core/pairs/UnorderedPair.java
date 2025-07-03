package com.github.systeminvecklare.badger.core.pairs;

public class UnorderedPair<A> implements IPair<A,A> {
	private A a;
	private A b;
	
	public UnorderedPair(A a, A b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public A getFirst() {
		return a;
	}

	@Override
	public A getSecond() {
		return b;
	}
	
	public boolean equalsAny(A object) {
		return (a == null ? object == null : a.equals(object)) || (b == null ? object == null : b.equals(object));
	}
	
	public A getOther(A object) {
		if(a == null ? object == null : a.equals(object)) {
			return b;
		} else if(b == null ? object == null : b.equals(object)) {
			return a;
		}
		throw new IllegalArgumentException(object+" is not part of the pair.");
	}
	
	@Override
	public int hashCode() {
		return (a == null ? 0 : a.hashCode()) ^ (b == null ? 0 : b.hashCode());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof UnorderedPair) && equalsOwn((UnorderedPair<A>) obj);
	}

	private boolean equalsOwn(UnorderedPair<A> obj) {
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
	
	public static <A> UnorderedPair<A> of(A a,A b)
	{
		return new UnorderedPair<A>(a, b);
	}
}
