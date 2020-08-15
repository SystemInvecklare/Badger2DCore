package com.github.systeminvecklare.badger.core.math;

public interface IReadableRotation {
	public float getTheta();
	public DeltaRotation deltaTo(IReadableRotation other, DeltaRotation result);
}
