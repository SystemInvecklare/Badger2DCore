package com.github.systeminvecklare.badger.core.widget;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.systeminvecklare.badger.core.math.simplex.StandardFormSimplexProblem;
import com.github.systeminvecklare.badger.core.math.simplex.TabularMethodSimpleSolver;
import com.github.systeminvecklare.badger.core.math.simplex.exception.SimplexException;

public class GridWidget extends AbstractParentWidget<GridWidget.GridChild<?>> implements IWidget {
	private final SpacingSettings rowSpacing = new SpacingSettings();
	private final SpacingSettings columnSpacing = new SpacingSettings();

	public GridWidget() {
	}

	public GridWidget(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public <W extends IWidget> W addChild(W widget, int row, int column) {
		return addChild(widget, row, column, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, final int row, final int column, final ICellLayoutSettings layoutSettings) {
		getDefaultInterface(widget, new IDefaultInterfaceHandler() {
			@Override
			public <W2 extends IWidget> void onWidget(W2 widget, IWidgetInterface<W2> widgetInterface) {
				addChild(widget, widgetInterface, row, column, layoutSettings);
			}
		});
		return widget;
	}
	
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column) {
		return addChild(widget, widgetInterface, row, column, defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, row, column, 1, 1, layoutSettings);
	}
	
	public <W extends IWidget> W addChild(W widget, int row, int column, int occupiedRows,
			int occupiedColumns) {
		return addChild(widget, row, column, occupiedRows, occupiedColumns, defaultLayoutSettings());
	}
	
	public <W extends IWidget> W addChild(W widget, final int row, final int column, final int occupiedRows,
			final int occupiedColumns, final ICellLayoutSettings layoutSettings) {
		getDefaultInterface(widget, new IDefaultInterfaceHandler() {
			@Override
			public <W2 extends IWidget> void onWidget(W2 widget, IWidgetInterface<W2> widgetInterface) {
				addChild(widget, widgetInterface, row, column, occupiedRows, occupiedColumns, layoutSettings);
			}
		});
		return widget;
	}
	
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column, int occupiedRows,
			int occupiedColumns) {
		return addChild(widget, widgetInterface, row, column, occupiedRows, occupiedColumns, defaultLayoutSettings());
	}
	
	public <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column, int occupiedRows,
			int occupiedColumns, ICellLayoutSettings layoutSettings) {
		return addChild(widget, widgetInterface, row, column, occupiedRows, occupiedColumns, (CellLayoutSettings) layoutSettings);
	}
	
	private <W> W addChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column, int occupiedRows,
			int occupiedColumns, CellLayoutSettings layoutSettings) {
		children.add(new GridChild<W>(widget, widgetInterface, row, column, occupiedRows, occupiedColumns, layoutSettings));
		return widget;
	}
	
	public void insertRows(int rowIndex, int numberOfRows) {
		for(GridChild<?> child : children) {
			if(child.row >= rowIndex) {
				child.row += numberOfRows;
			}
		}
		rowSpacing.insert(rowIndex, numberOfRows);
	}
	
	public void insertColumns(int columnIndex, int numberOfColumns) {
		for(GridChild<?> child : children) {
			if(child.column >= columnIndex) {
				child.column += numberOfColumns;
			}
		}
		columnSpacing.insert(columnIndex, numberOfColumns);
	}
	
	public void pack() {
		int maxRowIndex = -1;
		int maxColumnIndex = -1;
		for(GridChild<?> child : children) {
			maxRowIndex = Math.max(maxRowIndex, child.getMaxRowIndex());
			maxColumnIndex = Math.max(maxColumnIndex, child.getMaxColumnIndex());
		}
		if(maxRowIndex < 0 || maxColumnIndex < 0) {
			return;
		}
		int[] rowHeights = new int[maxRowIndex+1];
		int[] columnWidths = new int[maxColumnIndex+1];
		calculateWidthsAndHeights(columnWidths, rowHeights);
		
		int[] offsetX = new int[maxColumnIndex+1];
		int[] offsetY = new int[maxRowIndex+1];
		offsetX[0] = this.getX();
		offsetY[maxRowIndex] = this.getY();
		for(int column = 1; column < maxColumnIndex+1; ++column) {
			offsetX[column] = offsetX[column-1]+columnWidths[column-1]+columnSpacing.getSpacing(column-1);
		}
		for(int row = maxRowIndex-1; row >= 0; --row) {
			offsetY[row] = offsetY[row+1]+rowHeights[row+1]+rowSpacing.getSpacing(row);
		}
		this.width = offsetX[maxColumnIndex]+columnWidths[maxColumnIndex]-offsetX[0];
		this.height = offsetY[0]+rowHeights[0]-offsetY[maxRowIndex];
		for(GridChild<?> child : children) {
			child.setPosition(offsetX[child.column], offsetY[child.getMaxRowIndex()], child.getAvailableSpaceX(columnWidths, columnSpacing), child.getAvailableSpaceY(rowHeights, rowSpacing));
		}
	}
	
	private void calculateWidthsAndHeights(int[] columnWidths, int[] rowHeights) {
		//TODO only run simplex for columns/rows that need it.
		float[] widthTargetCoefs = new float[columnWidths.length];
		Arrays.fill(widthTargetCoefs, -1);
		float[] heightTargetCoefs = new float[rowHeights.length];
		Arrays.fill(heightTargetCoefs, -1);
		StandardFormSimplexProblem widthSimplexProblem = new StandardFormSimplexProblem(widthTargetCoefs);
		StandardFormSimplexProblem heightSimplexProblem = new StandardFormSimplexProblem(heightTargetCoefs);
		for(GridChild<?> child : children) {
			//Old non-simplex method
//			int remainingHeight = child.getHeight();
//			for(int dr = 0; dr < child.occupiedRows; ++dr) {
//				int childHeightPart = remainingHeight/(child.occupiedRows - dr);
//				remainingHeight -= childHeightPart;
//				rowHeights[child.row-dr] = Math.max(rowHeights[child.row-dr], childHeightPart);
//			}
//			int remainingWidth = child.getWidth();
//			for(int dc = 0; dc < child.occupiedColumns; ++dc) {
//				int childWidthPart = remainingWidth/(child.occupiedColumns - dc);
//				remainingWidth -= childWidthPart;
//				columnWidths[child.column+dc] = Math.max(columnWidths[child.column+dc], childWidthPart);
//			}
			{
				float[] coefs = new float[heightTargetCoefs.length];
				for(int dr = 0; dr < child.occupiedRows; ++dr) {
					coefs[child.row+dr] = -1;
				}
				int unaccountedHeight = child.getHeight();
				for(int i = 0; i < child.occupiedRows-1; ++i) {
					unaccountedHeight -= rowSpacing.getSpacing(child.row+i);
				}
				heightSimplexProblem.addConstraint(coefs).rhs(-unaccountedHeight);
			}
			
			{
				float[] coefs = new float[widthTargetCoefs.length];
				for(int dc = 0; dc < child.occupiedColumns; ++dc) {
					coefs[child.column+dc] = -1;
				}
				int unaccountedWidth = child.getWidth();
				for(int i = 0; i < child.occupiedColumns-1; ++i) {
					unaccountedWidth -= columnSpacing.getSpacing(child.column+i);
				}
				widthSimplexProblem.addConstraint(coefs).rhs(-unaccountedWidth);
			}
		}
		try {
			float[] widthResult = widthSimplexProblem.solve(TabularMethodSimpleSolver.INSTANCE);
			for(int i = 0; i < widthResult.length; ++i) {
				columnWidths[i] = (int) widthResult[i];
			}
			float[] heightResult = heightSimplexProblem.solve(TabularMethodSimpleSolver.INSTANCE);
			for(int i = 0; i < heightResult.length; ++i) {
				rowHeights[i] = (int) heightResult[i];
			}
		} catch(SimplexException e) {
			throw new RuntimeException(e);
		}
	}

	protected static class GridChild<W> extends AbstractParentWidget.Child<W> {
		private int row;
		private int column;
		private final int occupiedRows;
		private final int occupiedColumns;
		
		public GridChild(W widget, IWidgetInterface<? super W> widgetInterface, int row, int column, int occupiedRows,
				int occupiedColumns, CellLayoutSettings layoutSettings) {
			super(widget, widgetInterface, layoutSettings);
			this.row = row;
			this.column = column;
			this.occupiedRows = occupiedRows;
			this.occupiedColumns = occupiedColumns;
		}
		
		public int getAvailableSpaceX(int[] columnWidths, SpacingSettings spacingSettings) {
			if(occupiedColumns == 1) {
				return columnWidths[column];
			}
			int sum = 0;
			for(int dc = 0; dc < occupiedColumns; ++dc) {
				sum += columnWidths[column+dc];
				if(dc > 0) {
					sum += spacingSettings.getSpacing(column+dc-1);
				}
			}
			return sum;
		}

		public int getAvailableSpaceY(int[] rowHeights, SpacingSettings spacingSettings) {
			if(occupiedRows == 1) {
				return rowHeights[row];
			}
			int sum = 0;
			for(int dr = 0; dr < occupiedRows; ++dr) {
				sum += rowHeights[row+dr];
				if(dr > 0) {
					sum += spacingSettings.getSpacing(row+dr-1);
				}
			}
			return sum;
		}

		public int getMaxRowIndex() {
			return row + occupiedRows - 1;
		}
		
		public int getMaxColumnIndex() {
			return column + occupiedColumns - 1;
		}
	}

	public ISpacingSettings columnSpacing() {
		return columnSpacing;
	}
	
	public ISpacingSettings rowSpacing() {
		return rowSpacing;
	}
	
	private static class SpacingSettings implements ISpacingSettings {
		private int amount = 0;
		private final Map<Integer, Integer> overrides = new HashMap<Integer, Integer>();

		@Override
		public void setTo(int amount) {
			this.amount = amount;
		}

		public void insert(int index, int length) {
			if(!overrides.isEmpty()) {
				Map<Integer, Integer> old = new HashMap<Integer, Integer>(overrides);
				overrides.clear();
				for(Entry<Integer, Integer> entry : old.entrySet()) {
					int newIndex = entry.getKey();
					if(newIndex >= index) {
						newIndex += length;
					}
					overrides.put(newIndex, entry.getValue());
				}
			}
		}

		@Override
		public void reset() {
			this.amount = 0;
			this.overrides.clear();
		}

		@Override
		public ISpacingSettingOverride forIndex(int index) {
			return new IndexOverride(index);
		}
		
		public int getSpacing(int index) {
			Integer spacingOverride = overrides.get(index);
			return spacingOverride != null ? spacingOverride : amount;
		}
		
		private class IndexOverride implements ISpacingSettings.ISpacingSettingOverride {
			private final int index;
			
			public IndexOverride(int index) {
				this.index = index;
			}

			@Override
			public void setTo(int amount) {
				overrides.put(index, amount);
			}

			@Override
			public void useDefault() {
				overrides.remove(index);
			}
		}
	}
}
