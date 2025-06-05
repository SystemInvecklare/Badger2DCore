package com.github.systeminvecklare.badger.core.standard.input.mouse;

/*package-protected*/ abstract class AbstractConsumableEvent {
	private boolean consumed = false;
	
	protected void resetConsumed() {
		this.consumed = false;
	}
	
	public boolean consume() {
		boolean wasNot = !this.consumed;
		this.consumed = true;
		return wasNot;
	}

	public boolean isConsumed() {
		return consumed;
	}
}
