package com.github.systeminvecklare.badger.core.standard.input.mouse;

import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IScrollEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public interface IPoolableScrollEvent extends IScrollEvent, IPoolable {
	IPoolableScrollEvent init(IReadablePosition position, float scrollX, float scrollY);
}
