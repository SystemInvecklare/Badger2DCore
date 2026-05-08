package com.github.systeminvecklare.badger.core.util;

public interface IStateControlledBehavior {
	void setState(boolean currentState);
	void setTargetState(boolean targetState);
	boolean getTargetState();
}
