package com.github.systeminvecklare.badger.core.hover;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;

public interface IHoverable {
	void onHoverChanged(boolean hovered, IReadablePosition globalHoverPos);
	boolean consumesHover();
}
