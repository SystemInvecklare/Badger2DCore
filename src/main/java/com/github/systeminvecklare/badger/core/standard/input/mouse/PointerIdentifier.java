package com.github.systeminvecklare.badger.core.standard.input.mouse;

import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public class PointerIdentifier implements IPoolable {
	private int pointer;
	private int button;
	
	private IPool<PointerIdentifier> pool;
	
	public PointerIdentifier(IPool<PointerIdentifier> pool) {
		this.pool = pool;
	}

	public PointerIdentifier setTo(int pointer, int button) {
		this.pointer = pointer;
		this.button = button;
		return this;
	}
	
	@Override
	public void free() {
		pool.free(this);
	}
	
	public int getPointer() {
		return pointer;
	}
	
	public int getButton() {
		return button;
	}
	
	@Override
	public int hashCode() {
		return pointer ^ button;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PointerIdentifier) && ((PointerIdentifier) obj).pointer == this.pointer && ((PointerIdentifier) obj).button == this.button;
	}
}
