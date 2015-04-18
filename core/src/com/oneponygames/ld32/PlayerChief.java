package com.oneponygames.ld32;

import java.io.IOException;

/**
 * Created by Icewind on 18.04.2015.
 */
public class PlayerChief  extends BasicChief {

    @Override
    public void distributePopulation() {
        System.out.println("Do you want to train new warriors?");

        this.village.evenFarmers();
    }

    @Override
    public AutumnAction determineAutumnAction() {
        return new AutumnAction(Action.NOTHING, this.village);
    }

    @Override
    public void notifyRaidFrom(Village perpetrator, int foodStolen) {

    }
}
