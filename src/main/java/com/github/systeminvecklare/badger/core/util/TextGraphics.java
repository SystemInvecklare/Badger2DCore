package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.font.IFlashyFont;
import com.github.systeminvecklare.badger.core.font.IFlashyText;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.widget.IWidget;

public class TextGraphics<C> implements IMovieClipLayer, IWidget {
	private IFlashyFont<C> font;
	private String text;
	private C color;
	// Where on the text the 'anchor' is. This is the relative position in a rendered text that remains at the same location regardless of text.
	private float anchorX = 0;
	private float anchorY = 1;
	private float offsetX = 0;
	private float offsetY = 0;
	private boolean hittable = false;
	
	private final CachedFlashyText<C> cachedFlashyText = new CachedFlashyText<C>();
	private final CachedBounds cachedBounds = new CachedBounds();

	public TextGraphics(IFlashyFont<C> font, String text, C color) {
		this.font = font;
		this.text = text;
		this.color = color;
	}
	
	public IFlashyFont<C> getFont() {
		return font;
	}

	public void setFont(IFlashyFont<C> font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public C getColor() {
		return color;
	}

	public void setColor(C color) {
		this.color = color;
	}
	
	public TextGraphics<C> setAnchorX(float anchorX) {
		this.anchorX = anchorX;
		return this;
	}
	
	public TextGraphics<C> setAnchorY(float anchorY) {
		this.anchorY = anchorY;
		return this;
	}
	
	public float getAnchorX() {
		return anchorX;
	}
	
	public float getAnchorY() {
		return anchorY;
	}
	
	public TextGraphics<C> setAnchor(float x, float y) {
		this.anchorX = x;
		this.anchorY = y;
		return this;
	}
	
	public float getOffsetX() {
		return offsetX;
	}

	public TextGraphics<C> setOffsetX(float offsetX) {
		this.offsetX = offsetX;
		return this;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public TextGraphics<C> setOffsetY(float offsetY) {
		this.offsetY = offsetY;
		return this;
	}
	
	public TextGraphics<C> setOffset(float x, float y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		IFlashyFont<C> font = getFont();
		String text = getText();
		IFlashyText flashyText = cachedFlashyText.getText(font, text, getColor());
		IFloatRectangle rawBounds = flashyText.getBounds();
		IFloatRectangle bounds = cachedBounds.getBounds(getAnchorX(), getAnchorY(), getOffsetX(), getOffsetY(), rawBounds);
		
		flashyText.draw(drawCycle, bounds.getX() - rawBounds.getX(), bounds.getY() - rawBounds.getY());
	}
	
	public IFloatRectangle getBounds() {
		return cachedBounds.getBounds(getAnchorX(), getAnchorY(), getOffsetX(), getOffsetY(), cachedFlashyText.getText(getFont(), getText(), getColor()).getBounds());
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		if(hittable) {
			return GeometryUtil.isInRectangle(p.getX(), p.getY(), getBounds());
		}
		return false;
	}
	
	public TextGraphics<C> makeHittable() {
		this.hittable = true;
		return this;
	}

	@Override
	public void init() {
	}

	@Override
	public void dispose() {
		font = null;
		text = null;
		color = null;
		cachedFlashyText.fontCacheKey = null;
		cachedFlashyText.textCacheKey = null;
		cachedFlashyText.tintCacheKey = null;
		cachedFlashyText.flashyText = null;
		cachedBounds.unanchoredBoundsCacheKey = null;
		cachedBounds.bounds = null;
	}
	
	private static class CachedFlashyText<C> {
		private IFlashyFont<C> fontCacheKey = null;
		private String textCacheKey = null;
		private C tintCacheKey = null;
		
		private IFlashyText flashyText = null;
		
		public IFlashyText getText(IFlashyFont<C> font, String text, C tint) {
			if(flashyText == null || !same(fontCacheKey, font) || !same(textCacheKey,text) || !same(tintCacheKey, tint)) {
				fontCacheKey = font;
				textCacheKey = text;
				tintCacheKey = tint;
				flashyText = fontCacheKey.createText(text, tint);
			}
			return flashyText;
		}
		
		private boolean same(Object a, Object b) {
			return a == null ? b == null : a.equals(b);
		}
	}
	
	@Override
	public int getX() {
		return (int) getBounds().getX();
	}
	
	@Override
	public int getY() {
		return (int) getBounds().getY();
	}
	
	@Override
	public int getWidth() {
		return (int) getBounds().getWidth();
	}
	
	@Override
	public int getHeight() {
		return (int) getBounds().getHeight();
	}
	
	@Override
	public void setPosition(int x, int y) {
		offsetX += x - getX();
		offsetY += y - getY();
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		offsetX += dx;
		offsetY += dy;
	}
	
	private static class CachedBounds {
		private float anchorXCacheKey = 0;
		private float anchorYCacheKey = 1;
		private float offsetXCacheKey = 0;
		private float offsetYCacheKey = 0;
		private IFloatRectangle unanchoredBoundsCacheKey = null;
		
		private IFloatRectangle bounds = null;
		
		public IFloatRectangle getBounds(final float anchorX, final float anchorY, final float offsetX, final float offsetY, final IFloatRectangle unanchoredBounds) {
			if(bounds == null || anchorXCacheKey != anchorX || anchorYCacheKey != anchorY || offsetXCacheKey != offsetX || offsetYCacheKey != offsetY || unanchoredBoundsCacheKey != unanchoredBounds) {
				anchorXCacheKey = anchorX;
				anchorYCacheKey = anchorY;
				offsetXCacheKey = offsetX;
				offsetYCacheKey = offsetY;
				unanchoredBoundsCacheKey = unanchoredBounds;
				bounds = new IFloatRectangle() {
					@Override
					public float getX() {
						if(anchorX == 0f) {
							return unanchoredBounds.getX() + offsetX;
						}
						return Mathf.lerp(anchorX, 0, -unanchoredBounds.getWidth()) + unanchoredBounds.getX() + offsetX;
					}
					
					@Override
					public float getY() {
						if(anchorY == 1f) {
							return unanchoredBounds.getY() + offsetY;
						}
						return Mathf.lerp(1f - anchorY, 0, unanchoredBounds.getHeight()) + unanchoredBounds.getY() + offsetY;
					}
					
					@Override
					public float getWidth() {
						return unanchoredBounds.getWidth();
					}
					
					@Override
					public float getHeight() {
						return unanchoredBounds.getHeight();
					}
				};
			}
			return bounds;
		}
	}
}
