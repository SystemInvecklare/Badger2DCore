package com.github.systeminvecklare.badger.core.graphics.components.core;

public interface ILifecycleManager {
	void addManagedLifecycle(ILifecycleOwner lifecycleOwner);
	void removeManagedLifecycle(ILifecycleOwner lifecycleOwner);
}
