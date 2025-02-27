package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.math.IInterpolator;
import com.github.systeminvecklare.badger.core.math.Interpolators;
import com.github.systeminvecklare.badger.core.math.Mathf;

/*package-protected*/ abstract class AbstractInterpolatingBehavior<Self> extends Behavior {
	private final IInterpolator interpolator;
	private boolean targetState;
	private float state;
	private float duration;
	private boolean autoRemoveOnComplete = false;
	
	public AbstractInterpolatingBehavior(float duration) {
		this(duration, Interpolators.linear());
	}
	
	public AbstractInterpolatingBehavior(float duration, IInterpolator interpolator) {
		this(duration, interpolator, true);
	}
	
	public AbstractInterpolatingBehavior(float duration, IInterpolator interpolator, boolean autoStart) {
		this(duration, interpolator, false, autoStart);
	}

	public AbstractInterpolatingBehavior(float duration, IInterpolator interpolator, boolean initialState, boolean initialTargetState) {
		this.interpolator = interpolator;
		this.duration = duration;
		this.state = initialState ? 1 : 0;
		this.targetState = initialTargetState;
	}
	
	public Self removeOnCompletion() {
		this.autoRemoveOnComplete = true;
		return self();
	}
	
	public void setState(boolean currentState) {
		this.state = currentState ? 1 : 0;
		onStateChanged(this.state);
	}
	
	public void setTargetState(boolean targetState) {
		this.targetState = targetState;
	}
	
	public final float getState() {
		return state;
	}
	
	public boolean getTargetState() {
		return targetState;
	}
	
	@Override
	public void think(ITic tic) {
		final boolean currentTargetState = getTargetState();
		if(currentTargetState && state < 1) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 1;
			} else {
				state += getStep(tic)/currentDuration;
				state = Mathf.clamp(state, 0, 1);
			}
			internalOnStateChanged();
			onStateChanged(state);
			if(state >= 1) {
				onNewStateReached(currentTargetState);
			}
		} else if(!currentTargetState && state > 0) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 0;
			} else {
				state -= getStep(tic)/currentDuration;
				state = Mathf.clamp(state, 0, 1);
			}
			internalOnStateChanged();
			onStateChanged(state);
			if(state <= 0) {
				onNewStateReached(currentTargetState);
			}
		}
	}
	
	protected float getStep(ITic tic) {
		return tic.getStep();
	}

	protected void onStateChanged(float newState) {
	}
	
	/**
	 * @return true if the behavior was disposed.
	 */
	protected boolean onNewStateReached(boolean state) {
		if(autoRemoveOnComplete) {
			IMovieClip bound = getBound();
			onBeforeAutoRemove(bound);
			bound.removeBehavior(this);
			this.dispose();
			return true;
		}
		return false;
	}
	
	public float getDuration() {
		return duration;
	}

	public IInterpolator getInterpolator() {
		return interpolator;
	}
	
	public float getValue() {
		return getInterpolator().eval(Mathf.clamp(getState(), 0, 1));
	}
	
	@SuppressWarnings("unchecked")
	private Self self() {
		return (Self) this;
	}
	
	protected void internalOnStateChanged() {
	}
	
	protected void onBeforeAutoRemove(IMovieClip bound) {
	}
}
