package com.oneponygames.ld32;

/**
 * Created by Icewind on 18.04.2015.
 */
public class PlayerChief  extends BasicChief {

    private final GameScreen gameScreen;

    public PlayerChief(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

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

    @Override
    public void notifySpring(int newPop) {
        if(newPop>0)
            this.gameScreen.addText("Spring has come, the land begins to bloom and your village grows by " + newPop + " people.");
        else
            this.gameScreen.addText("Spring has finally come, a hard winter ends.");
    }
}
