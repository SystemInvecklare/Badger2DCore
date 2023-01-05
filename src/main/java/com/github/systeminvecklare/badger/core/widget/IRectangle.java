package com.github.systeminvecklare.badger.core.widget;

public interface IRectangle {
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	
	public static final IRectangleInterface<IRectangle> INTERFACE = new IRectangleInterface<IRectangle>() {
		@Override
		public int getX(IRectangle rectangle) {
			return rectangle.getX();
		}
		
		@Override
		public int getY(IRectangle rectangle) {
			return rectangle.getY();
		}
		
		@Override
		public int getWidth(IRectangle rectangle) {
			return rectangle.getWidth();
		}
		
		@Override
		public int getHeight(IRectangle rectangle) {
			return rectangle.getHeight();
		}
	};
}
