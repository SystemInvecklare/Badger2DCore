package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

public interface IFlashyText {
	IFloatRectangle getBounds();
	void draw(IDrawCycle drawCycle, float x, float y);
}
