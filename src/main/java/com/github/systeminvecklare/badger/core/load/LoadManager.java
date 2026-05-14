package com.github.systeminvecklare.badger.core.load;

import java.util.ArrayList;
import java.util.List;

public class LoadManager implements ILoadManager {
	private final List<ILoadTask> tasks = new ArrayList<ILoadTask>();
	
	@Override
	public boolean load() {
		if(!tasks.isEmpty()) {
			if(tasks.get(0).load()) {
				tasks.remove(0);
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void addTask(ILoadTask task) {
		tasks.add(task);
	}
}
