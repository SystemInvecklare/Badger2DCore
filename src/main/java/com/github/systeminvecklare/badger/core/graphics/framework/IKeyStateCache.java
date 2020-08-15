package com.github.systeminvecklare.badger.core.graphics.framework;

public interface IKeyStateCache {
	
	public KeyState getKeyState(int keyID);
	public void setKeyState(int keyID, KeyState state);
	
	public static class KeyState {
		public static final KeyState KEY_DOWN = new KeyState();
		public static final KeyState KEY_UP = new KeyState();
	}
}
