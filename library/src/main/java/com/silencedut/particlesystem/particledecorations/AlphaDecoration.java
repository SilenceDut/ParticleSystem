package com.silencedut.particlesystem.particledecorations;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.silencedut.particlesystem.Particle;

public class AlphaDecoration implements ParticleDecoration {

	private int mInitialValue;
	private int mFinalValue;
	private long mStartTime;
	private long mEndTime;
	private float mDuration;
	private float mValueIncrement;
	private Interpolator mInterpolator;

	public AlphaDecoration(int initialValue, int finalValue, long startMilis, long endMilis, Interpolator interpolator) {
		mInitialValue = initialValue;
		mFinalValue = finalValue;
		mStartTime = startMilis;		
		mEndTime = endMilis;
		mDuration = mEndTime - mStartTime;
		mValueIncrement = mFinalValue-mInitialValue;
		mInterpolator = interpolator;
	}
	
	public AlphaDecoration(int initialValue, int finalValue, long startMilis, long endMilis) {
		this(initialValue, finalValue, startMilis, endMilis, new LinearInterpolator());
	}

	@Override
	public void decorate(Particle particle, long timeConsumed) {
		if (timeConsumed < mStartTime) {
			particle.mAlpha = mInitialValue;
		}
		else if (timeConsumed > mEndTime) {
			particle.mAlpha = mFinalValue;
		}
		else {	
			float animatorValue = mInterpolator.getInterpolation((timeConsumed- mStartTime)*1f/mDuration);
			int alphaValue = (int) (mInitialValue + mValueIncrement*animatorValue);
			particle.mAlpha = alphaValue;
		}		
	}

}
