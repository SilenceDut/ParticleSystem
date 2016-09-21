package com.silencedut.particlesystem;

import java.util.List;

/**
 * Created by SilenceDut on 16/9/19.
 */

public class AnimatorType {


    /**
     * img : someimg.png
     * position : [0,0]
     * actions : [{"actionType":"move","delay":2000,"duration":1000,"relative":"to","position":[30,30]},{"actionType":"scale","delay":2000,"duration":1000,"relative":"to","position":[30,30]}]
     */

    private String img;
    private List<Integer> position;
    private List<ActionsEntity> actions;

    public void setImg(String img) {
        this.img = img;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public void setActions(List<ActionsEntity> actions) {
        this.actions = actions;
    }

    public String getImg() {
        return img;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public List<ActionsEntity> getActions() {
        return actions;
    }

    public static class ActionsEntity {
        /**
         * actionType : move
         * delay : 2000
         * duration : 1000
         * relative : to
         * position : [30,30]
         */

        private String actionType;
        private int delay;
        private int duration;
        private String relative;
        private List<Integer> position;

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setRelative(String relative) {
            this.relative = relative;
        }

        public void setPosition(List<Integer> position) {
            this.position = position;
        }

        public String getActionType() {
            return actionType;
        }

        public int getDelay() {
            return delay;
        }

        public int getDuration() {
            return duration;
        }

        public String getRelative() {
            return relative;
        }

        public List<Integer> getPosition() {
            return position;
        }
    }
}
