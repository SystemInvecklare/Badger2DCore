package com.github.systeminvecklare.badger.core.listeners;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleManager;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;

public class SmartListener {
	private SmartListener() {
	}
	
	public static <EVENT> void attach(ILifecycleManager lifecycleManager, IListenerAttachPoint<EVENT> attachPoint, IListener<? super EVENT> listener) {
		if(listener != null && lifecycleManager != null) {
			lifecycleManager.addManagedLifecycle(new SmartListenerImpl<EVENT>(attachPoint, listener));
		}
	}
	
	private static class SmartListenerImpl<EVENT> implements ILifecycleOwner {
		private IListenerAttachPoint<EVENT> attachPoint;
		private final IListener<EVENT> safetyWrapper;
		private boolean disposed = false;
		
		public SmartListenerImpl(IListenerAttachPoint<EVENT> attachPoint, final IListener<? super EVENT> listener) {
			this.attachPoint = attachPoint;
			this.safetyWrapper = new IListener<EVENT>() {
				@Override
				public void onEvent(EVENT event) {
					if(!disposed) {
						listener.onEvent(event);
					}
				}
			};
			attachPoint.addListener(safetyWrapper);
		}

		@Override
		public void init() {
		}

		@Override
		public void dispose() {
			disposed = true;
			attachPoint.removeListener(safetyWrapper);
		}
	}
}
