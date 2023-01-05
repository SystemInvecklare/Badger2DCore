package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.IReadableTransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.math.IInterpolator;
import com.github.systeminvecklare.badger.core.math.Interpolators;

public class InterpolatingOffsetBehavior extends AbstractInterpolatingBehavior<InterpolatingOffsetBehavior> {
	private float targetOffsetX;
	private float targetOffsetY;
	private float offsetX;
	private float offsetY;
	
	public InterpolatingOffsetBehavior(float offsetX, float offsetY, float duration) {
		this(offsetX, offsetY, duration, Interpolators.linear());
	}
	
	public InterpolatingOffsetBehavior(float offsetX, float offsetY, float duration, IInterpolator interpolator) {
		this(offsetX, offsetY, duration, interpolator, true);
	}
	
	public InterpolatingOffsetBehavior(float offsetX, float offsetY, float duration, IInterpolator interpolator, boolean autoStart) {
		this(offsetX, offsetY, duration, interpolator, false, autoStart);
	}

	public InterpolatingOffsetBehavior(float offsetX, float offsetY, float duration, IInterpolator interpolator, boolean initialState, boolean initialTargetState) {
		super(duration, interpolator, initialState, initialTargetState);
		this.targetOffsetX = offsetX;
		this.targetOffsetY = offsetY;
		updateOffsets();
	}
	
	@Override
	public ITransform getTransform(IReadableTransform transform, ITransform result) {
		return result.setTo(transform).addToPosition(getOffsetX(), getOffsetY());
	}
	
	public void setTargetOffsetX(float targetOffsetX) {
		this.targetOffsetX = targetOffsetX;
		updateOffsets();
	}
	
	public void setTargetOffsetY(float targetOffsetY) {
		this.targetOffsetY = targetOffsetY;
		updateOffsets();
	}
	
	public void setTargetOffset(float targetOffsetX, float targetOffsetY) {
		setTargetOffsetX(targetOffsetX);
		setTargetOffsetY(targetOffsetY);
	}
	
	@Override
	protected void onBeforeAutoRemove(IMovieClip bound) {
		super.onBeforeAutoRemove(bound);
		updateOffsets();
		final float endOffsetX = offsetX;
		final float endOffsetY = offsetY;
		bound.modifyTransform(new ITransformOperation() {
			@Override
			public ITransform execute(ITransform transform) {
				return transform.addToPosition(endOffsetX, endOffsetY);
			}
		}, true, true);
	}
	
	@Override
	protected void internalOnStateChanged() {
		super.internalOnStateChanged();
		updateOffsets();
	}
	
	private void updateOffsets() {
		final float value = getValue();
		offsetX = getTargetOffsetX()*value;
		offsetY = getTargetOffsetY()*value;
	}

	public float getTargetOffsetX() {
		return targetOffsetX;
	}
	
	public float getTargetOffsetY() {
		return targetOffsetY;
	}

	public float getOffsetX() {
		return offsetX;
	}
	
	public float getOffsetY() {
		return offsetY;
	}
}
