package com.github.systeminvecklare.badger.core.graphics.framework.engine;


public class SceneManager {
	private static ISceneManager sceneManager;
	
	public static ISceneManager get() {
		return sceneManager;
	}
	
	public static void set(ISceneManager sceneManager) {
		SceneManager.sceneManager = sceneManager;
	}
}
