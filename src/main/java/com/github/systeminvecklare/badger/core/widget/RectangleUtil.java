package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.widget.IRectangle;

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
	
	public static IRectangle outset(IRectangle source, int inset) {
		return inset(source, -inset);
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
}
