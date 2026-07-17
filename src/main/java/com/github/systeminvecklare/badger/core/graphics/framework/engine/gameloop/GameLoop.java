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
	private double accumMillis;
	private boolean skipUpdates = false;
	private IGameLoopHooks hooks;
	private IScene scenePreviousLoop = null;
	private boolean currentSceneHasHadOneThink = false;
	
	private final TimeMeasurer inputHandlingTime = new TimeMeasurer();
	private final TimeMeasurer renderTime = new TimeMeasurer();
	private final TimeMeasurer thinkTime = new TimeMeasurer();
	
	private Tic immutableTick = null;
	private final MutableTic mutableTic = new MutableTic();
	private final boolean variableStepSizeAllowed = isAllowVariableStepSize();
	
	public GameLoop(IInputHandler inputHandler, IApplicationContext applicationContext, IGameLoopHooks hooks) {
		this.inputHandler = inputHandler;
		this.applicationContext = applicationContext;
		this.hooks = hooks; 
	}

	@Override
	public void execute(float deltaTime) {
		inputHandlingTime.start();
		hooks.onBeforeUpdates();
		accumMillis += deltaTime*1000.0;
		IScene scene = getCurrentScene();
		if (scenePreviousLoop != scene) {
			currentSceneHasHadOneThink = false;
			sendForegroundingEvents(scenePreviousLoop, scene);
			scenePreviousLoop = scene;
		}
		if(scene != null) {
			inputHandler.handleInputs(scene);
		}
		final double currentStepMillis = SceneManager.get().getStep()*1000.0;
		accumMillis = Math.min(accumMillis, currentStepMillis*10.0); // max 10 accum frames
		{ // scope
			float floatStep = SceneManager.get().getStep();
			if(immutableTick == null || floatStep != immutableTick.getStep()) {
				immutableTick = new Tic(floatStep);
			}
		}
		inputHandlingTime.stop();
		final ITic stepSizedTick = immutableTick;
		boolean atLeastOneThink = false;
		if(skipUpdates)
		{
			if(accumMillis >= currentStepMillis)
			{
				skipUpdates = false;
				
				accumMillis -= Math.floor(accumMillis/currentStepMillis)*currentStepMillis;
				
				hooks.onBeforeThink();
				applicationContext.think(stepSizedTick);
				if(scene != null) {
					scene.think(stepSizedTick);
					atLeastOneThink = true;
				}
				hooks.onAfterThink();
			}
		}
		else
		{
			double updateTimeMillis = thinkTime.readoutMillis();
			double renderAndInputTimeMillis = inputHandlingTime.readoutMillis() + renderTime.readoutMillis();
			int updates = (int) (Math.min(accumMillis/currentStepMillis, Math.max((currentStepMillis - renderAndInputTimeMillis)/updateTimeMillis, 1)));
			
			if(variableStepSizeAllowed && updates == 1 && accumMillis - currentStepMillis >= currentStepMillis) {
				boolean skippdedThink = false;
				
				mutableTic.set((float)((accumMillis - currentStepMillis)/1000.0));
				
				thinkTime.start();
				accumMillis = currentStepMillis; // Yes. accumMillis -= accumMillis - currentStepMillis <--> accumMillis = currentStepMillis
				hooks.onBeforeThink();
				
				applicationContext.think(mutableTic);
				if(scene != null && isCurrentScene(scene)) {
					scene.think(mutableTic);
					atLeastOneThink = true;
				} else {
					skippdedThink = true;
				}
				hooks.onAfterThink();
				thinkTime.stop();
				
				if(skippdedThink) {
					// Measured thinkTime not valid for next loop
					thinkTime.set(updateTimeMillis);
				}
			} else if(updates > 0) {
				double millisBudget = currentStepMillis - renderAndInputTimeMillis;
				updateLoop: while(updates > 0) {
					boolean skippdedThink = false;
					
					thinkTime.start();
					accumMillis -= currentStepMillis;
					hooks.onBeforeThink();
					applicationContext.think(stepSizedTick);
					if(scene != null && isCurrentScene(scene)) {
						scene.think(stepSizedTick);
						atLeastOneThink = true;
					} else {
						skippdedThink = true;
					}
					hooks.onAfterThink();
					thinkTime.stop();
					
					millisBudget -= thinkTime.readoutMillis();
					
					if(skippdedThink) {
						// Measured thinkTime not valid for next loop
						thinkTime.set(updateTimeMillis);
					}
					
					if(millisBudget <= 0) {
						break updateLoop;
					}
					
					updates--;
				}
			}
		}
		if(!currentSceneHasHadOneThink) {
			if(!atLeastOneThink && isCurrentScene(scene)) {
				scene.think(stepSizedTick); // Ensure at least one think
			}
			currentSceneHasHadOneThink = true;
		}
		renderTime.start();
		hooks.onBeforeDraw();
		if(scene != null) {
			IDrawCycle drawCycle = this.newDrawCycle();
			hooks.onBeforeSceneDraw(drawCycle);
			scene.draw(drawCycle);
			hooks.onAfterSceneDraw(drawCycle);
			closeDrawCycle();
		}
		hooks.onAfterDraw();
		renderTime.stop();
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

	protected boolean isCurrentScene(IScene scene) {
		return scene == getCurrentScene();
	}

	protected abstract IScene getCurrentScene();
	
	protected abstract IDrawCycle newDrawCycle(); //drawCycle.reset()
	protected abstract void closeDrawCycle(); //drawCycle.getSpriteBatch().end()
	
	protected double millisTime() {
		return MillisTimeProvider.millisTime();
	}
	
	protected boolean isAllowVariableStepSize() {
		return true;
	}
	
	private class TimeMeasurer {
		private double start = 0;
		private double end = 8; // 60 fps -> 16.6666 millis step. halfStep ~= 8
		private boolean running = false;
		
		public void start() {
			start = millisTime();
			running = true;
		}
		
		public void set(double millis) {
			end = start + millis;
		}

		public void stop() {
			end = millisTime();
			running = false;
		}
		
		public double readoutMillis() {
			double millis = running ? millisTime() : end;
			millis -= start;
			return millis;
		}
	}
}
