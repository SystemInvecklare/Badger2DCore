package com.github.systeminvecklare.badger.core.graphics.components.util;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleManager;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ILoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;

public class LifecycleManagerComponent implements ILifecycleManager, ILifecycleOwner {
	private static final ILoopAction<ILifecycleOwner> INIT = new ILoopAction<ILifecycleOwner>() {
		@Override
		public boolean onIteration(ILifecycleOwner value) {
			value.init();
			return true;
		}
	};
	private static final ILoopAction<ILifecycleOwner> DISPOSE = new ILoopAction<ILifecycleOwner>() {
		@Override
		public boolean onIteration(ILifecycleOwner value) {
			value.dispose();
			return true;
		}
	};
	
	private final ISmartList<ILifecycleOwner> managed = FlashyEngine.get().newSmartList();
	private boolean disposed = false; 
	private boolean initialized = false;

	@Override
	public void addManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		if(isDisposed()) {
			lifecycleOwner.dispose();
		} else {
			managed.addToBirthList(lifecycleOwner);
		}
	}

	@Override
	public void removeManagedLifecycle(ILifecycleOwner lifecycleOwner) {
		if(!isDisposed()) {
			managed.addToDeathList(lifecycleOwner);
		}
	}
	
	@Override
	public void init() {
		if(!isDisposed()) {
			managed.forEach(INIT);
			initialized = true;
		}
	}
	
	@Override
	public void dispose() {
		if(!isDisposed()) {
			disposed = true;
			managed.forEach(DISPOSE);
			managed.clear();
		}
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public boolean isDisposed() {
		return disposed;
	}
}
