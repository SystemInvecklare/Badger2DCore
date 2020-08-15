package com.github.systeminvecklare.badger.core.standard.input.keyboard;

import java.util.ArrayList;
import java.util.Collection;

import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyReleaseListener;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableIterable;
import com.github.systeminvecklare.badger.core.pooling.IPool;

public class KeyPressEvent implements IPoolableKeyPressEvent {
	private int keyCode;
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
	public IPool<IPoolableKeyPressEvent> getPool() {
		return pool;
	}

	@Override
	public KeyPressEvent init(int keyCode) {
		if(!listeners.isEmpty()) listeners.clear();
		this.keyCode = keyCode;
		return this;
	}

}
