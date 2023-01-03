package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransformOperation;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;
import com.github.systeminvecklare.badger.core.math.IInterpolator;
import com.github.systeminvecklare.badger.core.math.Interpolators;
import com.github.systeminvecklare.badger.core.math.Mathf;

public class InterpolatingOffsetBehavior extends OffsetBehavior {
	private final IInterpolator interpolator;
	private float targetOffsetX;
	private float targetOffsetY;
	private float offsetX;
	private float offsetY;
	private boolean targetState;
	private float state;
	private float duration;
	private boolean autoRemoveOnComplete = false;
	
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
		this.interpolator = interpolator;
		this.targetOffsetX = offsetX;
		this.targetOffsetY = offsetY;
		this.duration = duration;
		this.state = initialState ? 1 : 0;
		this.targetState = initialTargetState;
	}
	
	public InterpolatingOffsetBehavior removeOnCompletion() {
		this.autoRemoveOnComplete = true;
		return this;
	}
	
	public void setState(boolean currentState) {
		this.state = currentState ? 1 : 0;
		updateOffsets();
	}
	
	public void setTargetState(boolean targetState) {
		this.targetState = targetState;
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
	public void think(ITic tic) {
		if(targetState && state < 1) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 1;
			} else {
				state += SceneManager.get().getStep()/currentDuration;
			}
			updateOffsets();
			if(state >= 1) {
				onNewStateReached(targetState);
			}
		} else if(!targetState && state > 0) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 0;
			} else {
				state -= SceneManager.get().getStep()/currentDuration;
			}
			updateOffsets();
			if(state <= 0) {
				onNewStateReached(targetState);
			}
		}
	}
	
	/**
	 * @return true if the behavior was disposed.
	 */
	protected boolean onNewStateReached(boolean state) {
		if(autoRemoveOnComplete) {
			updateOffsets();
			final float endOffsetX = offsetX;
			final float endOffsetY = offsetY;
			IMovieClip bound = getBound();
			bound.removeBehavior(this);
			this.dispose();
			bound.modifyTransform(new ITransformOperation() {
				@Override
				public ITransform execute(ITransform transform) {
					return transform.addToPosition(endOffsetX, endOffsetY);
				}
			}, true, true);
			return true;
		}
		return false;
	}
	
	public float getDuration() {
		return duration;
	}
	
	private void updateOffsets() {
		final float value = getInterpolator().eval(Mathf.clamp(state, 0, 1));
		offsetX = getTargetOffsetX()*value;
		offsetY = getTargetOffsetY()*value;
	}

	public IInterpolator getInterpolator() {
		return interpolator;
	}

	public float getTargetOffsetX() {
		return targetOffsetX;
	}
	
	public float getTargetOffsetY() {
		return targetOffsetY;
	}

	@Override
	public float getOffsetX() {
		return offsetX;
	}
	
	@Override
	public float getOffsetY() {
		return offsetY;
	}
}
