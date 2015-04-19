package com.oneponygames.ld32;

/**
 * Created by Icewind on 18.04.2015.
 */
public interface Chief {

    public void setVillage(Village village);

    public void distributePopulation();

    public AutumnAction determineAutumnAction();

    public void notifyRaidFrom(Village perpetrator, int foodStolen);

    public void notifySpring(int newPop);

    public void notifyAutumn(int food);

    public void notifyWinter(int starve);

    public void notifySummer();

    public void notifyRaidResult(Village target, int foodStolen);

    public void notifyVillageStarved(Village v);

    public int doProvideAssistance(int foodDeficit, Village village);

    public void notifyFoodAssistance(int food);
}
