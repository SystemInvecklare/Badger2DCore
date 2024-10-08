package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.math.Mathf;

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
	
	// Widget util functions
	public void setAbove(IRectangle other, float alignX) {
		setAbove(other, alignX, 0);
	}
	
	public void setBelow(IRectangle other, float alignX) {
		setBelow(other, alignX, 0);
	}
	
	public void setRightOf(IRectangle other, float alignY) {
		setRightOf(other, alignY, 0);
	}
	
	public void setLeftOf(IRectangle other, float alignY) {
		setLeftOf(other, alignY, 0);
	}
	
	public void setAbove(IRectangle other, float alignX, int padding) {
		setPosition(other.getX() + (int) Mathf.lerp(alignX, 0, other.getWidth() - this.getWidth()), other.getY() + other.getHeight() + padding);
	}
	
	public void setBelow(IRectangle other, float alignX, int padding) {
		setPosition(other.getX() + (int) Mathf.lerp(alignX, 0, other.getWidth() - this.getWidth()), other.getY() - this.getHeight() - padding);
	}
	
	public void setRightOf(IRectangle other, float alignY, int padding) {
		setPosition(other.getX() + other.getWidth() + padding, other.getY() + (int) Mathf.lerp(alignY, 0, other.getHeight() - this.getHeight()));
	}
	
	public void setLeftOf(IRectangle other, float alignY, int padding) {
		setPosition(other.getX() - this.getWidth() - padding, other.getY() + (int) Mathf.lerp(alignY, 0, other.getHeight() - this.getHeight()));
	}
	
	public void setInside(IRectangle other, float alignX, float alignY) {
		setInside(other, alignX, alignY, 0);
	}
	
	public void setInside(IRectangle other, float alignX, float alignY, int padding) {
		setInside(other, alignX, alignY, padding, padding);
	}
	
	public void setInside(IRectangle other, float alignX, float alignY, int paddingX, int paddingY) {
		int newX = other.getX() + (int) Mathf.lerp(alignX, paddingX, other.getWidth() - this.getWidth() - paddingX);
		int newY = other.getY() + (int) Mathf.lerp(alignY, paddingY, other.getHeight() - this.getHeight() - paddingY);
		setPosition(newX, newY);
	}
	
	public void setTop(int top) {
		setPosition(getX(), top - getHeight());
	}
	
	public void setBottom(int bottom) {
		setPosition(getX(), bottom);
	}
	
	public void setLeft(int left) {
		setPosition(left, getY());
	}
	
	public void setRight(int right) {
		setPosition(right - getWidth(), getY());
	}
	
	public final int getLeft() {
		return getX();
	}
	
	public final int getRight() {
		return getX() + getWidth();
	}
	
	public final int getBottom() {
		return getY();
	}
	
	public final int getTop() {
		return getY() + getHeight();
	}
	
	// Resizable util functions
	public void stretchTopTo(int top) {
		setHeight(top - getY());
	}
	
	public void stretchBottomTo(int bottom) {
		setHeight(getY() - bottom + getHeight());
		setPosition(getX(), bottom);
	}
	
	public void stretchLeftTo(int left) {
		setWidth(getX() - left + getWidth());
		setPosition(left, getY());
	}
	
	public void stretchRightTo(int right) {
		setWidth(right - getX());
	}
}
