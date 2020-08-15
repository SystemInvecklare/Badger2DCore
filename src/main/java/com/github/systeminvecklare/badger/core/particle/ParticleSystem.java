package com.github.systeminvecklare.badger.core.particle;

import java.util.LinkedList;
import java.util.List;

public abstract class ParticleSystem<T> implements IParticleSystem<T> {
	private int systemSize;
	private List<T> aliveParticles = new LinkedList<T>();
	private T[] restingParticlesStack;
	private int restingParticlesStackIndex = -1;
	
	@SuppressWarnings("unchecked")
	public ParticleSystem(int systemSize) {
		this.systemSize = systemSize;
		this.restingParticlesStack = (T[]) new Object[systemSize];
	}
	
	protected abstract T newParticle();

	@Override
	public T getNewParticle() {
		if(restingParticlesStackIndex >= 0)
		{
			T particle = restingParticlesStack[restingParticlesStackIndex--];
			aliveParticles.add(particle);
			return particle;
		}
		else
		{
			if(aliveParticles.size() >= systemSize)
			{
				T particle = resetParticle(aliveParticles.remove(0));
				aliveParticles.add(particle);
				return particle;
			}
			else
			{
				T particle = newParticle();
				aliveParticles.add(particle);
				return particle;
			}
		}
	}
	
	public abstract T resetParticle(T particle);
	
	@Override
	public void free(T particle) {
		boolean wasInAlive = aliveParticles.remove(particle);
		T freeParticle = resetParticle(particle);
		if(wasInAlive)
		{
			restingParticlesStack[++restingParticlesStackIndex] = freeParticle;
			System.out.println("Freed "+particle.hashCode());
		}
	}
}
