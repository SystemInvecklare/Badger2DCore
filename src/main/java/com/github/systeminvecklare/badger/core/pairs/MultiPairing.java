package com.github.systeminvecklare.badger.core.pairs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiPairing<A,B> implements IMultiPairing<A, B> {
	private List<OrderedPair<A, B>> allPairs = new ArrayList<OrderedPair<A,B>>();
	private Map<A, Collection<B>> akeys = new HashMap<A, Collection<B>>();
	private Map<B, Collection<A>> bkeys = new HashMap<B, Collection<A>>();
	
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
		allPairs.add(pair);
		getOrCreateCollection(akeys,pair.getFirst()).add(pair.getSecond());
		getOrCreateCollection(bkeys,pair.getSecond()).add(pair.getFirst());
		return true;
	}

	private static <KeyType,ItemType> Collection<ItemType> getOrCreateCollection(Map<KeyType, Collection<ItemType>> collMap, KeyType key) {
		Collection<ItemType> values = collMap.get(key);
		if(values == null)
		{
			values = new ArrayList<ItemType>();
			collMap.put(key, values);
		}
		return values;
	}

	@Override
	public boolean remove(OrderedPair<A, B> pair) {
		if(allPairs.contains(pair))
		{
			allPairs.remove(pair);
			akeys.get(pair.getFirst()).remove(pair.getSecond());
			bkeys.get(pair.getSecond()).remove(pair.getFirst());
			return true;
		}
		return false;
	}

	@Override
	public Collection<B> removeAllWithFirst(A key) {
		Iterator<OrderedPair<A, B>> iterator = allPairs.iterator();
		while(iterator.hasNext())
		{
			OrderedPair<A, B> pair = iterator.next();
			if(equals(pair.getFirst(),key))
			{
				iterator.remove();
				bkeys.get(pair.getSecond()).remove(key);
			}
		}
		Collection<B> removed = new ArrayList<B>(akeys.get(key));
		akeys.get(key).clear();
		return removed;
	}
	
	@Override
	public void clear() {
		allPairs.clear();
		akeys.clear();
		bkeys.clear();
	}

	private static boolean equals(Object one, Object two) {
		return one == null ? (two == null) : one.equals(two);
	}

	@Override
	public Collection<A> removeAllWithSecond(B key) {
		Iterator<OrderedPair<A, B>> iterator = allPairs.iterator();
		while(iterator.hasNext())
		{
			OrderedPair<A, B> pair = iterator.next();
			if(equals(pair.getSecond(),key))
			{
				iterator.remove();
				akeys.get(pair.getFirst()).remove(key);
			}
		}
		Collection<A> removed = new ArrayList<A>(bkeys.get(key));
		bkeys.get(key).clear();
		return removed;
	}

	@Override
	public Collection<B> getByFirst(A key) {
		Collection<B> byFirst = akeys.get(key);
		return Collections.unmodifiableCollection(byFirst != null ? byFirst : Collections.<B>emptyList());
	}

	@Override
	public Collection<A> getBySecond(B key) {
		Collection<A> bySecond = bkeys.get(key);
		return Collections.unmodifiableCollection(bySecond != null ? bySecond : Collections.<A>emptyList());
	}

	@Override
	public boolean hasFirstKey(A key) {
		return akeys.containsKey(key);
	}

	@Override
	public boolean hasSecondKey(B key) {
		return bkeys.containsKey(key);
	}

	@Override
	public Set<A> firstKeySet() {
		return Collections.unmodifiableSet(akeys.keySet());
	}

	@Override
	public Set<B> secondKeySet() {
		return Collections.unmodifiableSet(bkeys.keySet());
	}
}
