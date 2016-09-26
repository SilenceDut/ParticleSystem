package com.silencedut.particlesystem;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by SilenceDut on 16/9/19.
 */

public class AnimatorFactory {

    private static final String MOVE = "move";
    private static final String SCALE = "scale";
    private static final String ROTATE = "rotate";
    private static final String ALPHA = "alpha";
    private static final String TO = "to";
    public static Animator generateAnimator(AnimatorType.ActionsEntity actionEntity,View target) {
        AnimatorSet animatorSet = new AnimatorSet();

        String property = actionEntity.getActionType();
        String relative = actionEntity.getRelative();

        int[] position = new int[2];
        position[0] = actionEntity.getPosition().get(0);
        position[1] = actionEntity.getPosition().get(1);

        long duration = actionEntity.getDuration();
        long startDelay = actionEntity.getDelay();


        if(MOVE.equals(property)) {
            position[0] = DimensionUtil.dipToPx(target.getContext(),position[0]);
            position[1] = DimensionUtil.dipToPx(target.getContext(),position[1]);
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(target,TO.equals(relative)?"x":"translationX",position[0]);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(target,TO.equals(relative)?"y":"translationY",position[1]);
            animatorSet.playTogether(xAnimator,yAnimator);
            decorateAnimator(animatorSet,duration,startDelay);

        }else if(SCALE.equals(property)) {
            ObjectAnimator xScaleAnimator;
            ObjectAnimator yScaleAnimator;

            xScaleAnimator = ObjectAnimator.ofFloat(target, "scaleX",
                    (TO.equals(relative)?1.0f:target.getScaleX())*position[0]);
            yScaleAnimator = ObjectAnimator.ofFloat(target, "scaleY",
                    (TO.equals(relative)?1.0f:target.getScaleY())*position[1]);

            animatorSet.playTogether(xScaleAnimator,yScaleAnimator);
            decorateAnimator(animatorSet,duration,startDelay);

        }else if(ROTATE.equals(property)) {
            ObjectAnimator rotateAnimator;

            rotateAnimator = ObjectAnimator.ofFloat(target, "rotation",
                    (TO.equals(relative)?0.0f:target.getRotation())+position[0]);

            animatorSet.playTogether(rotateAnimator);
            decorateAnimator(animatorSet,duration,startDelay);

        }else if(ALPHA.equals(property)) {
            ObjectAnimator rotateAnimator;

            rotateAnimator = ObjectAnimator.ofFloat(target, "alpha",
                    (TO.equals(relative)?0.0f:target.getAlpha())*position[0]);

            animatorSet.playTogether(rotateAnimator);
            decorateAnimator(animatorSet,duration,startDelay);

        }

        return animatorSet;
    }

    private static void decorateAnimator(Animator animator,long duration,long startDelay) {
        animator.setDuration(duration);
        animator.setStartDelay(startDelay);
    }
}
