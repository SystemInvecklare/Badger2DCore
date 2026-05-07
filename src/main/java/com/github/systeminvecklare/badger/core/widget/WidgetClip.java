package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.MovieClip;
import com.github.systeminvecklare.badger.core.math.IReadablePosition;
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
	
	public void setSize(IRectangle rectangle) {
		setSize(rectangle.getWidth(), rectangle.getHeight());
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
	
	public void alignTo(IRectangle other, Axis axis, float align) {
		int newX = axis.pick(other.getX() + (int) Mathf.lerp(align, 0, other.getWidth() - this.getWidth()), this.getX());
		int newY = axis.pick(this.getY(), other.getY() + (int) Mathf.lerp(align, 0, other.getHeight() - this.getHeight()));
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
	
	public int getCenterX() {
		return getX() + getWidth()/2;
	}
	
	public int getCenterY() {
		return getY() + getHeight()/2;
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
	
	public void inset(int amount) {
		addToPosition(amount, amount);
		setWidth(getWidth() - amount*2);
		setHeight(getHeight() - amount*2);
	}
	
	public void inset(int horizontal, int vertical) {
		addToPosition(horizontal, vertical);
		setWidth(getWidth() - horizontal*2);
		setHeight(getHeight() - vertical*2);
	}
	
	public void insetHorizontal(int amount) {
		addToPosition(amount, 0);
		setWidth(getWidth() - amount*2);
	}
	
	public void insetVertical(int amount) {
		addToPosition(0, amount);
		setHeight(getHeight() - amount*2);
	}
	
	public void insetLeft(int amount) {
		addToPosition(amount, 0);
		setWidth(getWidth() - amount);
	}
	
	public void insetRight(int amount) {
		setWidth(getWidth() - amount);
	}
	
	public void insetTop(int amount) {
		setHeight(getHeight() - amount);
	}
	
	public void insetBottom(int amount) {
		addToPosition(0, amount);
		setHeight(getHeight() - amount);
	}
	
	public void outset(int amount) {
		inset(-amount);
	}
	
	public void outset(int horizontal, int vertical) {
		inset(-horizontal, -vertical);
	}
	
	public void outsetHorizontal(int amount) {
		insetHorizontal(-amount);
	}
	
	public void outsetVertical(int amount) {
		insetVertical(-amount);
	}
	
	public void outsetLeft(int amount) {
		insetLeft(-amount);
	}
	
	public void outsetRight(int amount) {
		insetRight(-amount);
	}
	
	public void outsetTop(int amount) {
		insetTop(-amount);
	}
	
	public void outsetBottom(int amount) {
		insetBottom(-amount);
	}
	
	public void setAt(int x, int y, float alignX, float alignY) {
		setPosition(x - Math.round(alignX*getWidth()), y - Math.round(alignY*getHeight()));
	}
	
	public void setAt(float x, float y, float alignX, float alignY) {
		setAt((int) x, (int) y, alignX, alignY);
	}
	
	public void setAt(IReadablePosition position, float alignX, float alignY) {
		setAt((int) position.getX(), (int) position.getY(), alignX, alignY);
	}
	
	public void setCenteredAt(int x, int y) {
		setAt(x, y, 0.5f, 0.5f);
	}
	
	public void setCenteredAt(float x, float y) {
		setAt((int) x, (int) y, 0.5f, 0.5f);
	}
	
	public void setCenteredAt(IReadablePosition position) {
		setAt((int) position.getX(), (int) position.getY(), 0.5f, 0.5f);
	}
}
