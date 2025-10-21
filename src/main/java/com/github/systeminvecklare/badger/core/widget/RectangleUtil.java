package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.layer.ILayer;
import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.graphics.components.transform.NonInvertibleMatrixException;
import com.github.systeminvecklare.badger.core.math.Mathf;
import com.github.systeminvecklare.badger.core.math.Position;
import com.github.systeminvecklare.badger.core.pooling.EasyPooler;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

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
	
	public static IRectangle fromFloat(final IFloatRectangle floatRectangle) {
		return new IRectangle() {
			@Override
			public int getX() {
				return (int) floatRectangle.getX();
			}
			
			@Override
			public int getY() {
				return (int) floatRectangle.getY();
			}
			
			@Override
			public int getWidth() {
				return (int) floatRectangle.getWidth();
			}
			
			@Override
			public int getHeight() {
				return (int) floatRectangle.getHeight();
			}
		};
	}
	
	public static IRectangle inset(final IRectangle source, final int insetHorizontal, final int insetVertical) {
		return new IRectangle() {
			@Override
			public int getX() {
				return source.getX() + insetHorizontal;
			}

			@Override
			public int getY() {
				return source.getY() + insetVertical;
			}

			@Override
			public int getWidth() {
				return source.getWidth() - insetHorizontal*2;
			}

			@Override
			public int getHeight() {
				return source.getHeight() - insetVertical*2;
			}
		};
	}
	
	public static IRectangle inset(IRectangle source, int inset) {
		return inset(source, inset, inset);
	}
	
	public static IRectangle outset(IRectangle source, int outset) {
		return inset(source, -outset);
	}
	
	public static IRectangle outset(IRectangle source, int outsetHorizontal, int outsetVertical) {
		return inset(source, -outsetHorizontal, -outsetVertical);
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
	
	public static IRectangle offsetByParent(final IRectangle parent, final IRectangle child) {
		return new IRectangle() {
			@Override
			public int getX() {
				return child.getX() + parent.getX();
			}
			
			@Override
			public int getY() {
				return child.getY() + parent.getY();
			}
			
			@Override
			public int getWidth() {
				return child.getWidth();
			}
			
			@Override
			public int getHeight() {
				return child.getHeight();
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

	public static IRectangle offsetByMovieClipToLayer(final IMovieClip movieClip, final IRectangle rectangle) {
		return new IRectangle() {
			private int getXOrY(boolean x) {
				if(!movieClip.isInitialized() || movieClip.isDisposed()) {
					return 0;
				}
				EasyPooler ep = EasyPooler.obtainFresh();
				try {
					Position pos = movieClip.toGlobalPosition(ep.obtain(Position.class).setToOrigin());
					try {
						ILayer layer = movieClip.getLayer();
						if(!layer.isInitialized() || layer.isDisposed()) {
							return 0;
						}
						layer.toLocalPosition(pos);
					} catch (NonInvertibleMatrixException e) {
						return 0;
					}
					return x ? ((int) pos.getX() + rectangle.getX()) : ((int) pos.getY() + rectangle.getY());
				} finally {
					ep.freeAllAndSelf();
				}
			}
			
			@Override
			public int getX() {
				return getXOrY(true);
			}
			
			@Override
			public int getY() {
				return getXOrY(false);
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
}
