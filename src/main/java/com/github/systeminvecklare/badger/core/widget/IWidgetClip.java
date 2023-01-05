package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.components.movieclip.IMovieClip;
import com.github.systeminvecklare.badger.core.widget.IWidgetInterface.IWidgetInterfaceExtension;

public interface IWidgetClip extends IMovieClip {
	int getWidth();
	int getHeight();
	
	public static IRectangleInterface<IWidgetClip> RECTANGLE_INTERFACE = new IRectangleInterface<IWidgetClip>() {
		@Override
		public int getX(IWidgetClip rectangle) {
			return IWidget.Util.getMovieClipX(rectangle);
		}

		@Override
		public int getY(IWidgetClip rectangle) {
			return IWidget.Util.getMovieClipY(rectangle);
		}

		@Override
		public int getWidth(IWidgetClip rectangle) {
			return rectangle.getWidth();
		}

		@Override
		public int getHeight(IWidgetClip rectangle) {
			return rectangle.getHeight();
		}
		
	};
	
	public static IWidgetInterface<IWidgetClip> WIDGET_INTERFACE = IWidgetInterface.Util.createExtension(RECTANGLE_INTERFACE, new IWidgetInterfaceExtension<IWidgetClip>() {
		@Override
		public void setPosition(IWidgetClip widget, int x, int y) {
			IWidget.Util.setMovieClipPosition(widget, x, y);
		}

		@Override
		public void addToPosition(IWidgetClip widget, int dx, int dy) {
			IWidget.Util.addToMovieClipPosition(widget, dx, dy);
		}
	});
}
