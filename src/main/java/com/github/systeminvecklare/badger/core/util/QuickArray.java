package com.github.systeminvecklare.badger.core.util;


public class QuickArray<T> {
//	private static final int INCREASE = 10;
	private static final int INCREASE = 2; //Seems like most only get's it's increase called one or twice with this value...
	@SuppressWarnings("unchecked")
	private T[] array = (T[]) new Object[0];
	private int size = 0;
	
	public void add(T object)
	{
		if(array.length <= size)
		{
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new Object[array.length+INCREASE];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
		array[size] = object;
		size++;
	}
	
	public void add(T[] objects)
	{
		makeRoomFor(objects.length);
		System.arraycopy(objects, 0, array, size, objects.length);
		size+=objects.length;
	}
	
	private void makeRoomFor(int more)
	{
		if(array.length <= size+more-1)
		{
			int extra = INCREASE;
			while(array.length+extra <= size+more-1)
			{
				extra+=INCREASE;
			}
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new Object[array.length+extra];
			System.arraycopy(array, 0, newArray, 0, array.length);
			array = newArray;
		}
	}
	
	public void addAllFrom(QuickArray<T> other)
	{
		makeRoomFor(other.size);
		System.arraycopy(other.array, 0, array, size, other.size);
		size+= other.size;
	}
	
	public void removeAllIn(QuickArray<T> other)
	{
		boolean atLeastOneHit = false;
		int it = -1;
		if(other.size > 0)
		{
			while((++it)<size)
			{
				findMatch: for(int otherIt = 0; otherIt < other.size; ++otherIt)
				{
					if(equal(array[it],other.array[otherIt]))
					{
						array[it] = null;
						atLeastOneHit = true;
						break findMatch;
					}
				}
			}
		}
		if(atLeastOneHit)
		{
			@SuppressWarnings("unchecked")
			T[] newArray = (T[]) new Object[array.length]; //TODO reuse arrays here also (keep 2 arrays of same size)
			int newArrayInsert = 0;
			int nulls = 0;
			it = -1;
			int nonNullLength = 0;
			int lastStart = 0;
			while((++it)<=size)
			{
				if(it == size || array[it] == null)
				{
					System.arraycopy(array, lastStart, newArray, newArrayInsert, nonNullLength);
					newArrayInsert+=nonNullLength;
					nonNullLength = 0;
					lastStart = it+1;
					nulls++;
				}
				else
				{
					nonNullLength++;
				}
			}
			array = newArray;
			size -= (nulls-1);//one added from end of list
		}
	}
	
	protected boolean equal(T t, T t2) {
		return t == t2;
	}

	public int getSize()
	{
		return size;
	}
	
	public void clear()
	{
		Object[] nulls = new Object[size];
		System.arraycopy(nulls, 0, array, 0, size);
		size = 0;
	}
	
	public T get(int index)
	{
		if(index >= size)
		{
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}
}
