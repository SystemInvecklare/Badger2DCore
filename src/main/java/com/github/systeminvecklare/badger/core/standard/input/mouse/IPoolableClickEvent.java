package com.github.systeminvecklare.badger.core.standard.input.mouse;

import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public interface IPoolableClickEvent extends IClickEvent, IPoolable {
	public IPoolableClickEvent init(IReadablePosition position, int button, int pointer);
}
