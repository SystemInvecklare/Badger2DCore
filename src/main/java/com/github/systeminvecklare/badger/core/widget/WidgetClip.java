package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.widget.IResizableWidget;
import com.github.systeminvecklare.badger.core.widget.IWidgetClip;

public class WidgetClip extends MovieClip implements IResizableWidget, IWidgetClip {
	private int width;
	private int height;
	
	public WidgetClip(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public int getX() {
		return IWidgetClip.WIDGET_INTERFACE.getX(this);
	}

	@Override
	public int getY() {
		return IWidgetClip.WIDGET_INTERFACE.getY(this);
	}

	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}

	@Override
	public void setPosition(int x, int y) {
		IWidgetClip.WIDGET_INTERFACE.setPosition(this, x , y);
	}

	@Override
	public void addToPosition(int dx, int dy) {
		IWidgetClip.WIDGET_INTERFACE.addToPosition(this, dx, dy);
	}
}
