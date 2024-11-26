package com.github.systeminvecklare.badger.core.standard.input.keyboard;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyReleaseListener;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class KeyPressEvent implements IPoolableKeyPressEvent {
	private int keyCode;
	private boolean consumed;
	private Collection<IKeyReleaseListener> listeners = new ArrayList<IKeyReleaseListener>();
	private IPool<IPoolableKeyPressEvent> pool;
	
	public KeyPressEvent(IPool<IPoolableKeyPressEvent> pool) {
		this.pool = pool;
	}

	@Override
	public int getKeyCode() {
		return keyCode;
	}

	@Override
	public void fireRelease(IKeyReleaseEvent e) {
		PoolableIterable<IKeyReleaseListener> listenerLoop = PoolableIterable.obtain(IKeyReleaseListener.class);
		for(IKeyReleaseListener listener : listenerLoop.setToCopy(listeners))
		{
			listener.onRelease(e);
		}
		listeners.clear();
		listenerLoop.free();
	}

	@Override
	public void addKeyReleaseListener(IKeyReleaseListener listener) {
		listeners.add(listener);
	}

	@Override
	public void free() {
		listeners.clear();
		pool.free(this);
	}

	@Override
	public KeyPressEvent init(int keyCode) {
		if(!listeners.isEmpty()) listeners.clear();
		this.keyCode = keyCode;
		this.consumed = false;
		return this;
	}

	@Override
	public boolean consume() {
		if(consumed) {
			return false;
		} else {
			consumed = true;
			return true;
		}
	}

	@Override
	public boolean isConsumed() {
		return consumed;
	}
}
