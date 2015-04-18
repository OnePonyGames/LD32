package com.oneponygames.ld32;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Icewind on 18.04.2015.
 */
public class Strategy {

    public static Chief NO_WAR(final double warriorFact) {
        return new BasicChief() {
            @Override
            public AutumnAction determineAutumnAction() {
                return new AutumnAction(Action.NOTHING, this.village);
            }
            @Override
            public void distributePopulation() {
                while(this.village.getWarriors() < (int) (this.village.getPopulation() * warriorFact))
                    this.village.addWarriors(1);
                this.village.evenFarmers();
            }
            @Override
            public void notifyRaidFrom(Village perpetrator, int foodStolen) {

            }
        };
    }

    public static Chief WAR(final double warriorFact) {
        return new BasicChief() {

            @Override
            public AutumnAction determineAutumnAction() {
                if(this.village.hasEnoughtFood() || this.village.getNeighbors().isEmpty() )
                    return new AutumnAction(Action.NOTHING, this.village);

                List<Village> vs = new ArrayList<Village>(this.village.getNeighbors());
                Collections.shuffle(vs);

                return new AutumnAction(Action.RAID, this.village, vs.get(0));
            }

            @Override
            public void distributePopulation() {
                while(this.village.getWarriors() < (int) (this.village.getPopulation() * warriorFact))
                    this.village.addWarriors(1);
                this.village.evenFarmers();
            }

            @Override
            public void notifyRaidFrom(Village perpetrator, int foodStolen) {

            }
        };
    }

    public static Chief NO_WAR() {
        return NO_WAR(0);
    }

    public static Chief TIT_TAT() {
        return new BasicChief() {
            public int addWarriors;
            public Village target;

            @Override
            public void distributePopulation() {
                if(this.addWarriors > 0) {
                    this.village.addWarriors(this.addWarriors);
                }
                this.village.evenFarmers();
            }

            @Override
            public AutumnAction determineAutumnAction() {
                AutumnAction a = new AutumnAction(Action.NOTHING, this.village);
                if(target != null) {
                    a = new AutumnAction(Action.RAID, this.village, this.target);
                    this.target = null;
                    this.addWarriors = 0;
                }
                return a;
            }

            @Override
            public void notifyRaidFrom(Village perpetrator, int foodStolen) {
                if(this.village.getWarriors() <= this.village.getPopulation() * 0.2) {
                    this.target = perpetrator;
                    this.addWarriors = (int) Math.max(1, this.village.getPopulation() * 0.05);
                }
            }
        };
    }
}
