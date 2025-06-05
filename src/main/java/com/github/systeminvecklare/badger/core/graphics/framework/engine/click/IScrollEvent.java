package com.github.systeminvecklare.badger.core.graphics.framework.engine.click;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IScrollEvent {
	boolean consume(); //Disable request to go any further
	boolean isConsumed();
	
	IReadablePosition getPosition();
	float getScrollX();
	float getScrollY();
}
