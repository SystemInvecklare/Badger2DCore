package com.github.systeminvecklare.badger.core.graphics.framework;

import java.util.HashMap;
import java.util.Map;

//TODO * maybe don't have this. It might be better to just have a global thing to check "isKeyDown"
//TODO * If we have this, move it and related to a different package.
public class KeyStateCache implements IKeyStateCache {
	private final Map<Integer, KeyState> map = new HashMap<Integer, IKeyStateCache.KeyState>();

	@Override
	public KeyState getKeyState(int keyID) {
		KeyState keyState = map.get(keyID);
		return keyState != null ? keyState : KeyState.KEY_UP;
	}

	@Override
	public void setKeyState(int keyID, KeyState state) {
		map.put(keyID, state);
	}
}
