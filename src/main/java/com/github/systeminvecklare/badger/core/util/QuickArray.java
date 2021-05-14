package com.github.systeminvecklare.badger.core.util;


public class QuickArray<T> implements IQuickArray<T> {
//	private static final int INCREASE = 10;
	private static final int INCREASE = 2; //Seems like most only get's it's increase called one or twice with this value...
	@SuppressWarnings("unchecked")
	private T[] array = (T[]) new Object[0];
	private int size = 0;
	
	@Override
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
	
//	public void add(T[] objects)
//	{
//		makeRoomFor(objects.length);
//		System.arraycopy(objects, 0, array, size, objects.length);
//		size+=objects.length;
//	}
	
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
	
	@Override
	public void copyToArray(T[] target, int offset, int length) {
		System.arraycopy(this.array, 0, target, offset, length);
	}
	
	@Override
	public void addAllFrom(IQuickArray<T> other) {
		int otherSize = other.getSize();
		if(otherSize > 0 ) {
			makeRoomFor(otherSize);
			other.copyToArray(array, size, otherSize);
			size+= otherSize;
		}
	}
	
	@Override
	public void removeAllIn(IQuickArray<T> other) {
		final int otherSize = other.getSize();
		if(otherSize > 0) {
			final int mySize = size;
			int newSize = mySize;
			for(int i = 0; i < mySize; ++i) {
				jLoop: for(int j = 0; j < otherSize; ++j) {
					if(equal(this.array[i], other.get(j))) {
						this.array[i] = null;
						--newSize;
						break jLoop;
					}
				}
			}
			if(newSize != mySize) {
				for(int i = 0; i < newSize; ++i) {
					if(this.array[i] == null) {
						jLoop: for(int j = i+1; j < mySize; ++j) {
							if(this.array[j] != null) {
								this.array[i] = this.array[j];
								this.array[j] = null;
								break jLoop;
							}
						}
					}
				}
				this.size = newSize;
			}
		}
	}
	
	protected boolean equal(T t, T t2) {
		return t == t2;
	}

	@Override
	public int getSize()
	{
		return size;
	}
	
	@Override
	public T get(int index) {
		if(index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}
	
	@Override
	public void clear() {
		for(int i = 0; i < size; ++i) {
			array[i] = null;
		}
		size = 0;
	}
}
