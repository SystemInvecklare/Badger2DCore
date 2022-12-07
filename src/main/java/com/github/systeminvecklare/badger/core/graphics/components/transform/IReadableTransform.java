package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableRotation;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.ICopyablePoolable;

public interface IReadableTransform extends ICopyablePoolable<ITransform> {
	public IReadablePosition getPosition();
	public IReadableRotation getRotation();
	public IReadableVector getScale();
	public float getShear();

	/**
	 * Transforms the argumentAndResult with this transform.
	 * 
	 * @param argumentAndResult
	 */
	public void transform(Position argumentAndResult);
}
