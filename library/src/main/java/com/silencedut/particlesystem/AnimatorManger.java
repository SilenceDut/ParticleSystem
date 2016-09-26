package com.silencedut.particlesystem;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.logging.Logger;

/**
 * Created by SilenceDut on 16/9/19.
 */

public class AnimatorManger {
    private ViewGroup mContentView;
    private Context context;
    private AnimatorType animatorType;
    private int[] position = new int[2];
    private AnimatorSet animatorSet;
    private View targetView;
    public static final String jsonString = " {\n" +
            "        \"img\": \"someimg.png\",\n" +
            "        \"position\": [20,20],\n" +
            "        \"actions\": [\n" +
            "\n" +
            "             {\n" +
            "                \"actionType\": \"move\",\n" +
            "\t        \"delay\" : 1000,\n" +
            "\t        \"duration\" : 1000,\n" +
            "\t        \"relative\" : \"by\",\n" +
            "\t        \"position\" : [100,100]\n" +
            "              },\n" +
            "             {\n" +
            "                \"actionType\": \"scale\",\n" +
            "\t        \"delay\" : 2000,\n" +
            "\t        \"duration\" : 1000,\n" +
            "\t        \"relative\" : \"to\",\n" +
            "\t        \"position\" : [2,3]\n" +
            "              },\n" +
            "  \n" +
            "            {\n" +
            "                \"actionType\": \"rotate\",\n" +
            "\t        \"delay\" : 2000,\n" +
            "\t        \"duration\" : 1000,\n" +
            "\t        \"relative\" : \"to\",\n" +
            "\t        \"position\" : [45,45]\n" +
            "              }\n" +
            "             \n" +
            "            \n" +
            "        ]\n" +
            "        \n" +
            "    }";

    public AnimatorManger(Activity activity,View targetView) {
        this.mContentView = (ViewGroup)activity.findViewById(android.R.id.content);
        this.context = activity;
        this.targetView = targetView;
        init();
    }

    private void init() {
        try {
            animatorType = new Gson().fromJson(jsonString,AnimatorType.class);

            if(animatorType==null) {
                return;
            }

            position[0] = DimensionUtil.dipToPx(context,animatorType.getPosition().get(0));
            position[1] = DimensionUtil.dipToPx(context,animatorType.getPosition().get(1));

            animatorSet = new AnimatorSet();
            targetView.setX(position[0]);
            targetView.setY(position[1]);
            for (AnimatorType.ActionsEntity actionsEntity :animatorType.getActions() ) {
                animatorSet.playTogether(AnimatorFactory.generateAnimator(actionsEntity,targetView));
            }
        }catch (Exception e) {
            //Todo
        }

    }

    public void startAnimator() {
        if(animatorSet!=null) {
            animatorSet.start();
        }
    }


}
