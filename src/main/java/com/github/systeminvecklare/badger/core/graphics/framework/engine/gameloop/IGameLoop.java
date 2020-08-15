package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

public interface IGameLoop {
	public void execute(float deltaTime);
	public void skipQueuedUpdates();
}
