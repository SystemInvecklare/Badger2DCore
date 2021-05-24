package com.github.systeminvecklare.badger.core.graphics.framework.smartlist;

import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;

public class ThinkAction implements ILoopAction<IThinker> {
	private ITic tic;
	
	public ThinkAction(ITic tic) {
		this.tic = tic;
	}

	public void setTic(ITic tic) {
		this.tic = tic;
	}
	
	@Override
	public boolean onIteration(IThinker value) {
		value.think(tic);
		return true;
	}
}