package com.github.systeminvecklare.badger.core.pairs;

import java.util.Collection;
import java.util.Set;

public interface IMultiPairing<A,B> extends Iterable<OrderedPair<A, B>> {
	public Collection<OrderedPair<A, B>> getAllPairs();
	public boolean add(OrderedPair<A, B> pair);
	public boolean remove(OrderedPair<A, B> pair);
	public void clear();
	public Collection<B> removeAllWithFirst(A key);
	public Collection<A> removeAllWithSecond(B key);
	public Collection<B> getByFirst(A key);
	public Collection<A> getBySecond(B key);
	public boolean hasFirstKey(A key);
	public boolean hasSecondKey(B key);
	public Set<A> firstKeySet();
	public Set<B> secondKeySet();
}
