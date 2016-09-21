package com.silencedut.particlesystem;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.silencedut.particlesystem.initializers.ParticleInitializer;
import com.silencedut.particlesystem.initializers.RotationSpeedInitializer;
import com.silencedut.particlesystem.initializers.SpeeddModuleAndRangeInitializer;
import com.silencedut.particlesystem.particledecorations.ParticleDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SilenceDut on 16/9/18.
 */

public class ParticleSystem {
    private ViewGroup mParentView;
    private int mMaxParticles;
    private Random mRandom;

    private ParticleField mDrawingView;

    private ArrayList<Particle> mParticles;
    private final ArrayList<Particle> mActiveParticles = new ArrayList<Particle>();
    private long mTimeToLive;
    private long mCurrentTime = 0;

    private float mParticlesPerMilisecond;
    private int mActivatedParticles;
    private long mEmitingTime;

    private List<ParticleDecoration> mDecorations;
    private List<ParticleInitializer> mInitializers;
    private ValueAnimator mAnimator;

    private float mDpToPxScale;
    private int[] mParentLocation;

    private int mEmiterXMin;
    private int mEmiterXMax;
    private int mEmiterYMin;
    private int mEmiterYMax;

    private ParticleSystem(ViewGroup parentView, int maxParticles, long timeToLive) {
        mRandom = new Random();
        mParentLocation = new int[2];

        setParentViewGroup(parentView);

        mDecorations = new ArrayList<>();
        mInitializers = new ArrayList<>();

        mMaxParticles = maxParticles;

        // Create the particles

        mParticles = new ArrayList<> ();
        mTimeToLive = timeToLive;

        DisplayMetrics displayMetrics = parentView.getContext().getResources().getDisplayMetrics();
        mDpToPxScale = (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public ParticleSystem(ViewGroup parentView, int maxParticles, Drawable drawable, long timeToLive) {
        this(parentView, maxParticles, timeToLive);

        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            for (int i=0; i<mMaxParticles; i++) {
                mParticles.add (new Particle (bitmap));
            }
        }
    }

    public ParticleSystem(Activity activity, int maxParticles, Drawable drawable, long timeToLive, int parentViewId) {
        this((ViewGroup)activity.findViewById(parentViewId), maxParticles, drawable, timeToLive);
    }

    public ParticleSystem setParentViewGroup(ViewGroup viewGroup) {
        mParentView = viewGroup;
        if (mParentView != null) {
            mParentView.getLocationInWindow(mParentLocation);
        }
        return this;
    }

    public ParticleSystem(Activity activity, int maxParticles, int drawableRedId, long timeToLive) {
        this(activity, maxParticles, activity.getResources().getDrawable(drawableRedId), timeToLive, android.R.id.content);
    }

    public float dpToPx(float dp) {
        return dp * mDpToPxScale;
    }

    public ParticleSystem addModifier(ParticleDecoration decoration) {
        mDecorations.add(decoration);
        return this;
    }

    public ParticleSystem setSpeedRange(float speedMin, float speedMax) {
        mInitializers.add(new SpeeddModuleAndRangeInitializer(dpToPx(speedMin), dpToPx(speedMax), 0, 360));
        return this;
    }

    public ParticleSystem setRotationSpeed(float rotationSpeed) {
        mInitializers.add(new RotationSpeedInitializer(rotationSpeed, rotationSpeed));
        return this;
    }

    public void oneShot(View emiter, int numParticles) {
        oneShot(emiter, numParticles, new LinearInterpolator());
    }

    /**
     * Launches particles in one Shot using a special Interpolator
     *
     * @param emiter View from which center the particles will be emited
     * @param numParticles number of particles launched on the one shot
     * @param interpolator the interpolator for the time
     */
    public void oneShot(View emiter, int numParticles, Interpolator interpolator) {
        configureEmiter(emiter, Gravity.LEFT);
        mActivatedParticles = 0;
        mEmitingTime = mTimeToLive;
        // We create particles based in the parameters
        for (int i=0; i<numParticles && i<mMaxParticles; i++) {
            activateParticle(0);
        }
        // Add a full size view to the parent view
        mDrawingView = new ParticleField(mParentView.getContext());
        mParentView.addView(mDrawingView);
        mDrawingView.setParticles(mActiveParticles);
        // We start a property animator that will call us to do the update
        // Animate from 0 to timeToLiveMax
        startAnimator(interpolator, mTimeToLive);
    }

    private void startAnimator(Interpolator interpolator, long animationTime) {
        mAnimator = ValueAnimator.ofInt(0, (int) animationTime);
        mAnimator.setDuration(animationTime);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int milliseconds = (Integer) animation.getAnimatedValue();
                onUpdate(milliseconds);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                cleanupAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                cleanupAnimation();
            }
        });
        mAnimator.setInterpolator(interpolator);
        mAnimator.start();
    }

    private void cleanupAnimation() {
        mParentView.removeView(mDrawingView);
        mDrawingView = null;
        mParentView.postInvalidate();
        mParticles.addAll(mActiveParticles);
    }

    private void configureEmiter(View emiter, int gravity) {
        // It works with an emision range
        int[] location = new int[2];
        emiter.getLocationInWindow(location);

        // Check horizontal gravity and set range
        if (hasGravity(gravity, Gravity.LEFT)) {
            mEmiterXMin = location[0] - mParentLocation[0];
            mEmiterXMax = mEmiterXMin;
        }
        else if (hasGravity(gravity, Gravity.RIGHT)) {
            mEmiterXMin = location[0] + emiter.getWidth() - mParentLocation[0];
            mEmiterXMax = mEmiterXMin;
        }
        else if (hasGravity(gravity, Gravity.CENTER_HORIZONTAL)){
            mEmiterXMin = location[0] + emiter.getWidth()/2 - mParentLocation[0];
            mEmiterXMax = mEmiterXMin;
            Log.d("ParticleSystem",mEmiterXMin+";"+mEmiterXMax+";"+(emiter.getLeft()+emiter.getMeasuredWidth()/2));
        }
        else {
            // All the range
            mEmiterXMin = location[0] - mParentLocation[0];
            mEmiterXMax = location[0] + emiter.getWidth() - mParentLocation[0];
        }

        // Now, vertical gravity and range
        if (hasGravity(gravity, Gravity.TOP)) {
            mEmiterYMin = location[1] - mParentLocation[1];
            mEmiterYMax = mEmiterYMin;
        }
        else if (hasGravity(gravity, Gravity.BOTTOM)) {
            mEmiterYMin = location[1] + emiter.getHeight() - mParentLocation[1];
            mEmiterYMax = mEmiterYMin;
        }
        else {
            if (hasGravity(gravity, Gravity.CENTER_VERTICAL)) {
                mEmiterYMin = location[1] + emiter.getHeight() / 2 - mParentLocation[1];
                mEmiterYMax = mEmiterYMin;
            } else {
                // All the range

                mEmiterYMin = location[1] - mParentLocation[1];
                mEmiterYMax = location[1] + emiter.getHeight() - mParentLocation[1];
                Log.d("ParticleSystem",mEmiterYMin+";"+mEmiterYMax+";"+emiter.getTop());
            }
        }
    }

    private boolean hasGravity(int gravity, int gravityToCheck) {
        return (gravity & gravityToCheck) == gravityToCheck;
    }

    private void activateParticle(long delay) {
        Particle p = mParticles.remove(0);
        p.init();
        // Initialization goes before configuration, scale is required before can be configured properly
        for (int i=0; i<mInitializers.size(); i++) {
            mInitializers.get(i).initParticle(p, mRandom);
        }
        int particleX = getFromRange (mEmiterXMin, mEmiterXMax);
        int particleY = getFromRange (mEmiterYMin, mEmiterYMax);
        p.configure(mTimeToLive, particleX, particleY);
        p.activate(delay, mDecorations);
        mActiveParticles.add(p);
        mActivatedParticles++;
    }

    private int getFromRange(int minValue, int maxValue) {
        if (minValue == maxValue) {
            return minValue;
        }
        return mRandom.nextInt(maxValue-minValue) + minValue;
    }

    private void onUpdate(long milliseconds) {
        while (((mEmitingTime > 0 && milliseconds < mEmitingTime)|| mEmitingTime == -1) &&
                !mParticles.isEmpty() &&
                mActivatedParticles < mParticlesPerMilisecond*milliseconds) {

            activateParticle(milliseconds);
        }

        for (int i = 0; i < mActiveParticles.size(); i++) {
            boolean active = mActiveParticles.get(i).update(milliseconds);
            if (!active) {
                Particle p = mActiveParticles.remove(i);
                i--; // Needed to keep the index at the right position
                mParticles.add(p);
            }
        }

        mDrawingView.postInvalidate();
    }

}
