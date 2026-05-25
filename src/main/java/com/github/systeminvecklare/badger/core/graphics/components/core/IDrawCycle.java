package com.github.systeminvecklare.badger.core.graphics.components.core;

import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;

public interface IDrawCycle {
	public ITransform getTransform();
	public void setShader(IShader shader);
	/**
	 * Let go immediately! 
	 * <p>Valid use case example:</p>
	 * <code>drawCycle.getTransform().mult(drawCycle.borrowUtility().setToIdentity().setPosition(x,y));</code>
	 */
	public ITransform borrowUtility();
}
