package com.github.systeminvecklare.badger.core.graphics.framework.engine.gameloop;

/*package-protected*/ class MillisTimeProvider {
	static {
		setup();
	}
	
    private static double currentTimeMillisAsDouble() {
        return System.currentTimeMillis();
    }
    
	private static native void setup() /*-{
	    $wnd.__millisTimeFn = ($wnd.performance && $wnd.performance.now)
	        ? $wnd.performance.now.bind($wnd.performance)
	        : @MillisTimeProvider::currentTimeMillisAsDouble();
	}-*/;
	
	public static native double millisTime() /*-{
    	return $wnd.__millisTimeFn();
	}-*/;
}
