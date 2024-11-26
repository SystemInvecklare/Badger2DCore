package com.github.systeminvecklare.badger.core.graphics.framework.engine.click;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDragEventListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IReleaseEventListener;

public interface IClickEvent extends IPointerStateEvent {
	public boolean consume(); //Disable request to go any further
	
	public boolean isConsumed();
	public void fireRelease(IPointerStateEvent e);
	public void fireDrag(IPointerStateEvent e);
	public void addReleaseListener(IReleaseEventListener listener);
	public void addDragListener(IDragEventListener listener);
}
