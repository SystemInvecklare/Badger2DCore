package com.github.systeminvecklare.badger.core.graphics.components.core;

import com.github.systeminvecklare.badger.core.graphics.components.shader.IShader;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;

public interface IDrawCycle {
	public ITransform getTransform();
	public void setShader(IShader shader);
}
