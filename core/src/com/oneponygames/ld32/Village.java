package com.oneponygames.ld32;

import java.util.List;
import java.util.Set;

/**
 * Created by Icewind on 18.04.2015.
 */
public class Village {
    private static final int FOOD_BUFFER = 2;
    private static final double WARRIOR_FOOD_FACT = 2;

    private final Chief chief;
    private final int y;
    private final int x;

    private int population;
    private int food;
    private boolean starved;
    private String name;

    private int warriors;
    private int farmers;
    private List<Village> neighbors;

    public Village(Chief chief, int startingPop, int startingFood, String name, int x, int y) {
        this.chief = chief;
        this.population = startingPop;
        this.farmers = this.population;
        this.food = startingFood;
        this.starved = false;
        this.name = name;
        this.x = x;
        this.y = y;

        chief.setVillage(this);
    }

    public int getFood() {
        return food;
    }

    public int getPopulation() {
        return population;
    }

    public boolean haveStarved() {
        return this.starved;
    }

    public void addPopulation(int newPop) {
        this.population += newPop;
    }

    public String getName() {
        return name;
    }

    public Chief getChief() {
        return chief;
    }

    public int getWarriors() {
        return warriors;
    }

    public int getFarmers() {
        return farmers;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setStarved(boolean starved) {
        this.starved = starved;
    }

    public void starvePopulation(int people) {
        this.population -= people;
        this.population = Math.max(this.population, 0);
        this.starved = true;
    }

    public void setNeighbors(List<Village> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Village> getNeighbors() {
        return neighbors;
    }

    public boolean hasEnoughtFood() {
        return this.getFoodDeficit() <= 0;
    }

    public int getFoodDeficit() {
        return (int)((this.getFarmers() + this.getWarriors() * WARRIOR_FOOD_FACT) - (this.getFood() + FOOD_BUFFER));
    }

    public void addFood(int foodStolen) {
        this.food += foodStolen;
    }

    public void removeFood(int foodStolen) {
        this.addFood(-foodStolen);
    }

    public void addWarriors(int i) {
        this.warriors += i;
    }

    public int getFoodRequirement() {
        return (int)((this.getFarmers() + this.getWarriors() * WARRIOR_FOOD_FACT));
    }

    public void evenFarmers() {
        this.farmers = this.population - this.warriors;
    }

    public void removeNeighbor(Village v) {
        this.neighbors.remove(v);
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getSpareFood() {
        return this.getFood() - this.getFoodRequirement();
    }
}
