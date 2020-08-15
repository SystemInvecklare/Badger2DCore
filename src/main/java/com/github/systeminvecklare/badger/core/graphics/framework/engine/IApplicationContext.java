package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;

public interface IApplicationContext extends IThinker {
	public void addThinker(IThinker thinker);
	public void removeThinker(IThinker thinker);
}
