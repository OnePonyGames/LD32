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
        this.gameScreen.addText("How many new warriors do you want to train?");
        int wrrs = this.gameScreen.getNumberInput(" new warriors!", 0, this.village.getFarmers());

        this.village.addWarriors(wrrs);
        this.village.evenFarmers();
    }

    @Override
    public AutumnAction determineAutumnAction() {
        return new AutumnAction(Action.NOTHING, this.village);
    }

    @Override
    public void notifyRaidFrom(Village perpetrator, int foodStolen) {
        if(foodStolen > 0)
            this.gameScreen.addText("Curses upon them! Warriors from " + perpetrator.getName() + " stole " + foodStolen + " food from your village.");
        else
            this.gameScreen.addText("Warriors from " + perpetrator.getName() + " tried to steal food, but your warriors managed to drive them away.");
    }

    @Override
    public void notifySpring(int newPop) {
        if(newPop>0)
            this.gameScreen.addText("Spring has come, the land begins to bloom and your village grows by " + newPop + " people.");
        else
            this.gameScreen.addText("Spring has finally come, a hard winter ends.");
        this.village.evenFarmers();
    }

    public void notifyAutumn(int producedFood) {
        if(producedFood > this.village.getFoodRequirement())
            this.gameScreen.addText("The harvest is brought in, there is much rejoicing as it is enough to last for the winter.");
        else
            this.gameScreen.addText("The gods cursed you this year. The harvest is too small to feed all for the winter!");

        this.gameScreen.addText("Please choose:");
        this.gameScreen.addText("1) Do nothing");
        this.gameScreen.addText("2) Ask for food");
        int max = 2;
        if(this.village.getWarriors()>0) {
            this.gameScreen.addText("3) Raid a village");
            max = 3;
        }

        int choice = this.gameScreen.getNumberInput("", 1, max);
    }

    public void notifyWinter(int starvedPeople) {
        if(starvedPeople > 0) {
            this.gameScreen.addText(starvedPeople + " people starved during the winter.");
            this.village.evenFarmers();
        } else {
            this.gameScreen.addText("Winter passes without much notice, your food reserves keep everyone fed.");
        }
    }

    public void notifySummer() {
        if(this.village.getWarriors() > 0)
            this.gameScreen.addText("Summer! Your farmers tend the crops while your warriors train for battle.");
        else
            this.gameScreen.addText("Long summer days while your farmers tend the crops awaiting the harvest to come.");
    }
}
