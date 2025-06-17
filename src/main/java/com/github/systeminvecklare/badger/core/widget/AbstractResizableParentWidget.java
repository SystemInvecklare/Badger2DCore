package com.github.systeminvecklare.badger.core.widget;

/*package-protected*/ abstract class AbstractResizableParentWidget<C extends AbstractParentWidget.Child<?>> extends AbstractParentWidget<C> implements IResizableWidget {
	public AbstractResizableParentWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

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
		setPosition(amount, amount);
		setWidth(getWidth() - amount*2);
		setHeight(getHeight() - amount*2);
	}
	
	public void outset(int amount) {
		inset(-amount);
	}
}
