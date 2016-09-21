package com.silencedut.particlesystem.particledecorations;

import com.silencedut.particlesystem.Particle;

/**
 * Created by SilenceDut on 16/9/18.
 */

public interface ParticleDecoration {
    void decorate(Particle particle, long timeConsumed);
}
