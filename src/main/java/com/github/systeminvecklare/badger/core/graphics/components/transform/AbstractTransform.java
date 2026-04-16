package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.math.IReadableDeltaRotation;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolManager;

public abstract class AbstractTransform implements ITransform {
	@Override
	public ITransform copy(IPool<ITransform> pool) {
		return pool.obtain().setTo(this);
	}

	@Override
	public ITransform copy(EasyPooler ep) {
		return ep.obtain(ITransform.class).setTo(this);
	}
	
	@Override
	public ITransform copy(IPoolManager poolManager) {
		return copy(poolManager.getPool(ITransform.class));
	}
	
	public ITransform addRadialToPosition(float theta, float scale) {
		return addToPosition(Mathf.cos(theta)*scale, Mathf.sin(theta)*scale);
	}
	
	@Override
	public ITransform addRotationAround(float dtheta, float x, float y) {
		// Pivot given in local coordinates. So apply transform (except translation). 
		IReadableVector currentScale = getScale();
		x *= currentScale.getX();
		y *= currentScale.getY();
		float currentRotation = getRotation().getTheta();
		float cos = Mathf.cos(currentRotation);
		float sin = Mathf.sin(currentRotation);
		float temp = x*cos - y*sin;
		y = x*sin + y*cos;
		x = temp;
		
		cos = Mathf.cos(dtheta);
		sin = Mathf.sin(dtheta);
		return addToRotation(dtheta).addToPosition(x - cos*x + sin*y, y - sin*x - cos*y);
	}
	
	@Override
	public ITransform addRotationAround(float dtheta, IReadablePosition pivot) {
		return addRotationAround(dtheta, pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform addRotationAround(float dtheta, IReadableVector pivot) {
		return addRotationAround(dtheta, pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform addRotationAround(IReadableDeltaRotation dtheta, float x, float y) {
		return addRotationAround(dtheta.getTheta(), x, y);
	}
	
	@Override
	public ITransform addRotationAround(IReadableDeltaRotation dtheta, IReadablePosition pivot) {
		return addRotationAround(dtheta.getTheta(), pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform addRotationAround(IReadableDeltaRotation dtheta, IReadableVector pivot) {
		return addRotationAround(dtheta.getTheta(), pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(float scaleX, float scaleY, float x, float y) {
		// Pivot given in local coordinates. So apply transform (except translation). 
		IReadableVector currentScale = getScale();
		x *= currentScale.getX();
		y *= currentScale.getY();
		float currentRotation = getRotation().getTheta();
		float cos = Mathf.cos(currentRotation);
		float sin = Mathf.sin(currentRotation);
		float temp = x*cos - y*sin;
		y = x*sin + y*cos;
		x = temp;
		return multiplyScale(scaleX, scaleY).addToPosition(x - scaleX*x, y - scaleY*y);
	}
	
	@Override
	public ITransform multiplyScaleAround(IReadableVector scale, float x, float y) {
		return multiplyScaleAround(scale.getX(), scale.getY(), x, y);
	}
	
	@Override
	public ITransform multiplyScaleAround(float scale, float x, float y) {
		return multiplyScaleAround(scale, scale, x, y);
	}
	
	@Override
	public ITransform multiplyScaleAround(float scaleX, float scaleY, IReadableVector pivot) {
		return multiplyScaleAround(scaleX, scaleY, pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(IReadableVector scale, IReadableVector pivot) {
		return multiplyScaleAround(scale.getX(), scale.getY(), pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(float scale, IReadableVector pivot) {
		return multiplyScaleAround(scale, scale, pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(float scaleX, float scaleY, IReadablePosition pivot) {
		return multiplyScaleAround(scaleX, scaleY, pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(IReadableVector scale, IReadablePosition pivot) {
		return multiplyScaleAround(scale.getX(), scale.getY(), pivot.getX(), pivot.getY());
	}
	
	@Override
	public ITransform multiplyScaleAround(float scale, IReadablePosition pivot) {
		return multiplyScaleAround(scale, scale, pivot.getX(), pivot.getY());
	}
}
