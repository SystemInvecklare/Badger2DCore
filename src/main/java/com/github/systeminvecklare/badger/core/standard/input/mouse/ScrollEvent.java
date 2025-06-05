package com.github.systeminvecklare.badger.core.standard.input.mouse;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class ScrollEvent extends AbstractConsumableEvent implements IPoolableScrollEvent {
	private IPool<IPoolableScrollEvent> pool;
	
	private Position position;
	private float scrollX;
	private float scrollY;
	
	public ScrollEvent(IPool<IPoolableScrollEvent> pool) {
		this.pool = pool;
		this.position = FlashyEngine.get().getPoolManager().getPool(Position.class).obtain();
	}

	@Override
	public IReadablePosition getPosition() {
		return position;
	}

	@Override
	public float getScrollX() {
		return scrollX;
	}
	
	@Override
	public float getScrollY() {
		return scrollY;
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	@Override
	public IPoolableScrollEvent init(IReadablePosition position, float scrollX, float scrollY) {
		this.position.setTo(position);
		this.scrollX = scrollX;
		this.scrollY = scrollY;
		resetConsumed();
		return this;
	}
}
