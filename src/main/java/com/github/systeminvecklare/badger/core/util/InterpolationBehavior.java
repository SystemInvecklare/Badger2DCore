package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.behavior.Behavior;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;
import com.github.systeminvecklare.badger.core.math.IInterpolator;
import com.github.systeminvecklare.badger.core.math.Mathf;

public class InterpolationBehavior extends Behavior {
	private final IInterpolator interpolator;
	private final float time;
	private float lifetime = 0;
	private float value = 0;

	public InterpolationBehavior(IInterpolator interpolator, float time) {
		this.interpolator = interpolator;
		this.time = time;
	}
	
	@Override
	public void think(ITic tic) {
		if(lifetime > time && this.value != 1) {
			this.value = 1;
		} else {
			lifetime += SceneManager.get().getStep();
			float t = Mathf.clamp(lifetime/time, 0, 1);
			this.value = interpolator.eval(t);
		}
	}
	
	public float getValue() {
		return value;
	}
	
	public IInterpolator getInterpolator() {
		return interpolator;
	}
}
