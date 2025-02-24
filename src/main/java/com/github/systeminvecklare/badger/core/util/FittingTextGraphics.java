package com.github.systeminvecklare.badger.core.util;

import com.github.systeminvecklare.badger.core.font.IFlashyFont;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.moviecliplayer.IMovieClipLayer;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.PlaceholderWidget;

public class FittingTextGraphics<C> implements IRectangle, IMovieClipLayer {
	private final TextGraphics<C> textGraphics;
	private IRectangle fitRectangle;
	private float alignX = 0.5f;
	private float alignY = 0.5f;
	
	private final CachedScaleAndBounds<C> scaleAndBounds = new CachedScaleAndBounds<C>();
	private IFlashyFont<C> font;
	private String text;
	
	public FittingTextGraphics(IFlashyFont<C> font, String text, C color, IRectangle fitRectangle) {
		this.font = font;
		this.text = text;
		this.textGraphics = new TextGraphics<C>(font, text, color) {
			@Override
			public String getText() {
				return FittingTextGraphics.this.getText();
			}
			
			@Override
			public IFlashyFont<C> getFont() {
				return FittingTextGraphics.this.getFont();
			}
		};
		this.textGraphics.setAnchor(0.5f, 0.5f);
		this.fitRectangle = fitRectangle;
	}
	
	public FittingTextGraphics<C> setAlignX(float alignX) {
		this.alignX = alignX;
		return this;
	}
	
	public FittingTextGraphics<C> setAlignY(float alignY) {
		this.alignY = alignY;
		return this;
	}
	
	public FittingTextGraphics<C> setAlign(float alignX, float alignY) {
		return setAlignX(alignX).setAlignY(alignY);
	}
	
	public FittingTextGraphics<C> setColor(C color) {
		textGraphics.setColor(color);
		return this;
	}
	
	public IFlashyFont<C> getFont() {
		return font;
	}
	
	public String getText() {
		return text;
	}
	
	public FittingTextGraphics<C> setFont(IFlashyFont<C> font) {
		this.font = font;
		return this;
	}
	
	public FittingTextGraphics<C> setText(String text) {
		this.text = text;
		return this;
	}
	
	public FittingTextGraphics<C> setFitRectangle(IRectangle rectangle) {
		this.fitRectangle = rectangle;
		return this;
	}
	
	public IRectangle getFitRectangle() {
		return fitRectangle;
	}
	
	public float getAlignX() {
		return alignX;
	}
	
	public float getAlignY() {
		return alignY;
	}
	
	private void refreshFit() {
		scaleAndBounds.refresh(textGraphics, getFitRectangle(), getAlignX(), getAlignY());
	}

	@Override
	public void draw(IDrawCycle drawCycle) {
		refreshFit();
		EasyPooler ep = EasyPooler.obtainFresh();
		try {
			ITransform drawCycleTransform = drawCycle.getTransform();
			ITransform original = drawCycleTransform.copy(ep);
			drawCycleTransform.mult(ep.obtain(ITransform.class).setToIdentity().addToPosition(scaleAndBounds.bounds.getCenterX(), scaleAndBounds.bounds.getCenterY()).multiplyScale(scaleAndBounds.scale));
			textGraphics.draw(drawCycle);
			drawCycleTransform.setTo(original);
		} finally {
			ep.freeAllAndSelf();
		}
	}

	@Override
	public boolean hitTest(IReadablePosition p) {
		refreshFit();
		float scaledWidth = textGraphics.getWidth()*scaleAndBounds.scale;
		float scaledHeight = textGraphics.getHeight()*scaleAndBounds.scale;
		int x = Math.round(scaleAndBounds.bounds.getCenterX() - (scaledWidth*0.5f));
		int y = Math.round(scaleAndBounds.bounds.getCenterY() - (scaledHeight*0.5f));
		return GeometryUtil.isInRectangle(p.getX(), p.getY(), x, y, (int) scaledWidth, (int) scaledHeight);
	}

	@Override
	public void init() {
		textGraphics.init();
	}

	@Override
	public void dispose() {
		textGraphics.dispose();
	}

	@Override
	public int getX() {
		refreshFit();
		return Math.round(scaleAndBounds.bounds.getCenterX() - (textGraphics.getWidth()*scaleAndBounds.scale*0.5f));
	}

	@Override
	public int getY() {
		refreshFit();
		return Math.round(scaleAndBounds.bounds.getCenterY() - (textGraphics.getHeight()*scaleAndBounds.scale*0.5f));
	}

	@Override
	public int getWidth() {
		refreshFit();
		return (int) (textGraphics.getWidth()*scaleAndBounds.scale);
	}

	@Override
	public int getHeight() {
		refreshFit();
		return (int) (textGraphics.getHeight()*scaleAndBounds.scale);
	}
	
	private static class CachedScaleAndBounds<C> {
		private int textWidthCacheKey;
		private int textHeightCacheKey;
		private float alignXCacheKey;
		private float alignYCacheKey;
		private PlaceholderWidget fitRectangelCacheKey = null;
		
		private float scale = 1;
		private final PlaceholderWidget bounds = new PlaceholderWidget();
		
		public void refresh(TextGraphics<C> textGraphics, IRectangle fitRectangle, float alignX, float alignY) {
			if(fitRectangle == null) {
				return;
			}
			if(fitRectangelCacheKey == null || alignX != alignXCacheKey || alignY != alignYCacheKey || !equal(fitRectangelCacheKey, fitRectangle) || textGraphics.getWidth() != textWidthCacheKey || textGraphics.getHeight() != textHeightCacheKey) {
				if(fitRectangelCacheKey == null) {
					fitRectangelCacheKey = new PlaceholderWidget();
				}
				textWidthCacheKey = textGraphics.getWidth();
				textHeightCacheKey = textGraphics.getHeight();
				alignXCacheKey = alignX;
				alignYCacheKey = alignY;
				fitRectangelCacheKey.setTo(fitRectangle);
				
				scale = textGraphics.findScaleToFitInside(fitRectangelCacheKey);
				bounds.setSize(Math.round(textWidthCacheKey*scale), Math.round(textHeightCacheKey*scale));
				bounds.setInside(fitRectangelCacheKey, alignX, alignY);
			}
		}
		
		private static boolean equal(IRectangle a, IRectangle b) {
			return a.getX() == b.getX() && a.getY() == b.getY() && a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight();
		}
	}
}
