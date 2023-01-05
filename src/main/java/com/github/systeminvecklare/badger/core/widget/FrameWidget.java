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
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
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
		for(Child<?> child : children) {
			child.setPosition(this.x, this.y, this.width, this.height);
		}
	}
	
	public static FrameWidget sceneSized() {
		return new FrameWidget(SceneManager.get().getWidth(), SceneManager.get().getHeight());
	}
}
