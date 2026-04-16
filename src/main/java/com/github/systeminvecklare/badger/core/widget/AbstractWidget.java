package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.math.IReadablePosition;
import com.github.systeminvecklare.badger.core.math.Mathf;

public abstract class AbstractWidget implements IWidget {
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
	
	public void setLeft(int left) {
		setPosition(left, getY());
	}
	
	public void setRight(int right) {
		setPosition(right - getWidth(), getY());
	}
	
	public void setBottom(int bottom) {
		setPosition(getX(), bottom);
	}
	
	public void setTop(int top) {
		setPosition(getX(), top - getHeight());
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
	
	public final int getCenterX() {
		return getX() + getWidth()/2;
	}
	
	public final int getCenterY() {
		return getY() + getHeight()/2;
	}
}
