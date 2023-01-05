package com.github.systeminvecklare.badger.core.graphics.components.transform;

import com.github.systeminvecklare.badger.core.math.IReadableDeltaRotation;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.IReadableRotation;
import com.github.systeminvecklare.badger.core.math.IReadableVector;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;


public interface ITransform extends IPoolable, IReadableTransform {
	public ITransform setTo(IReadablePosition position, IReadableVector scale, IReadableRotation rotation, float shear);
	public ITransform setTo(IReadableTransform other);
	public ITransform setToIdentity();
	public ITransform setPosition(IReadablePosition position);
	public ITransform setPosition(float x, float y);
	public ITransform setRotation(IReadableRotation rotation);
	public ITransform setRotation(float theta);
	public ITransform setScale(IReadableVector scale);
	public ITransform setScale(float x, float y);
	public ITransform setShear(float shear);
	public ITransform mult(IReadableTransform other);
	public ITransform multLeft(IReadableTransform other);
	public ITransform invert();
	public ITransform addToPosition(float dx, float dy);
	public ITransform addToPosition(IReadableVector dvector);
	public ITransform addToRotation(float dtheta);
	public ITransform addToRotation(IReadableDeltaRotation drotation);
	public ITransform multiplyScale(float sx, float sy);
	public ITransform multiplyScale(IReadableVector dscale);
	public ITransform multiplyScale(float s);
}
