package com.github.systeminvecklare.badger.core.load;

import java.util.ArrayList;
import java.util.List;

public class LoadManager implements ILoadManager {
	private final List<ILoadTask> tasks = new ArrayList<ILoadTask>();
	
	private float completedAndRemovedWork = 0;
	
	@Override
	public boolean load() {
		if(!tasks.isEmpty()) {
			if(tasks.get(0).load()) {
				completedAndRemovedWork += getCompletedWork(tasks.remove(0));
			}
			return false;
		}
		return true;
	}
	
	@Override
	public void addTask(ILoadTask task) {
		tasks.add(task);
	}
	
	@Override
	public float getTotalWork() {
		float totalWork = completedAndRemovedWork;
		for(ILoadTask task : tasks) {
			totalWork += getTotalWork(task);
		}
		return totalWork;
	}
	
	@Override
	public float getCompletedWork() {
		float completedWork = completedAndRemovedWork;
		for(ILoadTask task : tasks) {
			completedWork += getCompletedWork(task);
		}
		return completedWork;
	}
	
	private static float getCompletedWork(ILoadTask task) {
		if(task instanceof IProgressingLoadTask) {
			return ((IProgressingLoadTask) task).getCompletedWork();
		}
		return 1f;
	}
	
	private static float getTotalWork(ILoadTask task) {
		if(task instanceof IProgressingLoadTask) {
			return ((IProgressingLoadTask) task).getTotalWork();
		}
		return 1f;
	}
}
