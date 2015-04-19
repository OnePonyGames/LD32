package com.oneponygames.ld32;

import com.badlogic.gdx.Game;

import java.util.*;

public class LD32 extends Game {
    private static final int STARTING_POP = 50;
    private static final int STARTING_FOOD = 50;
    private SimulationManager simulation;

    @Override
	public void create () {
        GameScreen gameScreen = new GameScreen();

        Chief c1 = Strategy.NO_WAR(0.0);
        Chief c2 = Strategy.TIT_TAT();
        Chief c3 = Strategy.WAR(0.05);
        PlayerChief player = new PlayerChief(gameScreen);

        Village v1 = new Village(c1, STARTING_POP, STARTING_FOOD, "Verengi", 440, 600-312);
        Village v2 = new Village(c2, STARTING_POP, STARTING_FOOD, "Okhoti", 148, 600-170);
        Village v3 = new Village(c3, STARTING_POP, STARTING_FOOD, "Suremi", 570, 600-45);
        Village playerVillage = new Village(player, STARTING_POP, STARTING_FOOD, "Your Village", 235, 600-60);

        List<Village> villages = new ArrayList<Village>(Arrays.asList(v1, v2, v3, playerVillage));
        v1.setNeighbors(new ArrayList<Village>(Arrays.asList(v2, v3, playerVillage)));
        v2.setNeighbors(new ArrayList<Village>(Arrays.asList(v1, v3, playerVillage)));
        v3.setNeighbors(new ArrayList<Village>(Arrays.asList(v2, v1, playerVillage)));
        playerVillage.setNeighbors(new ArrayList<Village>(Arrays.asList(v2, v1, v3)));

        gameScreen.setVillages(villages);
        gameScreen.setPlayerVillage(playerVillage);

        this.simulation = new SimulationManager(villages, gameScreen, 20);
        this.simulation.start();

        this.setScreen(gameScreen);
    }
}
