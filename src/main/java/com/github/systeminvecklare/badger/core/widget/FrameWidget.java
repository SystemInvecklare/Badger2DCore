package com.github.systeminvecklare.badger.core.widget;

import com.github.systeminvecklare.badger.core.graphics.framework.engine.SceneManager;

public class FrameWidget extends AbstractParentWidget<AbstractParentWidget.Child<?>> implements IResizableWidget {
	public FrameWidget() {
		this(0, 0);
	}
	
	public FrameWidget(float width, float height) {
		this((int) width, (int) height);
	}
	
	public FrameWidget(int width, int height) {
		this(0, 0, width, height);
	}
	
	public FrameWidget(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public FrameWidget(IRectangle rectangle) {
		this(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public <T> FrameWidget(T rectangle, IRectangleInterface<? super T> rectangleInterface) {
		this(rectangleInterface.getX(rectangle), rectangleInterface.getY(rectangle), rectangleInterface.getWidth(rectangle), rectangleInterface.getHeight(rectangle));
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

	@Override
	public void setWidth(int width) {
		internalSetWidth(width);
	}

	@Override
	public void setHeight(int height) {
		internalSetHeight(height);
	}
	
	public <W extends IWidget> W addChild(W widget) {
		return addChild(widget, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, final ICellLayoutSettings layoutSettings) {
		getDefaultInterface(widget, new IDefaultInterfaceHandler() {
			@Override
			public <W2 extends IWidget> void onWidget(W2 widget, IWidgetInterface<W2> widgetInterface) {
				addChild(widget, widgetInterface, layoutSettings);	
			}
		});
		return widget;
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface) {
		return addChild(widget, widgetInterface, defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, (CellLayoutSettings) layoutSettings);
	}
	
	private <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, CellLayoutSettings layoutSettings) {
		children.add(new Child<W>(widget, widgetInterface, layoutSettings));
		return widget;
	}
	
	public void pack() {
		final int x = this.getX();
		final int y = this.getY();
		final int width = this.getWidth();
		final int height = this.getHeight();
		for(Child<?> child : children) {
			child.setPosition(x, y, width, height);
		}
	}
	
	public static FrameWidget sceneSized() {
		return new FrameWidget(SceneManager.get().getWidth(), SceneManager.get().getHeight());
	}
	
	public static FrameWidget sizeOf(IRectangle rectangle) {
		return new FrameWidget(rectangle.getWidth(), rectangle.getHeight());
	}
}
