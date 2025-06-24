package com.github.systeminvecklare.badger.core.pooling;

public abstract class SimplePool<T> implements IPool<T> {
	private final Thread owningThread;
	private Object[] poolArray;
	private int poolArrayLength = 0;
	private int maxSize;
	
	public SimplePool(int initCapacity, int maxSize) {
		this.owningThread = Thread.currentThread();
		this.poolArray = new Object[initCapacity];
		this.maxSize = maxSize;
	}

	@Override
	public T obtain() {
		validateThread();
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
		validateThread();
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
	
	private void validateThread() {
		if(owningThread != Thread.currentThread()) {
			throw new RuntimeException("Thread "+Thread.currentThread()+" tried to access pool owned by "+owningThread);
		}
	}

	public abstract T newObject();
}
