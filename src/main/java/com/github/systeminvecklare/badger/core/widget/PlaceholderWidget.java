package com.github.systeminvecklare.badger.core.widget;

public class PlaceholderWidget implements IResizableWidget {
	private int x;
	private int y;
	private int width;
	private int height;
	
	public PlaceholderWidget() {
	}
	
	public PlaceholderWidget(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public PlaceholderWidget(IRectangle rectangle) {
		setTo(rectangle);
	}
	
	public <R> PlaceholderWidget(R rectangle, IRectangleInterface<? super R> rectangleInterface) {
		setTo(rectangle, rectangleInterface);
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void addToPosition(int dx, int dy) {
		setPosition(getX()+dx, getY()+dy);
	}
	
	@Override
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void setHeight(int height) {
		this.height = height;
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
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	public void setTo(IRectangle rectangle) {
		setPosition(rectangle.getX(), rectangle.getY());
		setSizeTo(rectangle);
	}
	
	public void setSizeTo(IRectangle rectangle) {
		setSize(rectangle.getWidth(), rectangle.getHeight());
	}
	
	public <R> void setTo(R rectangle, IRectangleInterface<? super R> rectangleInterface) {
		setPosition(rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle));
		setSizeTo(rectangle, rectangleInterface);
	}
	
	public <R> void setSizeTo(R rectangle, IRectangleInterface<? super R> rectangleInterface) {
		setSize(rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
	}
	
	public void setSize(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	
	public static IWidget trackSize(final IRectangle rectangle) {
		return new PlaceholderWidget() {
			@Override
			public int getWidth() {
				return rectangle.getWidth();
			}
			
			@Override
			public int getHeight() {
				return rectangle.getHeight();
			}
		};
	}
}
