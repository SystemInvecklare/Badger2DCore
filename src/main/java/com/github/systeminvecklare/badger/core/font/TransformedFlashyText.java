package com.github.systeminvecklare.badger.core.font;

import com.github.systeminvecklare.badger.core.util.FloatRectangle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;
import com.github.systeminvecklare.badger.core.widget.IRectangle;
import com.github.systeminvecklare.badger.core.widget.Rectangle;

public class TransformedFlashyText {
	public IFlashyText flashyText;
	public float dx = 0;
	public float dy = 0;
	public float scale = 1;
	
	public TransformedFlashyText() {
		this(null);
	}
	
	public TransformedFlashyText(IFlashyText flashyText) {
		this.flashyText = flashyText;
	}

	public TransformedFlashyText setTo(IFlashyText flashyText) {
		return setTo(flashyText, 0, 0, 1);
	}
	
	public TransformedFlashyText setTo(IFlashyText flashyText, float dx, float dy, float scale) {
		this.flashyText = flashyText;
		this.dx = dx;
		this.dy = dy;
		this.scale = scale;
		return this;
	}

	public TransformedFlashyText translate(float dx, float dy) {
		this.dx += dx;
		this.dy += dy;
		return this;
	}
	
	public TransformedFlashyText transform(float dx, float dy, float scale) {
		return scale(scale).translate(dx, dy);
	}
	
	public TransformedFlashyText scale(float scale) {
		this.dx *= scale;
		this.dy *= scale;
		this.scale *= scale;
		return this;
	}
	
	public IFloatRectangle getBounds() {
		IFloatRectangle bounds = flashyText.getBounds();
		float x = bounds.getX() + dx;
		float y = bounds.getY() + dy;
		float width = bounds.getWidth()*scale;
		float height = bounds.getHeight()*scale;
		return new FloatRectangle(x, y, width, height);
	}

	public IRectangle getIntBounds() {
		IFloatRectangle bounds = flashyText.getBounds();
		float x = bounds.getX() + dx;
		float y = bounds.getY() + dy;
		float width = bounds.getWidth()*scale;
		float height = bounds.getHeight()*scale;
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}
}
