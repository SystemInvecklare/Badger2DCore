package com.github.systeminvecklare.badger.core.graphics.framework.engine.click;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IPointerStateEvent {
	public IReadablePosition getPosition();
	public int getButton();
	public int getPointerID();//Buttons can share the same pointer. This means that different clicks will have the same positions.
}
