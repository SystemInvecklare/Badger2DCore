package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

public interface IGameLoopHooks {
	public void onBeforeUpdates();
	public void onAfterUpdates();
	public void onBeforeThink();
	public void onAfterThink();
	public void onBeforeDraw();
	public void onAfterDraw();
	public void onAfterSceneDraw();
}
