package com.oneponygames.ld32;

/**
 * Created by Icewind on 18.04.2015.
 */
public class AutumnAction {
    private Action action;
    private Village target;
    private Village village;

    public AutumnAction(Action action, Village source, Village target) {
        this.action = action;
        this.target = target;
        this.village = source;
    }

    public AutumnAction(Action action, Village source) {
        this(action, source, null);
    }

    public Action getAction() {
        return action;
    }

    public Village getTarget() {
        return target;
    }

    public Village getVillage() {
        return village;
    }
}
