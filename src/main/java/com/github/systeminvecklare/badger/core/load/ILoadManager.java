package com.github.systeminvecklare.badger.core.load;

public interface ILoadManager extends ILoadTask {
	/**
	 * @return true if all tasks done
	 */
	@Override boolean load();

	void addTask(ILoadTask task);
}
