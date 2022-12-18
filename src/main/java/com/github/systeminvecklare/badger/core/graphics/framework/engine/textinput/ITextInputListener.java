package com.github.systeminvecklare.badger.core.graphics.framework.engine.textinput;

public interface ITextInputListener {
	void onTextChanged(CharSequence text);
	void onCaretSelectionChanged(int caretStart, int caretEnd);
}
