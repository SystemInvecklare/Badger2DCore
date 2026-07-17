package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;

/*package-protected*/ class MutableTic implements ITic {
	private float step;
	
	@Override
	public float getStep() {
		return step;
	}
	
	public MutableTic set(float step) {
		this.step = step;
		return this;
	}
}
