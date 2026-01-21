package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.font.IFlashyFont;
import com.github.systeminvecklare.badger.core.font.IFlashyText;
import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.widget.AbstractWidget;

public class SimpleTextGraphics<C> extends AbstractWidget implements IMovieClipLayer {
	private final IFlashyFont<C> font;
	private final C color;
	private final String text;
	private final float scale;
	private int x = 0;
	private int y = 0;
	private IFlashyText flashyText = null;
	private ITransform utilTransform;
	
	public SimpleTextGraphics(IFlashyFont<C> font, String text, C color) {
		this(font, text, color, 1f);
	}

	public SimpleTextGraphics(IFlashyFont<C> font, String text, C color, float scale) {
		this.font = font;
		this.color = color;
		this.text = text;
		this.scale = scale;
	}
	
	@Override
	public void draw(IDrawCycle drawCycle) {
		IFlashyText flashyText = getFlashyText();
		IFloatRectangle bounds = flashyText.getBounds();
		utilTransform.setToIdentity().setScale(scale, scale).setPosition(x - bounds.getX()*scale, y - bounds.getY()*scale);
		
		//Note: Does not respect drawCycleTransform (that's ok for a moviecliplayer)
		drawCycle.getTransform().mult(utilTransform);
		flashyText.draw(drawCycle, 0, 0);
	}
	
	private IFlashyText getFlashyText() {
		if(flashyText == null) {
			flashyText = font.createText(text, color);
		}
		return flashyText;
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		return false;
	}

	@Override
	public void init() {
		this.utilTransform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
	}

	@Override
	public void dispose() {
		if(this.utilTransform != null) {
			this.utilTransform.free();
			this.utilTransform = null;
		}
	}
	
	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return (int) (getFlashyText().getBounds().getWidth()*scale);
	}

	@Override
	public int getHeight() {
		return (int) (getFlashyText().getBounds().getHeight()*scale);
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(this.x + dx, this.y + dy);
	}
}
