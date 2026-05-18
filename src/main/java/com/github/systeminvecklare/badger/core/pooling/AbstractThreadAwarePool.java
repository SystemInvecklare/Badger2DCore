package com.github.systeminvecklare.badger.core.pooling;

/*package-protected*/ abstract class AbstractThreadAwarePool {
	private final Thread owningThread;
	
	public AbstractThreadAwarePool() {
		this.owningThread = Thread.currentThread();
	}
	
	protected void validateThread() {
		if(owningThread != Thread.currentThread()) {
			throw new RuntimeException("Thread "+Thread.currentThread()+" tried to access pool owned by "+owningThread);
		}
	}
}
