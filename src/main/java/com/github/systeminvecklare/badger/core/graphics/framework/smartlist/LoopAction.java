package com.github.systeminvecklare.badger.core.graphics.framework.smartlist;

import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;

public class LoopAction {
	public static final ILoopAction<ILifecycleOwner> INIT = new ILoopAction<ILifecycleOwner>() {
		@Override
		public boolean onIteration(ILifecycleOwner lifecycleOwner) {
			lifecycleOwner.init();
			return true;
		}
	};
	public static final ILoopAction<ILifecycleOwner> DISPOSE = new ILoopAction<ILifecycleOwner>() {
		@Override
		public boolean onIteration(ILifecycleOwner lifecycleOwner) {
			lifecycleOwner.dispose();
			return true;
		}
	};
}
