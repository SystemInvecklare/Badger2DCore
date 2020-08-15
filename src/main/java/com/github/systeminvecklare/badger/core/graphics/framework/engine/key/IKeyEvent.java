package com.github.systeminvecklare.badger.core.graphics.framework.engine.key;

public interface IKeyEvent {
	public int getKeyID();
	public KeyEventType getKeyEventType();
	public static class KeyEventType {
		public static final KeyEventType KEY_DOWN = new KeyEventType();
		public static final KeyEventType KEY_UP = new KeyEventType();
		public static final KeyEventType KEY_TYPED = new KeyEventType();
	}
}
