package com.github.systeminvecklare.badger.core.standard.input.mouse;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.click.IPointerStateEvent;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class ClickEvent extends AbstractConsumableEvent implements IPoolableClickEvent {
	private IPool<IPoolableClickEvent> pool;
	
	private Collection<IReleaseEventListener> listeners = new ArrayList<IReleaseEventListener>();
	private Collection<IDragEventListener> dragListeners = new ArrayList<IDragEventListener>();
	
	private Position position;
	private int button;
	private int pointer;
	
	public ClickEvent(IPool<IPoolableClickEvent> pool) {
		this.pool = pool;
		this.position = FlashyEngine.get().getPoolManager().getPool(Position.class).obtain();
	}

	@Override
	public IReadablePosition getPosition() {
		return position;
	}

	@Override
	public int getButton() {
		return button;
	}

	@Override
	public int getPointerID() {
		return pointer;
	}
	
	@Override
	public void fireDrag(IPointerStateEvent e) {
		PoolableIterable<IDragEventListener> listenerLoop = PoolableIterable.obtain(IDragEventListener.class);
		for(IDragEventListener dragListener : listenerLoop.setToCopy(dragListeners))
		{
			dragListener.onDrag(e);
		}
		listenerLoop.free();
	}

	@Override
	public void fireRelease(IPointerStateEvent e) {
		PoolableIterable<IReleaseEventListener> listenerLoop = PoolableIterable.obtain(IReleaseEventListener.class);
		for(IReleaseEventListener listener : listenerLoop.setToCopy(listeners))
		{
			listener.onRelease(e);
		}
		listeners.clear();
		dragListeners.clear();
		listenerLoop.free();
	}

	@Override
	public void addReleaseListener(IReleaseEventListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void addDragListener(IDragEventListener listener) {
		dragListeners.add(listener);
	}
	
	@Override
	public void free() {
		listeners.clear();
		dragListeners.clear();
		pool.free(this);
	}
	
	@Override
	public ClickEvent init(IReadablePosition position, int button, int pointer) {
		this.position.setTo(position);
		this.button = button;
		this.pointer = pointer;
		resetConsumed();
		return this;
	}
	
	@Override
	public Collection<? extends IDragEventListener> getDragListeners() {
		return dragListeners;
	}
	
	@Override
	public Collection<? extends IReleaseEventListener> getReleaseListeners() {
		return listeners;
	}

	@Override
	public void inheritListeners(IPoolableClickEvent other) {
		this.listeners.addAll(other.getReleaseListeners());
		this.dragListeners.addAll(other.getDragListeners());
	}
}
