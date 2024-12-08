package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.math.Mathf;

public class RectangleUtil {
	public static IRectangle justSize(final IRectangle rectangle) {
		return new IRectangle() {
			@Override
			public int getY() {
				return 0;
			}
			
			@Override
			public int getX() {
				return 0;
			}
			
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
	
	public static IRectangle inset(final IRectangle source, final int inset) {
		return new IRectangle() {
			@Override
			public int getX() {
				return source.getX() + inset;
			}

			@Override
			public int getY() {
				return source.getY() + inset;
			}

			@Override
			public int getWidth() {
				return source.getWidth() - inset*2;
			}

			@Override
			public int getHeight() {
				return source.getHeight() - inset*2;
			}
		};
	}
	
	public static IRectangle outset(IRectangle source, int outset) {
		return inset(source, -outset);
	}
	
	public static IRectangle offset(final IRectangle source, final int offsetX, final int offsetY) {
		return new IRectangle() {
			@Override
			public int getX() {
				return source.getX() + offsetX;
			}
			
			@Override
			public int getY() {
				return source.getY() + offsetY;
			}
			
			@Override
			public int getWidth() {
				return source.getWidth();
			}
			
			@Override
			public int getHeight() {
				return source.getHeight();
			}
		};
	}
	
	public static <T> IRectangle rectangle(final T object, final IRectangleInterface<T> rectangleInterface) {
		return new IRectangle() {
			@Override
			public int getX() {
				return rectangleInterface.getX(object);
			}
			
			@Override
			public int getY() {
				return rectangleInterface.getY(object);
			}
			
			@Override
			public int getWidth() {
				return rectangleInterface.getWidth(object);
			}
			
			@Override
			public int getHeight() {
				return rectangleInterface.getHeight(object);
			}
		};
	}

	public static void lerp(float t, IRectangle a, IRectangle b, IResizableWidget result) {
		result.setPosition(Math.round(Mathf.lerp(t, a.getX(), b.getX())), Math.round(Mathf.lerp(t, a.getY(), b.getY())));
		result.setWidth(Math.round(Mathf.lerp(t, a.getWidth(), b.getWidth())));
		result.setHeight(Math.round(Mathf.lerp(t, a.getHeight(), b.getHeight())));
	}
}
