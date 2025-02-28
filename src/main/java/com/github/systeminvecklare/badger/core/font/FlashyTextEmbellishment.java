package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

public class FlashyTextEmbellishment implements ITextEmbellishment {
	private final IFlashyText text;
	private final IEmbellishmentTag tag;
	
	public FlashyTextEmbellishment(IFlashyText text, IEmbellishmentTag tag) {
		this.text = text;
		this.tag = tag;
	}

	@Override
	public void draw(IDrawCycle drawCycle, float x, float y) {
		text.draw(drawCycle, x, y);
	}

	@Override
	public IFloatRectangle getBounds() {
		return text.getBounds();
	}

	@Override
	public float getXAdvance() {
		return getBounds().getWidth();
	}

	@Override
	public IEmbellishmentTag getEmbellishmentTag() {
		return tag;
	}
}
