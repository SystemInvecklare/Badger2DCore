package com.github.systeminvecklare.badger.core.widget;

import java.util.function.Consumer;

public class FlowWidget extends AbstractParentWidget<AbstractParentWidget.Child<?>> {
	private Axis layoutAxis = Axis.X;
	private Integer maxPrimaryLengthMaybe = null;
	private final GridWidget gridWidget = new GridWidget();
	private int position = 0;
	private float align = 0;
	private int rowSpacing = 0;
	private int columnSpacing = 0;

	public FlowWidget(int maxLength) {
		super(0,0,0,0);
		setMaxLength(maxLength);
	}
	
	public FlowWidget(int x, int y, int maxWidth) {
		super(x, y, 0, 0);
		setMaxLength(maxWidth);
	}
	

	public FlowWidget setRowSpacing(int rowSpacing) {
		this.rowSpacing = rowSpacing;
		return this;
	}
	
	public FlowWidget setColumnSpacing(int columnSpacing) {
		this.columnSpacing = columnSpacing;
		return this;
	}
	
	public FlowWidget setMaxLength(int maxLength) {
		this.maxPrimaryLengthMaybe = maxLength;
		return this;
	}
	
	public FlowWidget clearMaxLength() {
		this.maxPrimaryLengthMaybe = null;
		return this;
	}
	
	public FlowWidget setAlign(float align) {
		this.align = align;
		return this;
	}
	
	public Axis getLayoutAxis() {
		return layoutAxis;
	}
	
	public FlowWidget setLayoutAxis(Axis layoutAxis) {
		this.layoutAxis = layoutAxis;
		return this;
	}

	public <W extends IWidget> W addChild(W widget) {
		return addChild(widget, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, newLayoutSettings(layoutSettings));
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
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, Consumer<ICellLayoutSettings> layoutSettings) {
		return addChild(widget, widgetInterface, newLayoutSettings(layoutSettings));
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, (CellLayoutSettings) layoutSettings);
	}
	
	private <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, CellLayoutSettings layoutSettings) {
		children.add(new Child<W>(widget, widgetInterface, layoutSettings));
		return widget;
	}

	@Override
	public void setPosition(int x, int y) {
		gridWidget.setPosition(x, y);
	}

	@Override
	public void addToPosition(int dx, int dy) {
		gridWidget.addToPosition(dx, dy);			
	}

	@Override
	public int getX() {
		return gridWidget.getX();
	}

	@Override
	public int getY() {
		return gridWidget.getY();
	}

	@Override
	public int getWidth() {
		return gridWidget.getWidth();
	}

	@Override
	public int getHeight() {
		return gridWidget.getHeight();
	}
	
	private GridWidget newSection() {
		ICellLayoutSettings layoutSettings = layoutAxis.setAlign(gridWidget.newLayoutSettings(), align);
		GridWidget result = gridWidget.addChild(new GridWidget(), layoutAxis.pick(position, 0), layoutAxis.pick(0, position), layoutSettings);
		result.columnSpacing().setTo(layoutAxis.pick(columnSpacing, 0));
		result.rowSpacing().setTo(layoutAxis.pick(0, rowSpacing));
		position++;
		return result;
	}
	
	private static <W> void addChildToSection(GridWidget sectionWidget, Child<W> child, Axis layoutAxis, int secondaryPosition) {
		sectionWidget.addChild(child.widget, child.widgetInterface,  layoutAxis.pick(0, secondaryPosition), layoutAxis.pick(secondaryPosition, 0), child.layoutSettings);
	}
	
	public void pack() {
		gridWidget.clear();
		gridWidget.rowSpacing().setTo(layoutAxis.pick(rowSpacing, 0));
		gridWidget.columnSpacing().setTo(layoutAxis.pick(0, columnSpacing));
		position = 0;
		int secondaryPosition = 0;
		GridWidget sectionWidget = null;
		for(Child<?> child : children) {
			if(sectionWidget == null) {
				sectionWidget = newSection();
			}
			addChildToSection(sectionWidget, child, layoutAxis, secondaryPosition);
			sectionWidget.pack();
			if(maxPrimaryLengthMaybe != null && layoutAxis.getLength(sectionWidget) > maxPrimaryLengthMaybe.intValue()) {
				sectionWidget.removeChild(child.widget);
				sectionWidget.pack();
				sectionWidget = newSection();
				secondaryPosition = 0;
				addChildToSection(sectionWidget, child, layoutAxis, secondaryPosition);
				sectionWidget.pack();
			}
			secondaryPosition++;
		}
		gridWidget.pack();
	}
}
