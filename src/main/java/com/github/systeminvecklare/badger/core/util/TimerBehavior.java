package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;

public abstract class TimerBehavior extends TimedBehavior {
	private final float ringTime;
	private boolean running = true;
	
	public TimerBehavior(float ringTime) {
		this.ringTime = ringTime;
	}
	
	public TimerBehavior pause() {
		running = false;
		return this;
	}

	public TimerBehavior start() {
		running = true;
		return this;
	}
	
	@Override
	protected final float getTimeSpeed() {
		return running ? getTimeSpeed(super.getTimeSpeed()) : 0;
	}
	
	protected float getTimeSpeed(float current) {
		return current;
	}

	@Override
	public void think(ITic tic) {
		super.think(tic);
		if(getLifetime() >= ringTime) {
			onTrigger();
			IMovieClip bound = getBound();
			if(bound != null) {
				bound.removeBehavior(this);
			}
			this.dispose();
		}
	}

	protected abstract void onTrigger();
	
	public static TimerBehavior setTargetState(final float afterTime, final IStateControlledBehavior stateControlledBehavior, final boolean state) {
		return new TimerBehavior(afterTime) {
			@Override
			protected void onTrigger() {
				stateControlledBehavior.setTargetState(state);
			}
		};
	}
}
