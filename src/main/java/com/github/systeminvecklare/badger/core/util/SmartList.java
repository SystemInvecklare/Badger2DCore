package com.github.systeminvecklare.badger.core.util;


public class SmartList<T> implements ISmartList<T> {
	private IQuickArray<QueuedUpdate> updates = null;
	private QueuedUpdate latestUpdate = null;
	private IQuickArray<T> array = null;
	
	@SuppressWarnings("rawtypes")
	private static QuickArray STATIC_NULL_ARRAY = new QuickArray()
	{
		@Override
		public void add(Object object) {
			throw new UnsupportedOperationException();
		}
		@Override
		public void addAllFrom(IQuickArray other) {
			throw new UnsupportedOperationException();
		}
		@Override
		public void clear() {
		}
		
//		@Override
//		public Object get(int index)
//		{
//			return null;
//		}
		@Override
		public void removeAllIn(IQuickArray other) {
			throw new UnsupportedOperationException();
		}
		public int getSize() {
			return 0;	
		}
	};
	
	@Override
	public void addToBirthList(T object)
	{
		addToUpdate(true, object);
	}
	
	protected <U> IQuickArray<U> newQuickArray() {
		return new QuickArray<U>();
	}
	
	private void addToUpdate(boolean birth, T object) {
		if(updates == null)
		{
			updates = this.<QueuedUpdate>newQuickArray();
		}
		if(latestUpdate != null)
		{
			if(latestUpdate.birth != birth)
			{
				latestUpdate = new QueuedUpdate(birth);
				updates.add(latestUpdate);
			}
		}
		else
		{
			latestUpdate = new QueuedUpdate(birth);
			updates.add(latestUpdate);
		}
		latestUpdate.list.add(object);
	}

	@Override
	public void addToDeathList(T object)
	{
		addToUpdate(false, object);
	}
	
	private void update()
	{
		if(updates != null)
		{
			if(array == null)
			{
				array = this.<T>newQuickArray();
			}
			
			int updatesSize = updates.getSize();
			for(int i = 0; i < updatesSize; ++i)
			{
				QueuedUpdate update = updates.get(i);
				if(update.birth)
				{
					array.addAllFrom(update.list);
				}
				else
				{
					array.removeAllIn(update.list);
				}
				update.list.clear();
			}
			updates.clear();
			latestUpdate = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IQuickArray<T> getUpdatedArray() {
		update();
		if(array == null)
		{
			return STATIC_NULL_ARRAY;
		}
		return array;
	}

	@Override
	public void clear() {
		if(updates != null)
		{
			updates.clear();
		}
		if(array != null)
		{
			array.clear();
		}
	}
	
	@Override
	public boolean isEmpty() {
		if(updates != null && updates.getSize() != 0) {
			update();
		}
		return array == null || array.getSize() == 0;
	}
	
	private class QueuedUpdate {
		public IQuickArray<T> list = SmartList.this.<T>newQuickArray();
		public boolean birth;

		public QueuedUpdate(boolean birth) {
			this.birth = birth;
		};
	}
}
