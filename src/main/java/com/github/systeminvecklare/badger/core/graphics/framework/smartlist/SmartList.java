package com.github.systeminvecklare.badger.core.graphics.framework.smartlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SmartList<T> implements ISmartList<T> {
	private List<ObjectHolder> objects = new ArrayList<ObjectHolder>();
	private int loopDepth = 0;

	@Override
	public void addToBirthList(T object) {
		objects.add(new ObjectHolder(object));
	}

	@Override
	public void addToDeathList(T object) {
		for(ObjectHolder objectHolder : objects) {
			if(objectHolder.removed) {
				continue;
			}
			if(object.equals(objectHolder.obj)) {
				objectHolder.remove();
				return;
			}
		}
	}
	
	public void forEach(ILoopAction<? super T> sink) {
		loopDepth++;
		int size = objects.size();
		for(int i = 0; i < size; ++i) {
			ObjectHolder holder = objects.get(i);
			if(!holder.removed) {
				if(!sink.onIteration(holder.obj)) {
					break;
				}
			}
		}
		loopDepth--;
		if(loopDepth <= 0) {
			Iterator<ObjectHolder> iterator = objects.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().removed) {
					iterator.remove();
				}
			}
		}
	}

	@Override
	public void clear() {
		if(loopDepth <= 0) {
			objects.clear();
		} else {
			for(ObjectHolder objectHolder : objects) {
				objectHolder.remove();
			}
		}
	}

	@Override
	public boolean isEmpty() {
		if(loopDepth <= 0) {
			return objects.isEmpty();
		} else {
			for(ObjectHolder objectHolder : objects) {
				if(!objectHolder.removed) {
					return false;
				}
			}
			return true;
		}
	}
	
	private class ObjectHolder {
		private final T obj;
		private boolean removed = false;
		public ObjectHolder(T obj) {
			this.obj = obj;
		}
		
		public void remove() {
			removed = true;
		}
	}
}
