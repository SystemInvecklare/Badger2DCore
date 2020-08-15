package com.github.systeminvecklare.badger.core.math;

public abstract class AbstractRotation implements IReadableRotation {

	/**
	 * Gets the shortest delta rotation to other.
	 * @param other
	 * @return
	 */
	@Override
	public DeltaRotation deltaTo(IReadableRotation other, DeltaRotation result) {
		return Rotation.deltaTo(this, other, result);
	}
}
