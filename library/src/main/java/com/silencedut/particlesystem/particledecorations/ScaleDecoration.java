package com.silencedut.particlesystem.particledecorations;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.silencedut.particlesystem.Particle;

public class ScaleDecoration implements ParticleDecoration {

	private float mInitialValue;
	private float mFinalValue;
	private long mEndTime;
	private long mStartTime;
	private long mDuration;
	private float mValueIncrement;
	private Interpolator mInterpolator;

	public ScaleDecoration(float initialValue, float finalValue, long startMilis, long endMilis, Interpolator interpolator) {
		mInitialValue = initialValue;
		mFinalValue = finalValue;
		mStartTime = startMilis;
		mEndTime = endMilis;
		mDuration = mEndTime - mStartTime;
		mValueIncrement = mFinalValue-mInitialValue;
		mInterpolator = interpolator;
	}
	
	public ScaleDecoration(float initialValue, float finalValue, long startMilis, long endMilis) {
		this (initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
	}
	
	@Override
	public void decorate(Particle particle, long timeConsumed) {
		if (timeConsumed < mStartTime) {
			particle.mScale = mInitialValue;
		}
		else if (timeConsumed > mEndTime) {
			particle.mScale = mFinalValue;
		}
		else {
			float value = mInterpolator.getInterpolation((timeConsumed- mStartTime)*1f/mDuration);
			float scale = mInitialValue + mValueIncrement*value;
			particle.mScale = scale;
		}
	}

}
