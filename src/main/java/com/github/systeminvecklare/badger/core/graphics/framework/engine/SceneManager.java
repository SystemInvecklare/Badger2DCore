package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.widget.IRectangle;

public class SceneManager {
	public static final IRectangle RECTANGLE = new IRectangle() {
		@Override
		public int getX() {
			return 0;
		}
		
		@Override
		public int getY() {
			return 0;
		}
		
		@Override
		public int getWidth() {
			return (int) sceneManager.getWidth();
		}
		
		@Override
		public int getHeight() {
			return (int) sceneManager.getHeight();
		}
	};
	private static ISceneManager sceneManager;
	
	public static ISceneManager get() {
		return sceneManager;
	}
	
	public static void set(ISceneManager sceneManager) {
		SceneManager.sceneManager = sceneManager;
	}
}
