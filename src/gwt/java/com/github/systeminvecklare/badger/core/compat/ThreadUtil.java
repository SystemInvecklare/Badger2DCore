package com.github.systeminvecklare.badger.core.compat;

public class ThreadUtil {
	private static final IThread SINGLE_THREAD = new IThread() {
	};
	
	public static IThread currentThread() {
		return SINGLE_THREAD;
	}
}
