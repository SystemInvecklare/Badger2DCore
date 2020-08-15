package com.github.systeminvecklare.badger.core.graphics.components.layer;

import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;

public interface ITransformDependentLayerVisitor extends ILayerVisitor {
	public ITransform transform();
}
