package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public interface IReadableRotation extends ICopyablePoolable<Rotation> {
	public float getTheta();
	public DeltaRotation deltaTo(IReadableRotation other, DeltaRotation result);
	public Vector toUnitVector();
	public Vector toUnitVector(EasyPooler ep);
	public Vector toUnitVector(IPool<Vector> pool);
}
