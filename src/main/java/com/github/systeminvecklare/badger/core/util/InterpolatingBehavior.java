package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;
import com.github.systeminvecklare.badger.core.math.IInterpolator;
import com.github.systeminvecklare.badger.core.math.Interpolators;
import com.github.systeminvecklare.badger.core.math.Mathf;

public class InterpolatingBehavior extends Behavior {
	private final IInterpolator interpolator;
	private boolean targetState;
	private float state;
	private float duration;
	private boolean autoRemoveOnComplete = false;
	
	public InterpolatingBehavior(float duration) {
		this(duration, Interpolators.linear());
	}
	
	public InterpolatingBehavior(float duration, IInterpolator interpolator) {
		this(duration, interpolator, true);
	}
	
	public InterpolatingBehavior(float duration, IInterpolator interpolator, boolean autoStart) {
		this(duration, interpolator, false, autoStart);
	}

	public InterpolatingBehavior(float duration, IInterpolator interpolator, boolean initialState, boolean initialTargetState) {
		this.interpolator = interpolator;
		this.duration = duration;
		this.state = initialState ? 1 : 0;
		this.targetState = initialTargetState;
	}
	
	public InterpolatingBehavior removeOnCompletion() {
		this.autoRemoveOnComplete = true;
		return this;
	}
	
	public void setState(boolean currentState) {
		this.state = currentState ? 1 : 0;
		onStateChanged(this.state);
	}
	
	public void setTargetState(boolean targetState) {
		this.targetState = targetState;
	}
	
	public float getState() {
		return state;
	}
	
	public boolean getTargetState() {
		return targetState;
	}
	
	@Override
	public void think(ITic tic) {
		if(targetState && state < 1) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 1;
			} else {
				state += SceneManager.get().getStep()/currentDuration;
				state = Mathf.clamp(state, 0, 1);
			}
			onStateChanged(state);
			if(state >= 1) {
				onNewStateReached(targetState);
			}
		} else if(!targetState && state > 0) {
			final float currentDuration = getDuration();
			if(currentDuration <= 0) {
				state = 0;
			} else {
				state -= SceneManager.get().getStep()/currentDuration;
				state = Mathf.clamp(state, 0, 1);
			}
			onStateChanged(state);
			if(state <= 0) {
				onNewStateReached(targetState);
			}
		}
	}
	
	protected void onStateChanged(float newState) {
	}
	
	/**
	 * @return true if the behavior was disposed.
	 */
	protected boolean onNewStateReached(boolean state) {
		if(autoRemoveOnComplete) {
			IMovieClip bound = getBound();
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
}
