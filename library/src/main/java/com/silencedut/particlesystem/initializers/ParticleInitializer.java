package com.silencedut.particlesystem.initializers;


import com.silencedut.particlesystem.Particle;

import java.util.Random;

public interface ParticleInitializer {

	void initParticle(Particle p, Random r);

}
