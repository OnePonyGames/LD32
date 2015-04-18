package com.oneponygames.ld32;

/**
 * Created by Icewind on 18.04.2015.
 */
public interface Chief {

    public void setVillage(Village village);

    public void distributePopulation();

    public AutumnAction determineAutumnAction();

    public void notifyRaidFrom(Village perpetrator, int foodStolen);
}
