package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

public interface IFlashyFont<C> {
	float getWidth(String text);
	float getHeight(String text);
	IFloatRectangle getBounds(String text);
	IFloatRectangle getBounds(String text, float maxWidth);
	IFloatRectangle getBoundsCentered(String text, float maxWidth);
	void preloadFont();
	void draw(IDrawCycle drawCycle, String text, float x, float y, C tint);
	void drawWrapped(IDrawCycle drawCycle, String text, float x, float y, C tint, float maxWidth);
	void drawWrappedCentered(IDrawCycle drawCycle, String text, float x, float y, C tint, float maxWidth);
}
