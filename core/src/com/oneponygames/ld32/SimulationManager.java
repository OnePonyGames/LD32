package com.oneponygames.ld32;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Icewind on 18.04.2015.
 */
public class SimulationManager implements Runnable {

    private static final float MAX_POP_GROWTH = 0.2f;
    private static final double STD_DEV_FACT = 0.075;
    private static final double USUAL_PRODUCTION = 1.0;
    private static final double MAX_FOOD_STEAL = 0.33;

    private final List<Village> villages;
    private final List<AutumnAction> autumnActions;
    private final int yearsToPlay;
    private final Random rand;
    private final GameScreen gameScreen;

    public SimulationManager(List<Village> villages, GameScreen gameScreen, int yearsToPlay) {
        this.villages = villages;
        this.yearsToPlay = yearsToPlay;
        this.autumnActions = new ArrayList<AutumnAction>();
        this.gameScreen = gameScreen;

        this.rand = new Random();
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        for(int i=0;i<this.yearsToPlay;i++) {
            System.out.println("\nStarting Year: "+i);

            for(Season s : Season.values()) {
                System.out.println("Season " + s);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.gameScreen.setSeason(s);

                for(Village v : this.villages)  {
                    this.resolveSeason(s, v);
                }

                switch(s) {
                    case  AUTUMN:
                        for(AutumnAction a : this.autumnActions) {
                            switch (a.getAction()) {
                                case RAID:
                                    this.resolveRaid(a.getVillage(), a.getTarget());
                                    break;
                                case ASSISTANCE:
                                    this.resolveAssistance(a.getVillage(), a.getTarget());
                                    break;
                                case NOTHING:
                                    System.out.println(a.getVillage().getName() + " do nothing.");
                                    break;
                            }
                        }

                        this.autumnActions.clear();
                    break;
                    case WINTER:
                        for(int j=0;j<this.villages.size();j++) {
                            Village v = this.villages.get(j);
                            if(v.getPopulation() == 0) {
                                this.villages.remove(j);
                                j--;

                                for(Village v1 : this.villages)  {
                                    v1.removeNeighbor(v);
                                    if(!v1.equals(v))
                                        v1.getChief().notifyVillageStarved(v);
                                }
                            }
                        }
                        break;
                }

            }
        }
    }

    private void resolveAssistance(Village village, Village target) {
        int food = target.getChief().doProvideAssistance(village.getFoodDeficit(), village);

        System.out.println("The people of "+target.getName()+ " provide the people of "+village.getName()+" with "+food+" food" );

        village.addFood(food);
        target.removeFood(food);

        village.getChief().notifyFoodAssistance(food);
    }

    private void resolveSpring(Village v) {
        int newPop =0;
        if(!v.haveStarved()) {
            double val = 0.1 + 1 - v.getFood() / v.getPopulation();
            double fact = Math.min(val, MAX_POP_GROWTH);
            newPop = Math.max(1, (int) (v.getPopulation() * fact));

            System.out.println("New population for " + v.getName() + " "+newPop );
            v.addPopulation(newPop);
        }
        v.getChief().notifySpring(newPop);
        v.setFood(0);

        v.getChief().distributePopulation();
        System.out.println("W/F: "+v.getWarriors()+"/"+v.getFarmers());
    }

    private void resolveAutumn(Village v) {
        double fact = USUAL_PRODUCTION + this.rand.nextGaussian() * STD_DEV_FACT;
        
        int food = Math.max(0, (int) (v.getFarmers() * fact));
        System.out.println(v.getName() + " produced "+food+" for "+v.getPopulation()+" people fr: "+v.getFoodRequirement());

        v.setFood(food);
        v.getChief().notifyAutumn(food);

        this.autumnActions.add(v.getChief().determineAutumnAction());
    }

    private void resolveRaid(Village perpetrator, Village target) {
        int foodStolen = Math.max(0, (perpetrator.getWarriors() - target.getWarriors()) * 2);
        foodStolen = Math.min(foodStolen, (int) (target.getFood() * MAX_FOOD_STEAL));

        perpetrator.addFood(foodStolen);
        target.removeFood(foodStolen);

        target.getChief().notifyRaidFrom(perpetrator, foodStolen);
        perpetrator.getChief().notifyRaidResult(target, foodStolen);

        System.out.println(perpetrator.getName() + " are raiding " + target.getName() + " and stole " + foodStolen + " food");
    }

    private void resolveWinter(Village v) {
        v.setStarved(false);
        int starve = 0;
        if(!v.hasEnoughtFood()) {
            starve = v.getFoodDeficit();
            System.out.println(v.getName() + " starving: "+starve);
            v.starvePopulation( starve);
        }
        v.getChief().notifyWinter(starve);
    }

    private void resolveSummer(Village v) {
        v.getChief().notifySummer();
    }

    private void resolveSeason(Season s, Village v) {
        switch (s) {
            case SPRING: this.resolveSpring(v);
                break;
            case SUMMER: this.resolveSummer(v);
                break;
            case AUTUMN: this.resolveAutumn(v);
                break;
            case WINTER: this.resolveWinter(v);
                break;
        }
    }
}
