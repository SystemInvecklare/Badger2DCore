package com.github.systeminvecklare.badger.core.graphics.framework;

import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyReleaseListener;
import com.github.systeminvecklare.badger.core.graphics.framework.IKeyStateCache.KeyState;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyReleaseEvent;

public class KeyListener implements IKeyPressListener, IKeyReleaseListener {
	private final IKeyStateCache cache;

	public KeyListener(IKeyStateCache cache) {
		this.cache = cache;
	}
	
	@Override
	public void onKeyPress(IKeyPressEvent event) {
		cache.setKeyState(event.getKeyCode(), KeyState.KEY_DOWN);
		event.addKeyReleaseListener(this);
	}

	@Override
	public void onRelease(IKeyReleaseEvent e) {
		cache.setKeyState(e.getKeyCode(), KeyState.KEY_UP);
	}
}
