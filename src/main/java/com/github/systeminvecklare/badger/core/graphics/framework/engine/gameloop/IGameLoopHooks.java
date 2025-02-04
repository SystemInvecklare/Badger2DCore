package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;

public interface IGameLoopHooks {
	public void onBeforeUpdates();
	public void onAfterUpdates();
	public void onBeforeThink();
	public void onAfterThink();
	public void onBeforeDraw();
	public void onBeforeSceneDraw(IDrawCycle drawCycle);
	public void onAfterSceneDraw(IDrawCycle drawCycle);
	public void onAfterDraw();
}
