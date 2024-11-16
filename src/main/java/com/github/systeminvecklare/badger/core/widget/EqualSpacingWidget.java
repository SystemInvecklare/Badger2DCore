package com.github.systeminvecklare.badger.core.widget;

import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;

public class EqualSpacingWidget extends AbstractParentWidget<EqualSpacingWidget.Child<?>> implements IWidget, IResizableWidget {
	private Axis layoutAxis;
	private int outerPaddingLeft = 0;
	private int outerPaddingRight = 0;
	private int outerPaddingTop = 0;
	private int outerPaddingBottom = 0;
	
	public EqualSpacingWidget(Axis layoutAxis) {
		this(layoutAxis, 0, 0);
	}

	public EqualSpacingWidget(Axis layoutAxis, int width, int height) {
		super(0, 0, width, height);
		this.layoutAxis = layoutAxis;
	}
	
	public EqualSpacingWidget(Axis layoutAxis, IRectangle rectangle) {
		this(layoutAxis, rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
	}
	
	public EqualSpacingWidget(Axis layoutAxis, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.layoutAxis = layoutAxis;
	}
	
	@Override
	public void setWidth(int width) {
		internalSetWidth(width);
	}
	
	@Override
	public void setHeight(int height) {
		internalSetHeight(height);
	}
	
	public void setTo(IRectangle rectangle) {
		setPosition(rectangle.getX(), rectangle.getY());
		setWidth(rectangle.getWidth());
		setHeight(rectangle.getHeight());
	}
	
	public Axis getLayoutAxis() {
		return layoutAxis;
	}
	
	public void setLayoutAxis(Axis layoutAxis) {
		this.layoutAxis = layoutAxis;
	}
	
	private int nextIndex() {
		int nextIndex = 0;
		for(Child<?> child : children) {
			nextIndex = Math.max(nextIndex, child.index + 1);
		}
		return nextIndex;
	}
	
	public <W extends IWidget> W addChild(W widget) {
		return addChild(widget, nextIndex());
	}
	
	public <W extends IWidget> W addChild(W widget, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, nextIndex(), layoutSettings);
	}
	
	public <W extends IWidget> W addChild(W widget, final ICellLayoutSettings layoutSettings) {
		return addChild(widget, nextIndex(), layoutSettings);
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface) {
		return addChild(widget, widgetInterface, nextIndex());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, widgetInterface, nextIndex(), layoutSettings);
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, nextIndex(), layoutSettings);
	}
	
	public <W extends IWidget> W addChild(W widget, int index) {
		return addChild(widget, index, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, final int index, final Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, index, newLayoutSettings(layoutSettings));
	}
	
	public <W extends IWidget> W addChild(W widget, final int index, final ICellLayoutSettings layoutSettings) {
		getDefaultInterface(widget, new IDefaultInterfaceHandler() {
			@Override
			public <W2 extends IWidget> void onWidget(W2 widget, IWidgetInterface<W2> widgetInterface) {
				addChild(widget, widgetInterface, index, layoutSettings);
			}
		});
		return widget;
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int index) {
		return addChild(widget, widgetInterface, index, defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int index, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, widgetInterface, index, newLayoutSettings(layoutSettings));
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int index, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, index, (CellLayoutSettings) layoutSettings);
	}
	
	private <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int index, CellLayoutSettings layoutSettings) {
		children.add(new Child<W>(widget, widgetInterface, index, layoutSettings));
		return widget;
	}
	
	public void pack() {
		int numberOfChildren = children.size();
		if(numberOfChildren > 1) {
			Collections.sort(children, Child.INDEX_COMPARATOR);
		
			int spaceLeft = layoutAxis.pick(getWidth(), getHeight()) - (getOuterPaddingStart(layoutAxis) + getOuterPaddingEnd(layoutAxis));
			
			for(Child<?> child : children) {
				spaceLeft -= child.getTotalSize(layoutAxis);
			}
			
			Axis secondaryAxis = layoutAxis.other();
			
			int primaryPos = layoutAxis.pick(getX(), getY()) + getOuterPaddingStart(layoutAxis);
			int secondaryPos = secondaryAxis.pick(getX(), getY()) + getOuterPaddingStart(secondaryAxis);
			//TODO optimize all these pickings
			int secondarySpace = secondaryAxis.pick(getWidth(), getHeight()) - (getOuterPaddingStart(secondaryAxis)+getOuterPaddingEnd(secondaryAxis));
		
			int numberOfSpaces = numberOfChildren - 1;
			int spaceBetweenEach = spaceLeft / numberOfSpaces;
			int mod = spaceLeft % numberOfSpaces;
			
			
			int overflow = 0;
			for(Child<?> child : children) {
				child.setPosition(layoutAxis, primaryPos, secondaryPos, child.getSize(layoutAxis), secondarySpace);
				
				primaryPos += child.getSize(layoutAxis);
				primaryPos += spaceBetweenEach;
				if(mod > 0) {
					overflow += mod;
					if(overflow >= numberOfSpaces) {
						primaryPos += 1;
						overflow -= numberOfSpaces;
					}
				}
			}
		} else if(numberOfChildren == 1) {
			children.get(0).setPosition(getX() + outerPaddingLeft, getY() + outerPaddingBottom, getWidth() - outerPaddingLeft - outerPaddingRight, getHeight() - outerPaddingBottom - outerPaddingTop);
		}
	}
	
	private int getOuterPaddingStart(Axis axis) {
		switch (axis) {
			case X: return outerPaddingLeft;
			case Y: return outerPaddingBottom;
		}
		throw new RuntimeException();
	}
	
	private int getOuterPaddingEnd(Axis axis) {
		switch (axis) {
			case X: return outerPaddingRight;
			case Y: return outerPaddingTop;
		}
		throw new RuntimeException();
	}

	protected static class Child<W> extends AbstractParentWidget.Child<W> {
		protected static final Comparator<Child<?>> INDEX_COMPARATOR = new Comparator<Child<?>>() {
			@Override
			public int compare(Child<?> o1, Child<?> o2) {
				return Integer.compare(o1.index, o2.index);
			}
		};
		
		private int index;
		
		public Child(W widget, IWidgetInterface<? super W> widgetInterface, int index, CellLayoutSettings layoutSettings) {
			super(widget, widgetInterface, layoutSettings);
			this.index = index;
		}
		
		public void setPosition(Axis primaryAxis, int primaryPos, int secondaryPos, int primaryAvailableSpace, int secondaryAvailableSpace) {
			if(primaryAxis == Axis.X) {
				this.setPosition(primaryPos, secondaryPos, primaryAvailableSpace, secondaryAvailableSpace);
			} else {
				this.setPosition(secondaryPos, primaryPos, secondaryAvailableSpace, primaryAvailableSpace);
			}
		}

		public int getPaddingStart(Axis axis) {
			switch (axis) {
				case X: return this.layoutSettings.paddingLeft;
				case Y: return this.layoutSettings.paddingBottom;
			}
			throw new RuntimeException();
		}
		
		public int getPaddingEnd(Axis axis) {
			switch (axis) {
				case X: return this.layoutSettings.paddingRight;
				case Y: return this.layoutSettings.paddingTop;
			}
			throw new RuntimeException();
		}

		public int getTotalSize(Axis axis) {
			return getSize(axis) + getPaddingStart(axis) + getPaddingEnd(axis);
		}

		public int getSize(Axis axis) {
			switch (axis) {
				case X: return this.getWidth();
				case Y: return this.getHeight();
			}
			throw new RuntimeException();
		}
	}

	public void setOuterPadding(int padding) {
		setOuterPadding(padding, padding, padding, padding);
	}
	
	public void setOuterPadding(int horizontal, int vertical) {
		setOuterPadding(horizontal, horizontal, vertical, vertical);
	}
	
	public void setOuterPadding(int left, int right, int top, int bottom) {
		setOuterPaddingLeft(left);
		setOuterPaddingRight(right);
		setOuterPaddingTop(top);
		setOuterPaddingBottom(bottom);
	}
	
	public void setOuterPaddingHorizontal(int padding) {
		setOuterPaddingHorizontal(padding, padding);
	}
	
	public void setOuterPaddingHorizontal(int left, int right) {
		setOuterPaddingLeft(left);
		setOuterPaddingRight(right);
	}
	
	public void setOuterPaddingVertical(int padding) {
		setOuterPaddingVertical(padding, padding);
	}
	
	public void setOuterPaddingVertical(int top, int bottom) {
		setOuterPaddingTop(top);
		setOuterPaddingBottom(bottom);
	}
	
	public void setOuterPaddingLeft(int left) {
		this.outerPaddingLeft = left;
	}
	
	public void setOuterPaddingRight(int right) {
		this.outerPaddingRight = right;
	}
	
	public void setOuterPaddingTop(int top) {
		this.outerPaddingTop = top;
	}
	
	public void setOuterPaddingBottom(int bottom) {
		this.outerPaddingBottom = bottom;
	}
}
