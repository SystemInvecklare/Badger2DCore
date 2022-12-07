package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;
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
	
	public DeltaRotation setTo(IReadableDeltaRotation deltaRotation) {
		return setTo(deltaRotation.getTheta());
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
	
	public DeltaRotation add(float theta) {
		return setTo(getTheta()+theta);
	}
	
	public DeltaRotation addScaled(IReadableDeltaRotation delta, float scale) {
		return add(delta.getTheta()*scale);
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public DeltaRotation copy() {
		return new DeltaRotation(null).setTo(getTheta());
	}
	
	@Override
	public DeltaRotation copy(EasyPooler ep) {
		return ep.obtain(DeltaRotation.class).setTo(this);
	}
	
	@Override
	public DeltaRotation copy(IPool<DeltaRotation> pool) {
		return pool.obtain().setTo(this);
	}
	
	@Override
	public DeltaRotation copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(DeltaRotation.class));
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
