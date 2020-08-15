package com.github.systeminvecklare.badger.core.pairs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Pairing<A,B> implements IPairing<A, B> {
	private List<OrderedPair<A, B>> allPairs = new ArrayList<OrderedPair<A,B>>();
	private Map<A, OrderedPair<A, B>> aToB = new HashMap<A, OrderedPair<A, B>>();
	private Map<B, OrderedPair<A, B>> bToA = new HashMap<B, OrderedPair<A, B>>();

	@Override
	public Iterator<OrderedPair<A, B>> iterator() {
		return allPairs.iterator();
	}

	@Override
	public Collection<OrderedPair<A, B>> getAllPairs() {
		return new ArrayList<OrderedPair<A,B>>(allPairs);
	}

	@Override
	public boolean add(OrderedPair<A, B> pair) {
		if(allPairs.contains(pair))
		{
			return false;
		}
		OrderedPair<A, B> existingWithA = aToB.get(pair.getFirst());
		if(existingWithA != null)
		{
			remove(existingWithA);
		}
		OrderedPair<A, B> existingWithB = bToA.get(pair.getSecond());
		if(existingWithB != null)
		{
			remove(existingWithB);
		}
		allPairs.add(pair);
		aToB.put(pair.getFirst(), pair);
		bToA.put(pair.getSecond(), pair);
		return true;
	}

	@Override
	public boolean remove(OrderedPair<A, B> pair) {
		if(allPairs.contains(pair))
		{
			allPairs.remove(pair);
			aToB.remove(pair.getFirst());
			bToA.remove(pair.getSecond());
			
			return true;
		}
		return false;
	}
	
	@Override
	public boolean add(A first, B second) {
		return add(OrderedPair.of(first, second));
	}
	
	@Override
	public boolean remove(A first, B second) {
		return remove(OrderedPair.of(first, second));
	}
	
	@Override
	public void clear() {
		allPairs.clear();
		aToB.clear();
		bToA.clear();
	}

	@Override
	public B getByFirst(A key) {
		OrderedPair<A, B> pair = aToB.get(key);
		return pair == null ? null : pair.getSecond();
	}

	@Override
	public A getBySecond(B key) {
		OrderedPair<A, B> pair = bToA.get(key);
		return pair == null ? null : pair.getFirst();
	}

	@Override
	public boolean hasFirstKey(A key) {
		return aToB.containsKey(key);
	}

	@Override
	public boolean hasSecondKey(B key) {
		return bToA.containsKey(key);
	}
	
	@Override
	public Set<A> firstKeySet() {
		return Collections.unmodifiableSet(aToB.keySet());
	}
	
	@Override
	public Set<B> secondKeySet() {
		return Collections.unmodifiableSet(bToA.keySet());
	}
	
	@Override
	public int hashCode() {
		return allPairs.hashCode();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pairing)
		{
			Pairing objPairing = (Pairing) obj;
			return this.allPairs.equals(objPairing.allPairs);
		}
		else if(obj instanceof IPairing)
		{
			return this.allPairs.equals(((IPairing) obj).getAllPairs());
		}
		return false;
	}
}
