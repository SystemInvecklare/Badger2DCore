package com.github.systeminvecklare.badger.core.pooling;

public abstract class SimplePool<T> implements IPool<T> {
	private Object[] poolArray;
	private int poolArrayLength = 0;
	private int maxSize;
	
	public SimplePool(int initCapacity, int maxSize) {
		this.poolArray = new Object[initCapacity];
		this.maxSize = maxSize;
	}

	@Override
	public T obtain() {
		if(poolArrayLength > 0)
		{
			try
			{
				poolArrayLength--;
				@SuppressWarnings("unchecked")
				T obj = (T) poolArray[poolArrayLength];
				poolArray[poolArrayLength] = null;
				if(obj == null) {
					obj = newObject(); // This should not happen, but for safety... Let's make sure thins never return a null object
				}
				return obj;
			}
			catch(IndexOutOfBoundsException e)
			{
				return newObject();
//				T object = newObject();
//				System.out.println("New "+object.getClass().getSimpleName()+" created");
//				return object;
			}
		}
		else
		{
			return newObject();
//			T object = newObject();
//			System.out.println("New "+object.getClass().getSimpleName()+" created");
//			return object;
		}
	}

	@Override
	public void free(T poolable) {
		if(poolable != null) {
			if(poolArrayLength < maxSize) {
				if(poolArray.length <= poolArrayLength) {
					Object[] newArray = new Object[poolArray.length + 1];
					System.arraycopy(poolArray, 0, newArray, 0, poolArray.length);
					poolArray = newArray;
				}
				poolArray[poolArrayLength] = poolable;
				poolArrayLength++;
			}
		}
	}

	public abstract T newObject();
}
