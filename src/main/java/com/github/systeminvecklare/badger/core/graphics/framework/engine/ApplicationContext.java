package com.github.systeminvecklare.badger.core.graphics.framework.engine;

import com.github.systeminvecklare.badger.core.graphics.components.FlashyEngine;
import com.github.systeminvecklare.badger.core.graphics.components.core.ILifecycleOwner;
import com.github.systeminvecklare.badger.core.graphics.components.core.IThinker;
import com.github.systeminvecklare.badger.core.graphics.components.core.ITic;
import com.github.systeminvecklare.badger.core.util.IQuickArray;
import com.github.systeminvecklare.badger.core.util.ISmartList;

public class ApplicationContext implements IApplicationContext, ILifecycleOwner {
	private ISmartList<IThinker> thinkers;
	private ISmartList<ILifecycleOwner> lifecyleOwners;
	
	public ApplicationContext() {
		this.thinkers = FlashyEngine.get().newSmartList();
		this.lifecyleOwners = FlashyEngine.get().newSmartList();
	}
	
	@Override
	public void think(ITic tic) {
		IQuickArray<IThinker> array = thinkers.getUpdatedArray();
		final int size = array.getSize();
		for(int i = 0; i < size; ++i)
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
		IQuickArray<ILifecycleOwner> array = lifecyleOwners.getUpdatedArray();
		final int size = array.getSize();
		for(int i = 0; i < size; ++i)
		{
			array.get(i).init();
		}
	}

	@Override
	public void dispose() {
		IQuickArray<ILifecycleOwner> array = lifecyleOwners.getUpdatedArray();
		final int size = array.getSize();
		for(int i = 0; i < size; ++i)
		{
			array.get(i).dispose();
		}
		this.thinkers.clear();
		this.thinkers = null;
		this.lifecyleOwners.clear();
		this.lifecyleOwners = null;
	}
}
