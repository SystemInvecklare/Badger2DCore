package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ISmartList;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.LoopAction;
import com.github.systeminvecklare.badger.core.graphics.framework.smartlist.ThinkAction;

public class ApplicationContext implements IApplicationContext, ILifecycleOwner {
	private ISmartList<IThinker> thinkers;
	private ISmartList<ILifecycleOwner> lifecyleOwners;
	private final ThinkAction thinkAction = new ThinkAction(null);
	
	public ApplicationContext() {
		this.thinkers = FlashyEngine.get().newSmartList();
		this.lifecyleOwners = FlashyEngine.get().newSmartList();
	}
	
	@Override
	public void think(ITic tic) {
		thinkAction.setTic(tic);
		thinkers.forEach(thinkAction);
	}
	
	@Override
	public void addThinker(IThinker thinker) {
		thinkers.addToBirthList(thinker);
		if(thinker instanceof ILifecycleOwner)
		{
			lifecyleOwners.addToBirthList((ILifecycleOwner) thinker);
		}
	}
	
	@Override
	public void removeThinker(IThinker thinker) {
		thinkers.addToDeathList(thinker);
		if(thinker instanceof ILifecycleOwner)
		{
			lifecyleOwners.addToDeathList((ILifecycleOwner) thinker);
		}		
	}

	@Override
	public void init() {
		lifecyleOwners.forEach(LoopAction.INIT);
	}

	@Override
	public void dispose() {
		lifecyleOwners.forEach(LoopAction.DISPOSE);
		this.thinkers.clear();
		this.thinkers = null;
		this.lifecyleOwners.clear();
		this.lifecyleOwners = null;
	}
}
