package com.github.systeminvecklare.badger.core.load;

public interface IProgressingLoadTask extends ILoadTask {
	float getCompletedWork();
	float getTotalWork();
}
