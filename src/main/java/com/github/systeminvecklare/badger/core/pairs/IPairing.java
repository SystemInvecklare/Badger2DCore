package com.github.systeminvecklare.badger.core.pairs;

import java.util.Collection;
import java.util.Set;

public interface IPairing<A,B> extends Iterable<OrderedPair<A, B>> {
	public Collection<OrderedPair<A, B>> getAllPairs();
	public boolean add(OrderedPair<A, B> pair);
	public boolean add(A first, B second);
	public boolean remove(OrderedPair<A, B> pair);
	public boolean remove(A first, B second);
	public void clear();
	public B getByFirst(A key);
	public A getBySecond(B key);
	public boolean hasFirstKey(A key);
	public boolean hasSecondKey(B key);
	public Set<A> firstKeySet();
	public Set<B> secondKeySet();
}
