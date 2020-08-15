package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class DeltaRotation implements IPoolable, IReadableDeltaRotation {
	private float theta;
	
	private IPool<DeltaRotation> pool;
	
	public DeltaRotation(IPool<DeltaRotation> pool) {
		this.pool = pool;
	}

	public DeltaRotation setTo(float theta) {
		this.theta = theta;
		return this;
	}
	
	public DeltaRotation setToZero() {
		return setTo(0);
	}
	
	public DeltaRotation setTo(IReadableRotation rotation) {
		return setTo(rotation.getTheta());
	}
	
	@Override
	public float getTheta()
	{
		return theta;
	}
	
	public Vector toUnitVector(Vector result)
	{
		float thetaValue = getTheta();
		return result.setTo(Mathf.cos(thetaValue), Mathf.sin(thetaValue));
	}
	
	
	@Override
	public boolean isCW()
	{
		return theta > 0;
	}
	
	@Override
	public boolean isCCW()
	{
		return theta < 0;
	}

	public DeltaRotation scale(float factor) {
		return setTo(getTheta()*factor);
	}
	
	public DeltaRotation add(IReadableDeltaRotation other)
	{
		return setTo(getTheta()+other.getTheta());
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public IPool<DeltaRotation> getPool() {
		return pool;
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(theta).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IReadableDeltaRotation) && equalsOwn((IReadableDeltaRotation) obj);
	}

	private boolean equalsOwn(IReadableDeltaRotation obj) {
		return this.theta == obj.getTheta();
	}
}
