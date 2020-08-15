package com.github.systeminvecklare.badger.core.particle;


public interface IParticleSystem<T> {
	public T getNewParticle();
	public void free(T particle);
}
