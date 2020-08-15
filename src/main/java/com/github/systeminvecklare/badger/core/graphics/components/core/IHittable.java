package com.github.systeminvecklare.badger.core.graphics.components.core;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IHittable {
	public boolean hitTest(IReadablePosition p);
}
