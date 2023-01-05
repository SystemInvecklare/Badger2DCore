package com.github.systeminvecklare.badger.core.math.simplex;

import java.util.List;

import com.github.systeminvecklare.badger.core.math.simplex.exception.NoFeasibleSolutionException;
import com.github.systeminvecklare.badger.core.math.simplex.exception.UnboundedFeasableSolutionException;

public class TabularMethodSimpleSolver implements ISimplexSolver {
	public static final ISimplexSolver INSTANCE = new TabularMethodSimpleSolver();
	
	private static boolean DEBUG = false;
	
	private TabularMethodSimpleSolver() {
	}

	@Override
	public void solve(float[] utilityFactors, List<IStandardConstraint> constraints, float[] result)
			throws NoFeasibleSolutionException, UnboundedFeasableSolutionException {
		//TODO Hmm... I think we can convert the optimal linear one to an optimal discreet by:
		//     1. Start at the optimal.
		//     2. Consider the neighbouring lattice-points. Try stepping (making one fractional value an integer) 
		//        towards the one that would increase the utility value the most. This will be the best choice, surely.
		//     3. If that point is not feasible, try stepping along the axis for the fractional value that decreases the utility value the least.
		
		
		Table table = setupTable(utilityFactors, constraints);
		while(true) {
			debugPrintTable(table);
			int firstRowWithNegativeRHS = hasNegativeConstraintRHSs(table);
			if(firstRowWithNegativeRHS == -1) {
				debugPrintln("Default case");
				int pivotColumn = findPivotColumn(table);
				if(pivotColumn == -1) {
					debugPrintln("We are done");
					setSolution(table, result);
					for(int c = 0; c < constraints.size(); ++c) {
						float[] tmp = new float[utilityFactors.length];
						constraints.get(c).getCoefs(tmp);
						float sum = 0;
						for(int varIndex = 0; varIndex < tmp.length; ++varIndex) {
							sum += tmp[varIndex]*result[varIndex];
						}
						if(sum > constraints.get(c).getMax()) {
							throw new IllegalStateException("Condition "+c+" violated");
						}
					}
					return;
				}
				int pivotRow = findPivotRow(table, pivotColumn);
				if(pivotRow == -1) {
					throw new UnboundedFeasableSolutionException();
				}
				debugPrintln("PivotRow == "+pivotRow+", PivorColumn == "+pivotColumn);
				pivot(table, pivotRow, pivotColumn);
			} else {
				debugPrintln("Invoking case 2");
				int pivotColumn = findPivotColumnCase2(table.rows[firstRowWithNegativeRHS]);
				if(pivotColumn == -1) {
					throw new NoFeasibleSolutionException();
				}
				int pivotRow = findPivotRowCase2(table, pivotColumn, firstRowWithNegativeRHS);
				debugPrintln("PivotRow == "+pivotRow+", PivorColumn == "+pivotColumn);
				pivot(table, pivotRow, pivotColumn);
			}
		}
	}
	

	private int findPivotRowCase2(Table table, int pivotColumn, int firstRowWithNegativeRHS) {
		float smallestRatio = table.rows[firstRowWithNegativeRHS].rhs/table.rows[firstRowWithNegativeRHS].values[pivotColumn];
		int pivotRow = firstRowWithNegativeRHS;	
		for(int row = 0; row < table.rows.length; ++row) {
			if(table.rows[row].rhs >= 0f && table.rows[row].values[pivotColumn] > 0) {
				float ratio = table.rows[row].rhs/table.rows[row].values[pivotColumn];
				if(ratio < smallestRatio) {
					smallestRatio = ratio;
					pivotRow = row;
				}
			}
		}
		return pivotRow;
	}

	private int findPivotColumnCase2(Row row) {
		for(int c = 0; c < row.values.length; ++c) {
			if(row.values[c] < 0) {
				return c;
			}
		}
		return -1;
	}

	/**
	 * Returns first row that is negative ( < 0) or -1 if none
	 */
	private int hasNegativeConstraintRHSs(Table table) {
		for(int r = 0; r < table.rows.length; ++r) {
			if(table.rows[r].rhs < 0f) {
				return r;
			}
		}
		return -1;
	}

	private void debugPrintTable(Table table) {
		if(DEBUG) {
			String utilPrint = debugPrintRow(table.utilityRow);
			debugPrintCopies('=', utilPrint.length());
			for(Row row : table.rows) {
				System.out.println(debugPrintRow(row));
			}
			debugPrintCopies('-', utilPrint.length());
			System.out.println(utilPrint);
			debugPrintCopies('=', utilPrint.length());
		}
	}
	
	private void debugPrintCopies(char c, int length) {
		if(DEBUG) {
			for(int i = 0; i < length; ++i) {
				System.out.print(c);
			}
			System.out.println();
		}
	}

	private String debugPrintRow(Row row) {
		if(!DEBUG) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < row.values.length; ++i) {
			if(i > 0) {
				builder.append("\t| ");
			}
			builder.append(row.values[i]);
		}
		builder.append("\t|| ").append(row.rhs);
		return builder.toString();
	}

	private void setSolution(Table table, float[] result) {
		for(int i = 0; i < result.length; ++i) {
			int forRow = isActiveVariable(table, i);
			result[i] = forRow == -1 ? 0 : table.rows[forRow].rhs;
		}
	}
	
	/**
	 * If active, returns the corresponding row index. Else -1.
	 */
	private int isActiveVariable(Table table, int i) {
		int rowEqualToOne = -1;
		for(int r = 0; r < table.rows.length; ++r) {
			float value = table.rows[r].values[i];
			if(value == 1f) {
				if(rowEqualToOne == -1) {
					rowEqualToOne = r;
				} else {
					return -1; //Multiple rows are 1
				}
			} else if(value != 0f) {
				return -1; //One row is different from 0 or 1.
			}
		}
		if(rowEqualToOne != -1) {
			if(table.utilityRow.values[i] != 0f) {
				return -1;
			}
		}
		return rowEqualToOne;
	}

	private void pivot(Table table, int pivotRow, int pivotColumn) {
		normalizePivotRow(table.rows[pivotRow], pivotColumn);
		
		//Note: Since the pivot is exactly 1f, we should not get any rounding errors here and the value should be
		//      EXACTLY 0 in all rows for pivot column
		table.utilityRow.addScaled(-table.utilityRow.values[pivotColumn], table.rows[pivotRow]);
		for(int i = 0; i < table.rows.length; ++i) {
			if(i == pivotRow) {
				continue;
			}
			table.rows[i].addScaled(-table.rows[i].values[pivotColumn], table.rows[pivotRow]);
		}
	}

	private void normalizePivotRow(Row row, int pivotColumn) {
		row.divide(row.values[pivotColumn]);
		row.values[pivotColumn] = 1f;
	}
	
	private int findPivotRow(Table table, int pivotColumn) {
		debugPrintln("Finding pivot row for column "+pivotColumn);
		int pivotIndex = -1;
		float smallestPositive = Float.MAX_VALUE;
		for(int i = 0; i < table.rows.length; ++i) {
			float denominator = table.rows[i].values[pivotColumn];
			debugPrintln("Denominator for row "+i+" was "+denominator);
			if(denominator != 0f) {
				float ratio = table.rows[i].rhs/denominator;
				debugPrintln("Ratio for row "+i+" was "+ratio);
				if(ratio > 0 && ratio < smallestPositive) {
					debugPrintln("This is positive and less than currently smallest ("+smallestPositive+")");
					pivotIndex = i;
					smallestPositive = ratio;
				}
			}
		}
		return pivotIndex;
	}

	private int findPivotColumn(Table table) {
		int pivotIndex = -1;
		float mostNegative = Float.MAX_VALUE;
		for(int i = 0; i < table.utilityRow.values.length; ++i) {
			float value = table.utilityRow.values[i];
			if(value < 0 && value < mostNegative) {
				pivotIndex = i;
				mostNegative = value;
			}
		}
		return pivotIndex;
	}

	private Table setupTable(float[] utilityFactors, List<IStandardConstraint> constraints) {
		int numberOfVariables = utilityFactors.length;
		int valuesLength = numberOfVariables + constraints.size();
		Row[] rows = new Row[constraints.size()];
		int rowIndex = 0;
		for(IStandardConstraint constraint : constraints) {
			Row row = new Row(valuesLength, constraint.getMax());
			constraint.getCoefs(row.values);
			setupSlackVar(row.values, numberOfVariables, rowIndex);
			rows[rowIndex++] = row;
		}
		Row utilityRow = new Row(valuesLength, 0);
		for(int i = 0; i < utilityFactors.length; ++i) {
			utilityRow.values[i] = -utilityFactors[i];
		}
		return new Table(rows, utilityRow);
	}

	private void setupSlackVar(float[] values, int numberOfVariables, int rowIndex) {
		for(int i = numberOfVariables; i < values.length; ++i) {
			values[i] = (i == numberOfVariables+rowIndex) ? 1 : 0;
		}
	}

	private static class Table {
		public final Row[] rows;
		public final Row utilityRow;
		
		public Table(Row[] rows, Row utilityRow) {
			this.rows = rows;
			this.utilityRow = utilityRow;
		}
	}
	
	private static class Row {
		public final float[] values;
		public float rhs;
		
		/**
		 * 
		 * @param valuesLength should be = numberOfVariable + numberOfConstraints
		 * @param f
		 */
		public Row(int valuesLength, float rhs) {
			this.values = new float[valuesLength];
			this.rhs = rhs;
		}

		public void addScaled(float factor, Row row) {
			for(int i = 0; i < values.length; ++i) {
				values[i] = values[i] + factor*row.values[i];
			}
			rhs += factor*row.rhs;			
		}

		public void divide(float value) {
			for(int i = 0; i < values.length; ++i) {
				values[i] = values[i]/value;
			}
			rhs /= value;
		}
	}
	
	private void debugPrintln(String text) {
		if(DEBUG) {
			System.out.println(text);
		}
	}
}
