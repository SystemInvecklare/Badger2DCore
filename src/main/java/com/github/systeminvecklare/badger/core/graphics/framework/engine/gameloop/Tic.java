package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;

/*package-protected*/ class Tic implements ITic {
	private final float step;
	
	public Tic(float step) {
		this.step = step;
	}

	@Override
	public float getStep() {
		return step;
	}
	
	@Override
	public int hashCode() {
		return Float.hashCode(step);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Tic) {
			return this.step == ((Tic) obj).step;
		}
		return super.equals(obj);
	}
}
