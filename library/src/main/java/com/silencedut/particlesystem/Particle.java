package com.silencedut.particlesystem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.silencedut.particlesystem.particledecorations.ParticleDecoration;

import java.util.List;

/**
 * Created by SilenceDut on 16/9/18.
 */

public class Particle {
    private Bitmap mBitmap;

    public float mCurrentX;
    public float mCurrentY;

    public float mScale = 1f;
    public int mAlpha = 255;

    public float mInitialRotation = 0f;

    public float mRotationSpeed = 0f;

    public float mSpeedX = 0f;
    public float mSpeedY = 0f;

    private Matrix mMatrix;
    private Paint mPaint;

    private float mInitialX;
    private float mInitialY;

    private float mRotation;

    private long mTimeToLive;

    protected long mStartingMilisecond;

    private int mBitmapHalfWidth;
    private int mBitmapHalfHeight;

    private List<ParticleDecoration> mModifiers;


    protected Particle() {
        mMatrix = new Matrix();
        mPaint = new Paint();
    }

    public Particle(Bitmap bitmap) {
        this();
        mBitmap = bitmap;
    }

    public void init() {
        mScale = 1;
        mAlpha = 255;
    }

    public void configure(long timeToLive, float emiterX, float emiterY) {
        mBitmapHalfWidth = mBitmap.getWidth()/2;
        mBitmapHalfHeight = mBitmap.getHeight()/2;

        mInitialX = emiterX - mBitmapHalfWidth;
        mInitialY = emiterY - mBitmapHalfHeight;
        mCurrentX = mInitialX;
        mCurrentY = mInitialY;

        mTimeToLive = timeToLive;
    }

    public boolean update (long milliseconds) {
        long realMilliseconds = milliseconds - mStartingMilisecond;
        if (realMilliseconds > mTimeToLive) {
            return false;
        }
        mCurrentX = mInitialX+mSpeedX*realMilliseconds;
        mCurrentY = mInitialY+mSpeedY*realMilliseconds;
        mRotation = mInitialRotation + mRotationSpeed*realMilliseconds/1000;
        for (int i=0; i<mModifiers.size(); i++) {
            mModifiers.get(i).decorate(this, realMilliseconds);
        }
        return true;
    }

    public void draw (Canvas c) {
        mMatrix.reset();
        mMatrix.postRotate(mRotation, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postScale(mScale, mScale, mBitmapHalfWidth, mBitmapHalfHeight);
        mMatrix.postTranslate(mCurrentX, mCurrentY);
        mPaint.setAlpha(mAlpha);
        c.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    public Particle activate(long startingMilliseconds, List<ParticleDecoration> modifiers) {
        mStartingMilisecond = startingMilliseconds;
        // We do store a reference to the list, there is no need to copy, since the modifiers do not carte about states
        mModifiers = modifiers;
        return this;
    }
}
