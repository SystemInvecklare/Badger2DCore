package com.github.systeminvecklare.badger.core.standard.input.keyboard;

import com.github.systeminvecklare.badger.core.pooling.IPoolable;

public interface IPoolableKeyPressEvent extends IKeyPressEvent, IPoolable {
	public IPoolableKeyPressEvent init(int keyCode);
}
