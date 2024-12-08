package com.github.systeminvecklare.badger.core.standard.input.mouse;

import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IClickEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public interface IPoolableClickEvent extends IClickEvent, IPoolable {
	IPoolableClickEvent init(IReadablePosition position, int button, int pointer);
	Collection<? extends IReleaseEventListener> getReleaseListeners();
	Collection<? extends IDragEventListener> getDragListeners();
	void inheritListeners(IPoolableClickEvent other);
}
