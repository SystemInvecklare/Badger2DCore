package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

public interface ITextEmbellishment {
	void draw(IDrawCycle drawCycle, float x, float y);	
	IFloatRectangle getBounds();
	float getXAdvance();
	IEmbellishmentTag getEmbellishmentTag();
}
