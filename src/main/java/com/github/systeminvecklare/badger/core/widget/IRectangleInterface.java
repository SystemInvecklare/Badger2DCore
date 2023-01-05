package com.github.systeminvecklare.badger.core.widget;

public interface IRectangleInterface<R> {
	int getX(R rectangle);
	int getY(R rectangle);
	int getWidth(R rectangle);
	int getHeight(R rectangle);
	
	public static final IRectangleInterface<IRectangle> RECTANGLE_INTERFACE = new IRectangleInterface<IRectangle>() {
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
