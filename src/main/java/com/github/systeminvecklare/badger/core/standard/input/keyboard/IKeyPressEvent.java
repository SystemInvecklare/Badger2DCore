package com.github.systeminvecklare.badger.core.standard.input.keyboard;

import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyReleaseListener;

public interface IKeyPressEvent extends IKeyReleaseEvent {
	public void fireRelease(IKeyReleaseEvent e);
	public void addKeyReleaseListener(IKeyReleaseListener listener);
}
