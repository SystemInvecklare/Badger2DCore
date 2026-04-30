package com.github.systeminvecklare.badger.core.font;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IDrawCycle;
import com.github.systeminvecklare.badger.core.graphics.components.transform.ITransform;
import com.github.systeminvecklare.badger.core.graphics.components.util.PoolableList;
import com.github.systeminvecklare.badger.core.pattern.IPatternMatch;
import com.github.systeminvecklare.badger.core.pattern.IPatternMatchHandler;
import com.github.systeminvecklare.badger.core.pattern.IPatternMatcher;
import com.github.systeminvecklare.badger.core.pattern.PatternMatcher;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.util.FloatRectangle;
import com.github.systeminvecklare.badger.core.util.IFloatRectangle;

public class EmbellishedFont<C> implements IFlashyFont<C> {
	private static final IPatternMatcher LINE_BREAK_PATTERN = PatternMatcher.simple("\n");
	
	private final IFlashyFont<C> baseFont;
	private final float capHeight;
	private final float lineAdvance;
	private final C defaultColor;
	private final List<Embellishment<C>> embellishments;

	public EmbellishedFont(IFlashyFont<C> baseFont, float capHeight, float lineAdvance, C defaultColor, List<Embellishment<C>> embellishments) {
		this.baseFont = baseFont;
		this.capHeight = capHeight;
		this.lineAdvance = lineAdvance;
		this.defaultColor = defaultColor;
		this.embellishments = embellishments;
	}

	@Override
	public float getWidth(String text) {
		return createText(text, defaultColor).getBounds().getWidth();
	}

	@Override
	public float getHeight(String text) {
		return createText(text, defaultColor).getBounds().getHeight();
	}

	@Override
	public IFloatRectangle getBounds(String text) {
		return createText(text, defaultColor).getBounds();
	}

	@Override
	public IFloatRectangle getBounds(String text, float maxWidth) {
		return createTextWrapped(text, defaultColor, maxWidth).getBounds();
	}

	@Override
	public IFloatRectangle getBoundsCentered(String text, float maxWidth) {
		return createTextWrappedCentered(text, defaultColor, maxWidth).getBounds();
	}

	@Override
	public void preloadFont() {
		baseFont.preloadFont();
		for(Embellishment<C> embellishment : embellishments) {
			embellishment.preload();
		}
	}

	@Override
	public void draw(IDrawCycle drawCycle, String text, float x, float y, C tint) {
		createText(text, tint).draw(drawCycle, x, y);
	}

	@Override
	public void drawWrapped(IDrawCycle drawCycle, String text, float x, float y, C tint, float maxWidth) {
		createTextWrapped(text, tint, maxWidth).draw(drawCycle, x, y);
	}

	@Override
	public void drawWrappedCentered(IDrawCycle drawCycle, String text, float x, float y, C tint, float maxWidth) {
		createTextWrappedCentered(text, tint, maxWidth).draw(drawCycle, x, y);
	}

	@Override
	public IFlashyText createText(String text, C tint) {
		return createTextInternal(text, tint, null, 0);
	}
	

	@Override
	public IFlashyText createTextWrapped(String text, C tint, float maxWidth) {
		return createTextInternal(text, tint, maxWidth, 0);
	}

	@Override
	public IFlashyText createTextWrappedCentered(String text, C tint, float maxWidth) {
		return createTextInternal(text, tint, maxWidth, 0.5f);
	}
	
	private IFlashyText createTextInternal(String text, C tint, Float maxWidth, float alignX) {
		ReusablePatternMatchHandler patternMatchHandler = new ReusablePatternMatchHandler();
		
		IPool<PoolableList> listPool = FlashyEngine.get().getPoolManager().getPool(PoolableList.class);
		
		PoolableList poolableTextParts = listPool.obtain();
		List<ITextPart<C>> textParts = poolableTextParts.list();
		textParts.add(new PureTextPart<C>(text));
		embellish(listPool, patternMatchHandler, textParts, tint);
		
		linebreaks(listPool, patternMatchHandler, textParts);
		
		patternMatchHandler.clear();
		
		PoolableList poolableRows = listPool.obtain();
		List<Row> rows = poolableRows.list(Row.class);
		
		
		{ // Layout
			boolean forceAddOnNextRow = false;
			Row row = new Row(listPool);
			rows.add(row);
			
			Caret caret = new Caret(0, 0, 0);
			for(int i = 0; i < textParts.size(); ++i) {
				boolean forceAddOnRow = forceAddOnNextRow;
				forceAddOnNextRow = false;
				final ITextPart<C> textPart = textParts.get(i);
				if(textPart == LINE_BREAK_PART) {
					caret = caret.linebreak();
					row = new Row(listPool);
					rows.add(row);
					continue;
				}
				OffsetFlashyText flashy = textPart.toOffsetFlashy(baseFont, tint);
				Caret oldCaret = caret;
				caret = caret.advance(textPart, flashy);
				if(maxWidth != null && caret.maxX > maxWidth) {
					// Can't fit on row!

					// First see if we can fit part of the text on row.
					if(textPart instanceof PureTextPart) {
						String textPartText = ((PureTextPart<C>) textPart).text;
						
						caret = oldCaret;
						
						PoolableList poolableRowParts = listPool.obtain();
						try {
							List<RowPart> rowParts = splitIntoRowParts(textPartText, poolableRowParts.list(RowPart.class));
							StringBuilder hypotheticalString = new StringBuilder();
							int maxParts = 0;
							findMaxParts: for(RowPart rowPart : rowParts) {
								hypotheticalString.append(rowPart.string);
								if(caret.maxX + baseFont.getWidth(hypotheticalString.toString()) <= maxWidth) {
									maxParts++;
								} else {
									break findMaxParts;
								}
							}
							if(maxParts > 0) {
								hypotheticalString.setLength(0);
								for(int rpi = 0; rpi < maxParts; ++rpi) {
									hypotheticalString.append(rowParts.remove(0).string);
								}
								ITextPart<C> fittingPart = new PureTextPart<C>(hypotheticalString.toString());
								OffsetFlashyText fittingFlashy = fittingPart.toOffsetFlashy(baseFont, tint);
								caret = caret.advance(fittingPart, fittingFlashy);
								row.append(fittingFlashy);
								
								//Remove leading whitespace on next row
								while(!rowParts.isEmpty() && rowParts.get(0).isWhitespace) {
									rowParts.remove(0);
								}
							} else if(forceAddOnRow) {
								if(!rowParts.isEmpty()) {
									ITextPart<C> forcedPart = new PureTextPart<C>(rowParts.remove(0).string);
									OffsetFlashyText forcedFlashy = forcedPart.toOffsetFlashy(baseFont, tint);
									caret = caret.advance(forcedPart, forcedFlashy);
									row.append(forcedFlashy);
									
									//Remove leading whitespace on next row
									while(!rowParts.isEmpty() && rowParts.get(0).isWhitespace) {
										rowParts.remove(0);
									}
								}
							}
							caret = caret.linebreak(); // Next row, regardless of if we manage to fit anything
							
							// Now, we can push back.
							if(!rowParts.isEmpty()) {
								hypotheticalString.setLength(0);
								for(RowPart rowPart : rowParts) {
									hypotheticalString.append(rowPart.string);
								}
								ITextPart<C> remainingPart = new PureTextPart<C>(hypotheticalString.toString());
								textParts.add(i+1, remainingPart);
								forceAddOnNextRow = true;
							}							
						} finally {
							poolableRowParts.free();
						}
					} else {
						caret = caret.linebreak();
						caret = caret.advance(textPart, flashy);
						row.append(flashy);
						row = new Row(listPool);
						rows.add(row);
					}
				} else {
					row.append(flashy);
				}
			}
		}
		
		if(alignX != 0f) {
			boolean firstRow = true;
			float maxRowWidth = 0;
			float minRowX = 0;
			for(Row row : rows) {
				if(firstRow) {
					firstRow = false;
					minRowX = row.minX;
					maxRowWidth = row.getWidth();
				} else {
					minRowX = Math.min(minRowX, row.minX);
					maxRowWidth = Math.max(maxRowWidth, row.getWidth());
				}
			}
			for(Row row : rows) {
				float offset = (maxRowWidth - row.getWidth())*alignX;
				row.offsetX(offset);
			}
		}
		
		poolableTextParts.free();
		
		CombinedFlashyText combinedFlashyText = new CombinedFlashyText(rows);
		
		for(Row row : rows) {
			row.poolableParts.free();
		}
		poolableRows.free();
		
		return combinedFlashyText;
	}

	private void embellish(IPool<PoolableList> listPool, ReusablePatternMatchHandler patternMatchHandler, List<ITextPart<C>> textParts, final C tint) {
		PoolableList poolableNewParts = listPool.obtain();
		try {
			final List<ITextPart<C>> newParts = poolableNewParts.list();
			for(int i = 0; i < textParts.size(); ++i) {
				ITextPart<C> textPart = textParts.get(i);
				if(textPart instanceof PureTextPart) {
					String text = ((PureTextPart<C>) textPart).text;
					// Find the first match for the first embellishment.
					for(final Embellishment<C> embellishment : embellishments) {
						newParts.clear();
						try {
							boolean matched = embellishment.pattern.match(text, patternMatchHandler.reset(newParts, embellishment, tint, false));
							if(matched) {
								textParts.remove(i);
								textParts.addAll(i, newParts);
								i--; // Repeat at same index
								break; // Break embellishment-loop
							}
						} finally {
							newParts.clear();
						}
					}
				}
			}
		} finally {
			poolableNewParts.free();
		}
	}
	

	private void linebreaks(IPool<PoolableList> listPool, ReusablePatternMatchHandler patternMatchHandler, List<ITextPart<C>> textParts) {
		PoolableList poolableNewParts = listPool.obtain();
		try {
			final List<ITextPart<C>> newParts = poolableNewParts.list();
			for(int i = 0; i < textParts.size(); ++i) {
				ITextPart<C> textPart = textParts.get(i);
				if(textPart instanceof PureTextPart) {
					String text = ((PureTextPart<C>) textPart).text;
					
					newParts.clear();
					try {
						boolean found = LINE_BREAK_PATTERN.match(text, patternMatchHandler.reset(newParts, null, null, true));
						if(found) {
							textParts.remove(i);
							textParts.addAll(i, newParts);
							i += newParts.size() - 1;
						}
					} finally {
						newParts.clear();
					}
				}
			}
		} finally {
			poolableNewParts.free();
		}
	}
	
	private static List<RowPart> splitIntoRowParts(String run, List<RowPart> result) {
		StringBuilder builder = new StringBuilder();
		boolean buildingWhitespace = false;
		boolean buildingText = false;
		for(char c : run.toCharArray()) {
			boolean isWhitespace = Character.isWhitespace(c);
			if(isWhitespace) {
				if(!buildingWhitespace && buildingText) {
					result.add(new RowPart(builder.toString(), false));
					builder.setLength(0);
					buildingText = false;
				}
				buildingWhitespace = true;
				builder.append(c);
			} else {
				if(!buildingText && buildingWhitespace) {
					result.add(new RowPart(builder.toString(), true));
					builder.setLength(0);
					buildingWhitespace = false;
				}
				buildingText = true;
				builder.append(c);
			}
		}
		if(builder.length() > 0) {
			result.add(new RowPart(builder.toString(), buildingWhitespace));
		}
		return result;
	}
	
	private static class RowPart {
		private final String string;
		private final boolean isWhitespace;
		
		public RowPart(String string, boolean isWhitespace) {
			this.string = string;
			this.isWhitespace = isWhitespace;
		}
	}
	
	private class Caret {
		private final float caretPos;
		private final float lineY;
		private final float maxX;
		
		public Caret(float caretPos, float lineY, float maxX) {
			this.caretPos = caretPos;
			this.lineY = lineY;
			this.maxX = maxX;
		}

		public Caret advance(ITextPart<C> textPart, OffsetFlashyText flashy) {
			flashy.setOffset(caretPos, lineY);
			float newCaretPos = caretPos + (textPart.usesWidthForXAdvance() ? flashy.getBounds().getWidth() : textPart.getXAdvance());
			float newMaxX = Math.max(maxX, flashy.getBounds().getX() + flashy.getBounds().getWidth());
			return new Caret(newCaretPos, lineY, newMaxX);
		}

		public Caret linebreak() {
			return new Caret(0, lineY - lineAdvance, 0);
		}
	}
	
	@SuppressWarnings("unchecked")
	private ITextPart<C> getLineBreakPart() {
		return LINE_BREAK_PART;
	}
	
	private interface ITextPart<C> {
		OffsetFlashyText toOffsetFlashy(IFlashyFont<C> baseFont, C tint);
		float getXAdvance();
		boolean usesWidthForXAdvance();
	}
	
	@SuppressWarnings("rawtypes")
	private static final ITextPart LINE_BREAK_PART = new ITextPart() {
		@Override
		public boolean usesWidthForXAdvance() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public OffsetFlashyText toOffsetFlashy(IFlashyFont baseFont, Object tint) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public float getXAdvance() {
			throw new UnsupportedOperationException();
		}
	};

	private static class PureTextPart<C> implements ITextPart<C> {
		public final String text;

		public PureTextPart(String text) {
			this.text = text;
		}

		@Override
		public OffsetFlashyText toOffsetFlashy(IFlashyFont<C> baseFont, C tint) {
			return new OffsetFlashyText(baseFont.createText(text, tint));
		}

		@Override
		public float getXAdvance() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean usesWidthForXAdvance() {
			return true;
		}
	}
	
	private static class EmbellishedTextPart<C> implements ITextPart<C> {
		public final ITextEmbellishment embellishment;

		public EmbellishedTextPart(ITextEmbellishment embellishment) {
			this.embellishment = embellishment;
		}

		@Override
		public OffsetFlashyText toOffsetFlashy(IFlashyFont<C> baseFont, C tint) {
			final IFloatRectangle bounds = embellishment.getBounds();
			OffsetFlashyText offsetFlashyText = new OffsetFlashyText(new IFlashyText() {
				private final IFloatRectangle shiftedBounds = new IFloatRectangle() {
					@Override
					public float getY() {
						return 0;
					}
					
					@Override
					public float getX() {
						return 0;
					}
					
					@Override
					public float getWidth() {
						return bounds.getWidth();
					}
					
					@Override
					public float getHeight() {
						return bounds.getHeight();
					}
				};

				@Override
				public List<EmbellishmentTextSegment> getEmbellishments(List<EmbellishmentTextSegment> result,
						Supplier<TransformedFlashyText> transfromedTextSupplier) {
					result.add(new EmbellishmentTextSegment(transfromedTextSupplier.get().setTo(this).translate(bounds.getX(), bounds.getY()), embellishment.getEmbellishmentTag()));
					return result;
				}
				
				@Override
				public IFloatRectangle getBounds() {
					return shiftedBounds;
				}
				
				@Override
				public void draw(IDrawCycle drawCycle, float x, float y) {
					embellishment.draw(drawCycle, x, y);
				}
			});
			offsetFlashyText.offsetForCombiningX = bounds.getX();
			offsetFlashyText.offsetForCombiningY = bounds.getY();
			return offsetFlashyText;
		}

		@Override
		public float getXAdvance() {
			return embellishment.getXAdvance();
		}

		@Override
		public boolean usesWidthForXAdvance() {
			return false;
		}
	}

	
	private static class OffsetFlashyText implements IFlashyText {
		private final IFlashyText flashyText;
		private final IFloatRectangle flashyTextBounds;
		private float dx = 0f;
		private float dy = 0f;
		private float offsetForCombiningX = 0;
		private float offsetForCombiningY = 0;
		
		private final IFloatRectangle bounds = new IFloatRectangle() {
			@Override
			public float getX() {
				return flashyTextBounds.getX() + dx;
			}
			
			@Override
			public float getY() {
				return flashyTextBounds.getY() + dy;
			}
			
			@Override
			public float getWidth() {
				return flashyTextBounds.getWidth();
			}
			
			@Override
			public float getHeight() {
				return flashyTextBounds.getHeight();
			}
		};

		public OffsetFlashyText(IFlashyText flashyText) {
			this.flashyText = flashyText;
			this.flashyTextBounds = flashyText.getBounds();
		}

		@Override
		public IFloatRectangle getBounds() {
			return bounds;
		}
		
		public void setOffset(float dx, float dy) {
			this.dx = dx;
			this.dy = dy;
		}

		@Override
		public void draw(IDrawCycle drawCycle, float x, float y) {
			flashyText.draw(drawCycle, x + dx, y + dy);
		}

		@Override
		public List<EmbellishmentTextSegment> getEmbellishments(List<EmbellishmentTextSegment> result,
				Supplier<TransformedFlashyText> transfromedTextSupplier) {
			for(EmbellishmentTextSegment segment : flashyText.getEmbellishments(result, transfromedTextSupplier)) {
				segment.transformed.translate(dx, dy);
			}
			return result;
		}
	}
	
	private static class CombinedFlashyText implements IFlashyText {
		private final IFloatRectangle bounds;
		private final List<OffsetFlashyText> parts = new ArrayList<OffsetFlashyText>();
		
		public CombinedFlashyText(List<Row> rows) {
			boolean first = true;
			float minX = 0;
			float minY = 0;
			float maxX = 0;
			float maxY = 0;
			for(Row row : rows) {
				for(OffsetFlashyText text : row.parts) {
					parts.add(text);
					IFloatRectangle bounds = text.getBounds();
					float x = bounds.getX() + text.offsetForCombiningX;
					float y = bounds.getY() + text.offsetForCombiningY;
					if(first) {
						first = false;
						minX = x;
						minY = y;
						maxX = x + bounds.getWidth();
						maxY = y + bounds.getHeight();
					} else {
						minX = Math.min(minX, x);
						minY = Math.min(minY, y);
						maxX = Math.max(maxX, x + bounds.getWidth());
						maxY = Math.max(maxY, y + bounds.getHeight());
					}
				}
			}
			
			float ddy = -maxY;
			for(OffsetFlashyText part : parts) {
				part.dy += ddy;
			}
			minY += ddy;
			maxY += ddy;
			
			this.bounds = new FloatRectangle(minX, minY, maxX - minX, maxY - minY);
		}
		
		@Override
		public IFloatRectangle getBounds() {
			return bounds;
		}

		@Override
		public void draw(IDrawCycle drawCycle, float x, float y) {
			ITransform originalTransform = FlashyEngine.get().getPoolManager().getPool(ITransform.class).obtain();
			try {
				for(IFlashyText part : parts) {
					ITransform drawcycleTransform = drawCycle.getTransform();
					originalTransform.setTo(drawcycleTransform);
					part.draw(drawCycle, x, y);
					drawCycle.getTransform().setTo(originalTransform);
				}
			} finally {
				originalTransform.free();
			}
		}

		@Override
		public List<EmbellishmentTextSegment> getEmbellishments(List<EmbellishmentTextSegment> result,
				Supplier<TransformedFlashyText> transfromedTextSupplier) {
			PoolableList poolableList = FlashyEngine.get().getPoolManager().getPool(PoolableList.class).obtain();
			List<EmbellishmentTextSegment> partResult = poolableList.list(EmbellishmentTextSegment.class);
			try {
				for(IFlashyText part : parts) {
					partResult.clear();
					part.getEmbellishments(partResult, transfromedTextSupplier);
					result.addAll(partResult);
				}
			} finally {
				poolableList.free();
			}
			return result;
		}
	}
	
	private static class Row {
		public final PoolableList poolableParts;
		public final List<OffsetFlashyText> parts;
		private float minX;
		private float maxX;

		public Row(IPool<PoolableList> pool) {
			this.poolableParts = pool.obtain();
			this.parts = poolableParts.list(OffsetFlashyText.class);
		}

		public void offsetX(float offset) {
			for(OffsetFlashyText part : parts) {
				part.dx += offset;
			}
			minX += offset;
			maxX += offset;
		}

		public float getWidth() {
			return maxX - minX;
		}

		public void append(OffsetFlashyText part) {
			IFloatRectangle bounds = part.getBounds();
			if(parts.isEmpty()) {
				minX = bounds.getX();
				maxX = bounds.getX() + bounds.getWidth();
			} else {
				minX = Math.min(minX, bounds.getX());
				maxX = Math.max(maxX, bounds.getX() + bounds.getWidth());
			}
			parts.add(part);
		}
	}
	
	private class ReusablePatternMatchHandler implements IPatternMatchHandler {
		private List<ITextPart<C>> newParts = null;
		private Embellishment<C> embellishment = null;
		private C tint = null;
		private boolean isLinebreak = false;

		public ReusablePatternMatchHandler reset(List<ITextPart<C>> newParts, Embellishment<C> embellishment, C tint, boolean isLinebreak) {
			this.newParts = newParts;
			this.embellishment = embellishment;
			this.tint = tint;
			this.isLinebreak = isLinebreak;
			return this;
		}
		
		public void clear() {
			this.newParts = null;
			this.embellishment = null;
			this.tint = null;
			this.isLinebreak = false;
		}
		
		@Override
		public void onText(String text) {
			newParts.add(new PureTextPart<C>(text));
		}
		
		@Override
		public void onPattern(IPatternMatch patternMatch) {
			if(isLinebreak) {
				newParts.add(getLineBreakPart());
			} else {
				newParts.add(new EmbellishedTextPart<C>(embellishment.create(baseFont, tint, capHeight, patternMatch)));
			}
		}
	}
}
