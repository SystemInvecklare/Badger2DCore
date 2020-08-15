package com.github.systeminvecklare.badger.core.graphics.framework.engine.click;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;

public interface IClickEvent extends IPointerStateEvent {
	public void consume(); //Disable request to go any futher
	
	public boolean isConsumed();
	public void fireRelease(IPointerStateEvent e);
	public void fireDrag(IPointerStateEvent e);
	public void addReleaseListener(IReleaseEventListener listener);
	public void addDragListener(IDragEventListener listener);
}
