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
	/**
	 * Sets the 'result' to the closest quantization starting from <code>thetaZero</code>.
	 * 'result' may be 'null'.
	 * 
	 * @return The index of the quantization. For example, if the closest rotation was <code>thetaZero + 3*(2.0*Math.PI/steps)</code> the value returned is <code>3</code>.
	 */
	public int quantize(float thetaZero, int steps, Rotation result);
}
