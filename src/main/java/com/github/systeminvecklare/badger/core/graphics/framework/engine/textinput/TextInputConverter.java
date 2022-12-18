package com.github.systeminvecklare.badger.core.graphics.framework.engine.textinput;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyPressListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyReleaseListener;
import com.github.systeminvecklare.badger.core.graphics.components.core.IKeyTypedListener;
import com.github.systeminvecklare.badger.core.graphics.components.scene.IScene;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ILoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyPressEvent;
import com.github.systeminvecklare.badger.core.standard.input.keyboard.IKeyReleaseEvent;

public class TextInputConverter {
	private final ISmartList<ITextInputListener> listeners = FlashyEngine.get().newSmartList();
	private final State state = new State();
	private final StateTracker SHIFT = new StateTracker();
	private final StateTracker CONTROL = new StateTracker();
	private final IKeyTypedListener keyTypedListener = new IKeyTypedListener() {
		@Override
		public void onKeyTyped(char c) {
//			System.out.println((int) c);
			
			int intChar = (int) c; 
			
			if(intChar == 8) { //Backspace
				state.remove(true);
			} else if(intChar == 127) { //Delete
				state.remove(false);
			} else if(intChar == 10) {
				if(onEnter()) {
					state.write("\n");
				}
			} else if(intChar == 3) {
				state.copy(false);
			} else if(intChar == 24) {
				state.copy(true);
			} else if(intChar == 22) {
				String pasted = FlashyEngine.get().pasteFromClipboard();
				if(pasted != null) {
					state.write(pasted);
				}
			} else if(intChar == 1) {
				state.selectAll();
			} else {
				state.write(String.valueOf(c));
			}
			
			state.sendChangeEvents(listeners);
		}
	};
	private final IKeyPressListener keyPressListener = new IKeyPressListener() {
		@Override
		public void onKeyPress(IKeyPressEvent event) {
			int keyCode = event.getKeyCode();
			if(keyCode == 37) { //Left
				state.moveCaret(false, SHIFT.isDown(), CONTROL.isDown());
			} else if(keyCode == 39) { //Right
				state.moveCaret(true, SHIFT.isDown(), CONTROL.isDown());
			} else if(keyCode == 16) {
				SHIFT.onDown(event);
			} else if(keyCode == 17) {
				CONTROL.onDown(event);
			} else if(keyCode == 35) { //end
				state.moveToEndOfLine(SHIFT.isDown());
			} else if(keyCode == 36) { //home
				state.moveToStartOfLine(SHIFT.isDown());
			} else {
				//TODO add support for up (38) and down (40)
				//     Should change row, keep offset from row left, and clamp caret position
				
//				System.out.println(keyCode);
			}
			
			state.sendChangeEvents(listeners);
		}
	};
	
	/**
	 * @return true if we should allow newline
	 */
	protected boolean onEnter() {
		return true;
	}
	

	protected boolean isShift(int keyCode) {
		return keyCode == 16;
	}

	
	public void setText(CharSequence text) {
		state.setText(text);
		state.sendChangeEvents(listeners);
	} 
	

	public void setCaretPosition(int position) {
		state.setCaretPos(position);
		state.sendChangeEvents(listeners);
	}
	
	public void setCaretPosition(int startPos, int endPos) {
		if(endPos < startPos) {
			setCaretPosition(endPos, startPos);
		} else {
			state.caretStart.set(startPos);
			state.caretEnd.set(endPos);
			state.sendChangeEvents(listeners);
		}
	}
	

	public void forceFireTextChanged() {
		listeners.forEach(state.textChanged);
	}
	
	public void forceFireCaretChanged() {
		listeners.forEach(state.caretChanged);
	}
	
	public ITextInputState getCurrentState() {
		return state;
	}
	
	public void addTextInputListener(ITextInputListener listener) {
		listeners.addToBirthList(listener);
	}
	
	public void removeTextInputListener(ITextInputListener listener) {
		listeners.addToDeathList(listener);
	}
	
	private class State implements ITextInputState {
		public final ILoopAction<ITextInputListener> textChanged = new ILoopAction<ITextInputListener>() {
			@Override
			public boolean onIteration(ITextInputListener value) {
				value.onTextChanged(text.getAll());
				return true;
			}
		};
		public final ILoopAction<ITextInputListener> caretChanged = new ILoopAction<ITextInputListener>() {
			@Override
			public boolean onIteration(ITextInputListener value) {
				value.onCaretSelectionChanged(caretStart.get(), caretEnd.get());
				return true;
			}
		};
		private final ITrackedText text = new TrackedText("");
		private final ITrackedInteger caretStart = new TrackedInteger(0);
		private final ITrackedInteger caretEnd = new TrackedInteger(0);
		private int selectStart;
		
		public void write(String newInput) {
			String beginning = text.substring(0, caretStart.get());
			String end = text.substring(caretEnd.get());
			text.clear();
			text.append(beginning);
			text.append(newInput);
			text.append(end);
			setCaretPos(beginning.length()+newInput.length());
		}
		
		public void sendChangeEvents(ISmartList<ITextInputListener> listeners) {
			boolean textHasChanged = text.updateTracking();
			if(textHasChanged) {
				listeners.forEach(textChanged);
			}
			
			boolean startChanged = caretStart.updateTracking();
			boolean endChanged = caretEnd.updateTracking();
			if(startChanged || endChanged) {
				listeners.forEach(caretChanged);
			}
		}

		public void setText(CharSequence newText) {
			text.clear();
			text.append(newText);
			caretStart.set(clampCaret(caretStart.get()));
			caretEnd.set(clampCaret(caretEnd.get()));
		}

		public void selectAll() {
			caretStart.set(0);
			caretEnd.set(text.length());
		}

		public void copy(boolean cut) {
			FlashyEngine.get().copyToClipboard(text.substring(caretStart.get(), caretEnd.get()));
			if (cut) {
				setCaretPos(remove(caretStart.get(), caretEnd.get()));
			}
		}

		public void moveToEndOfLine(boolean shift) {
			int pos = caretEnd.get();
			for(; pos < text.length(); ++pos) {
				if(text.charAt(pos) == '\n') {
					break;
				}
			}
			if(shift) {
				if(!hasSelection()) {
					selectStart = caretStart.get();
				}
				if(selectStart == caretStart.get()) {
					caretEnd.set(pos);
				} else if(selectStart == caretEnd.get()) {
					caretStart.set(selectStart);
					caretEnd.set(pos);
				}
			} else {
				setCaretPos(pos);
			}
		}
		
		public void moveToStartOfLine(boolean shift) {
			int pos = caretStart.get()-1;
			for(; pos >= 0; --pos) {
				if(text.charAt(pos) == '\n') {
					pos++;
					break;
				}
			}
			pos = clampCaret(pos);
			if(shift) {
				if(!hasSelection()) {
					selectStart = caretEnd.get();
				}
				if(selectStart == caretEnd.get()) {
					caretStart.set(pos);
				} else if(selectStart == caretStart.get()) {
					caretEnd.set(selectStart);
					caretStart.set(pos);
				}
			} else {
				setCaretPos(pos);
			}
		}

		public void moveCaret(boolean right, boolean shift, boolean control) {
			//TODO if control is true, move to end or beginning of next 'word'
			if(shift) {
				if(!hasSelection()) {
					selectStart = caretStart.get();
				}
				if(right) {
					if(selectStart == caretStart.get()) {
						caretEnd.set(clampCaret(caretEnd.get()+1));
					} else if(selectStart == caretEnd.get()) {
						caretStart.set(clampCaret(caretStart.get()+1));
					}
				} else {
					if(selectStart == caretEnd.get()) {
						caretStart.set(clampCaret(caretStart.get()-1));
					} else if(selectStart == caretStart.get()) {
						caretEnd.set(clampCaret(caretEnd.get()-1));
					}
				}
			} else {
//				if(control) {
//					setCaretPos(findNextWord(right));
//				} else {
					if(hasSelection()) {
						setCaretPos(right ? caretEnd.get() : caretStart.get());
					} else {
						setCaretPos(caretStart.get() + (right ? 1 : -1));
					}
//				}
			}
		}

		private int remove(int from, int to) {
			from = clampCaret(from);
			to = clampCaret(to);
			if(from == to) {
				return caretStart.get();
			}
			String beginning = text.substring(0, from);
			String end = text.substring(to);
			text.clear();
			text.append(beginning);
			text.append(end);
			return beginning.length();
		}
		
		private int clampCaret(int car) {
			return car < 0 ? 0 : (car > text.length() ? text.length() : car);
		}

		public void remove(boolean backwards) {
			if(hasSelection()) {
				setCaretPos(remove(caretStart.get(), caretEnd.get()));
			} else {
				if(backwards) {
					setCaretPos(remove(caretStart.get()-1, caretStart.get()));
				} else {
					setCaretPos(remove(caretStart.get(), caretStart.get()+1));
				}
			}
		}
		
		private void setCaretPos(int pos) {
			pos = clampCaret(pos);
			caretStart.set(pos);
			caretEnd.set(pos);
		}

		private boolean hasSelection() {
			return caretEnd.get() - caretStart.get() > 0;
		}

		@Override
		public CharSequence getText() {
			return text.getAll();
		}

		@Override
		public int getCaretStart() {
			return caretStart.get();
		}

		@Override
		public int getCaretEnd() {
			return caretEnd.get();
		}
	}

	public void addToScene(IScene scene) {
		SHIFT.state = false;
		CONTROL.state = false;
		scene.addKeyTypedListener(keyTypedListener);
		scene.addKeyPressListener(keyPressListener);
	}
	
	public void removeFromScene(IScene scene) {
		scene.removeKeyTypedListener(keyTypedListener);
		scene.removeKeyPressListener(keyPressListener);
	}
	
	private static class StateTracker implements IKeyReleaseListener {
		private boolean state = false;
		
		public boolean isDown() {
			return state;
		}
		
		public void onDown(IKeyPressEvent event) {
			state = true;
			event.addKeyReleaseListener(this);
		}
		
		@Override
		public void onRelease(IKeyReleaseEvent e) {
			state = false;
		}
	}
	
	private interface ITrackedText {
		CharSequence getAll();
		char charAt(int index);
		int length();
		void append(CharSequence string);
		void clear();
		String substring(int i);
		String substring(int i, int j);
		boolean updateTracking();
	}
	
	private static class TrackedText implements ITrackedText {
		private final StringBuilder text;
		private String lastTrackedValue;
		
		public TrackedText(CharSequence text) {
			this.text = new StringBuilder(text);
			this.lastTrackedValue = text.toString();
		}
		
		@Override
		public CharSequence getAll() {
			return text;
		}
		
		@Override
		public char charAt(int index) {
			return text.charAt(index);
		}
		
		@Override
		public int length() {
			return text.length();
		}
		
		@Override
		public void append(CharSequence string) {
			text.append(string);
		}
		
		@Override
		public void clear() {
			text.setLength(0);
		}
		
		@Override
		public String substring(int startIndex) {
			return text.substring(startIndex);
		}
		@Override
		public String substring(int startIndex, int endIndex) {
			return text.substring(startIndex, endIndex);
		}
		@Override
		public boolean updateTracking() {
			String currentvalue = text.toString();
			if(!currentvalue.equals(lastTrackedValue)) {
				lastTrackedValue = currentvalue;
				return true;
			} else {
				return false;
			}
		}
	}
	
	private interface ITrackedInteger {
		int get();
		void set(int newValue);
		boolean updateTracking();
	}
	
	private static class TrackedInteger implements ITrackedInteger {
		private int lastTrackedValue;
		private int value;

		public TrackedInteger(int value) {
			this.value = value;
			this.lastTrackedValue = value;
		}

		@Override
		public int get() {
			return value;
		}

		@Override
		public void set(int newValue) {
			this.value = newValue;
		}
		
		@Override
		public boolean updateTracking() {
			if(value != lastTrackedValue) {
				lastTrackedValue = value;
				return true;
			} else {
				return false;
			}
		}
	}
}
