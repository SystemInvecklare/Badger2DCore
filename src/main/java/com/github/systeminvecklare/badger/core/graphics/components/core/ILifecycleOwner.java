package com.github.systeminvecklare.badger.core.graphics.components.core;

public interface ILifecycleOwner {
	/**
	 * The contract of <code>init()</code> is that it is called after in has been called on the parent. 
	 */
	public void init();
	public void dispose();
}
