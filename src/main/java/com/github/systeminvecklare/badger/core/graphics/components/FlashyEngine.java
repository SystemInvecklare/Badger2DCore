package com.github.systeminvecklare.badger.core.graphics.components;

import com.github.systeminvecklare.badger.core.graphics.framework.engine.IFlashyEngine;

public class FlashyEngine {
	private static IFlashyEngine engine;
	
	private FlashyEngine() {
	}
	
	public static synchronized IFlashyEngine get() {
		return engine;
	}
	
	public static synchronized void set(IFlashyEngine engine) {
		FlashyEngine.engine = engine;
	}
}
