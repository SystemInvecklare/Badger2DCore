package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableDeltaRotation extends ICopyablePoolable<DeltaRotation> {
	public float getTheta();
	public boolean isCW();
	public boolean isCCW();
}
