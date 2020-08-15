package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.util.ISmartList;
import com.github.systeminvecklare.badger.core.util.QuickArray;

public class ApplicationContext implements IApplicationContext, ILifecycleOwner {
	private ISmartList<IThinker> thinkers;
	private ISmartList<ILifecycleOwner> lifecyleOwners;
	
	public ApplicationContext() {
		this.thinkers = FlashyEngine.get().newSmartList();
		this.lifecyleOwners = FlashyEngine.get().newSmartList();
	}
	
	@Override
	public void think(ITic tic) {
		QuickArray<IThinker> array = thinkers.getUpdatedArray();
		for(int i = 0; i < array.getSize(); ++i)
		{
			array.get(i).think(tic);
		}
	}
	
	@Override
	public void addThinker(IThinker thinker)
	{
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
		QuickArray<ILifecycleOwner> array = lifecyleOwners.getUpdatedArray();
		for(int i = 0; i < array.getSize(); ++i)
		{
			array.get(i).init();
		}
	}

	@Override
	public void dispose() {
		QuickArray<ILifecycleOwner> array = lifecyleOwners.getUpdatedArray();
		for(int i = 0; i < array.getSize(); ++i)
		{
			array.get(i).dispose();
		}
		this.thinkers.clear();
		this.thinkers = null;
		this.lifecyleOwners.clear();
		this.lifecyleOwners = null;
	}
}
