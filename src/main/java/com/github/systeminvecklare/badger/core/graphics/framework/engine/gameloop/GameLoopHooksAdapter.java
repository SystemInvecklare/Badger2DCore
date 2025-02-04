package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;

public class GameLoopHooksAdapter implements IGameLoopHooks {
	@Override
	public void onBeforeUpdates() {
	}
	
	@Override
	public void onAfterUpdates() {
	}

	@Override
	public void onBeforeThink() {
	}

	@Override
	public void onAfterThink() {
	}

	@Override
	public void onBeforeDraw() {
	}
	
	@Override
	public void onBeforeSceneDraw(IDrawCycle drawCycle) {
	}
	
	@Override
	public void onAfterSceneDraw(IDrawCycle drawCycle) {
	}

	@Override
	public void onAfterDraw() {
	}
}
