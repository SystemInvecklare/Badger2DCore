package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;

public class TimedBehavior extends Behavior {
	private boolean disposed = false;
	private float lifetime = 0;
	
	@Override
	public void think(ITic tic) {
		if(isDisposed()) {
			return;
		}
		lifetime += tic.getStep()*getTimeSpeed();
	}

	protected float getTimeSpeed() {
		return 1f;
	}

	protected float getLifetime() {
		return lifetime;
	}
	
	protected boolean isDisposed() {
		return disposed;
	}
	
	@Override
	public void dispose() {
		disposed = true;
	}
}
