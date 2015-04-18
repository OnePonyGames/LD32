package com.oneponygames.ld32;

/**
 * Created by Icewind on 18.04.2015.
 */
public abstract class BasicChief implements Chief {

    protected Village village;

    @Override
    public void setVillage(Village village) {
        this.village = village;
    }


    @Override
    public void notifySpring(int newPop) {

    }
}
