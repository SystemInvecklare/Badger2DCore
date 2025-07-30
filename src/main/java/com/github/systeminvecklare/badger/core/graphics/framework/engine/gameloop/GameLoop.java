package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.IApplicationContext;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;
import com.github.systeminvecklare.badger.core.graphics.framework.engine.inputprocessor.IInputHandler;

public abstract class GameLoop implements IGameLoop {
	private IApplicationContext applicationContext;
	private IInputHandler inputHandler;
	private float accum;
	private boolean skipUpdates = false;
	private IGameLoopHooks hooks;
	private IScene scenePreviousLoop = null;
	private boolean currentSceneHasHadOneThink = false;
	
	private Tic immutableTick = null;
	
	public GameLoop(IInputHandler inputHandler, IApplicationContext applicationContext, IGameLoopHooks hooks) {
		this.inputHandler = inputHandler;
		this.applicationContext = applicationContext;
		this.hooks = hooks; 
	}

	@Override
	public void execute(float deltaTime) {
		hooks.onBeforeUpdates();
		accum += deltaTime;
		IScene scene = getCurrentScene();
		if (scenePreviousLoop != scene) {
			currentSceneHasHadOneThink = false;
			sendForegroundingEvents(scenePreviousLoop, scene);
			scenePreviousLoop = scene;
		}
		if(scene != null) {
			inputHandler.handleInputs(scene);
		}
		final float currentStep = SceneManager.get().getStep();
		if(immutableTick == null || currentStep != immutableTick.getStep()) {
			immutableTick = new Tic(currentStep);
		}
		final ITic currentTick = immutableTick;
		boolean atLeastOneThink = false;
		if(skipUpdates)
		{
			if(accum >= currentStep)
			{
				skipUpdates = false;
				
				accum -= Math.floor(accum/currentStep)*currentStep;
				
				hooks.onBeforeThink();
				applicationContext.think(currentTick);
				if(scene != null) {
					scene.think(currentTick);
					atLeastOneThink = true;
				}
				hooks.onAfterThink();
			}
		}
		else
		{
			while(accum >= currentStep)
			{
				accum -= currentStep;
				hooks.onBeforeThink();
				applicationContext.think(currentTick);
				if(scene != null) {
					scene.think(currentTick);
					atLeastOneThink = true;
				}
				hooks.onAfterThink();
			}
		}
		if(!currentSceneHasHadOneThink) {
			if(!atLeastOneThink) {
				scene.think(currentTick); // Ensure at least one think
			}
			currentSceneHasHadOneThink = true;
		}
		hooks.onBeforeDraw();
		if(scene != null) {
			IDrawCycle drawCycle = this.newDrawCycle();
			hooks.onBeforeSceneDraw(drawCycle);
			scene.draw(drawCycle);
			hooks.onAfterSceneDraw(drawCycle);
			closeDrawCycle();
		}
		hooks.onAfterDraw();
		SceneManager.get().emptyTrashCan();
		hooks.onAfterUpdates();
	}
	
	private void sendForegroundingEvents(IScene backgroundedScene, IScene foregroundedScene) {
		if(backgroundedScene != null) {
			backgroundedScene.onBackgrounded(backgroundedScene, foregroundedScene);
		}
		if(foregroundedScene != null) {
			foregroundedScene.onForegrounded(backgroundedScene, foregroundedScene);
		}
	}

	@Override
	public void skipQueuedUpdates() {
		skipUpdates = true;
	}


	protected abstract IScene getCurrentScene();
	
	protected abstract IDrawCycle newDrawCycle(); //drawCycle.reset()
	protected abstract void closeDrawCycle(); //drawCycle.getSpriteBatch().end()
}
