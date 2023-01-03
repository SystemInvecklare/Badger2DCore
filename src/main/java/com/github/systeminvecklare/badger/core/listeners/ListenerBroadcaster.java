package com.github.systeminvecklare.badger.core.listeners;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ILoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.pooling.IPool;
import com.github.systeminvecklare.badger.core.pooling.SimplePool;

public final class ListenerBroadcaster<EVENT> implements IListener<EVENT>, IListenerAttachPoint<EVENT> {
	private final ISmartList<IListener<? super EVENT>>  listeners = FlashyEngine.get().newSmartList();
	private final IPool<OnEventLoopAction<EVENT>> loopActionPool = new SimplePool<ListenerBroadcaster.OnEventLoopAction<EVENT>>(0, 3) {
		@Override
		public OnEventLoopAction<EVENT> newObject() {
			return new OnEventLoopAction<EVENT>();
		}
	};

	@Override
	public void addListener(IListener<? super EVENT> listener) {
		listeners.addToBirthList(listener);
	}

	@Override
	public void removeListener(IListener<? super EVENT> listener) {
		listeners.addToDeathList(listener);
	}
	
	public boolean hasListeners() {
		return !listeners.isEmpty();
	}

	@Override
	public void onEvent(EVENT event) {
		OnEventLoopAction<EVENT> loopAction = loopActionPool.obtain().reset(event);
		try {
			listeners.forEach(loopAction);
		} finally {
			loopActionPool.free(loopAction);
		}
	}
	
	private static class OnEventLoopAction<EVENT> implements ILoopAction<IListener<? super EVENT>> {
		private EVENT event;

		public OnEventLoopAction<EVENT> reset(EVENT event) {
			this.event = event;
			return this;
		}
		
		@Override
		public boolean onIteration(IListener<? super EVENT> value) {
			value.onEvent(event);
			return true;
		}
	}
}
