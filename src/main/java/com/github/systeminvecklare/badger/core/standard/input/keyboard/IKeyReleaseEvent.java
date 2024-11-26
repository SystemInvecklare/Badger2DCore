package com.github.systeminvecklare.badger.core.standard.input.keyboard;

public interface IKeyReleaseEvent {
	boolean consume(); //Disable request to go any further
	boolean isConsumed();
	int getKeyCode();
}
