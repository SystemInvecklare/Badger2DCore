package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

/*package-protected*/ class MillisTimeProvider {
	public static double millisTime() {
		return System.nanoTime()/1000000.0;
	}
}
