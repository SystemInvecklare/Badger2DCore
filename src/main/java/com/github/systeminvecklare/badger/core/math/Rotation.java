package com.github.systeminvecklare.badger.core.math;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class Rotation extends AbstractRotation implements IPoolable, IReadableRotation {
	private float theta;
	
	private IPool<Rotation> pool;
	
	public Rotation(IPool<Rotation> pool) {
		this.pool = pool;
	}
	
	public Rotation setToZero()
	{
		return setTo(0);
	}
	
	public Rotation setTo(IReadableDeltaRotation deltaRotation)
	{
		return setTo(deltaRotation.getTheta());
	}

	public Rotation setTo(IReadableRotation rotation) {
		return setTo(rotation.getTheta());
	}
	
	public static float rotationClamp(float theta)
	{
		float fixed = theta;
		while(fixed >= Mathf.PI*2)
		{
			fixed -= Mathf.PI*2;
		}
		while(fixed < 0)
		{
			fixed += Mathf.PI*2;
		}
		return fixed;
	
	}

	public Rotation setTo(float theta) {
		this.theta = rotationClamp(theta);
		return this;
	}
	
	public Rotation setTo(IReadableVector vector) {
		return setTo(vector.getRotationTheta());
	}
	
	@Override
	public float getTheta()
	{
		return theta;
	}
	
	
	public static DeltaRotation deltaTo(IReadableRotation from, IReadableRotation to, DeltaRotation result)
	{
		float diff = to.getTheta() - from.getTheta();
		if(diff >= 0)
		{
			if(diff > Mathf.PI)
			{
				return result.setTo(diff-(Mathf.PI*2));
			}
			else
			{
				return result.setTo(diff);
			}
		}
		else
		{
			return to.deltaTo(from,result).scale(-1f);
		}
	}
	
	public Rotation add(IReadableDeltaRotation delta)
	{
		return setTo(getTheta()+delta.getTheta());
	}
	
	public Rotation addScaled(IReadableDeltaRotation delta, float scale)
	{
		return add(delta.getTheta()*scale);
	}
	
	public Rotation add(float deltaTheta)
	{
		return setTo(getTheta()+deltaTheta);
	}
	
	public Vector toUnitVector(Vector result)
	{
		float thetaValue = getTheta();
		return result.setTo(Mathf.cos(thetaValue), Mathf.sin(thetaValue));
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(theta).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IReadableRotation) && equalsOwn((IReadableRotation) obj);
	}

	private boolean equalsOwn(IReadableRotation obj) {
		return this.theta == obj.getTheta();
	}
}
