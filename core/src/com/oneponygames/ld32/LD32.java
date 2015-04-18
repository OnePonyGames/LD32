package com.oneponygames.ld32;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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

        Village v1 = new Village(c1, STARTING_POP, STARTING_FOOD, "Verengi");
        Village v2 = new Village(c2, STARTING_POP, STARTING_FOOD, "Okhoti");
        Village v3 = new Village(c3, STARTING_POP, STARTING_FOOD, "Suremi");
        Village playerVillage = new Village(player, STARTING_POP, STARTING_FOOD, "Player");

        List<Village> villages = new ArrayList<Village>(Arrays.asList(v1, v2, v3, playerVillage));
        v1.setNeighbors(new HashSet<Village>(Arrays.asList(v2, v3, playerVillage)));
        v2.setNeighbors(new HashSet<Village>(Arrays.asList(v1, v3, playerVillage)));
        v3.setNeighbors(new HashSet<Village>(Arrays.asList(v2, v1, playerVillage)));
        playerVillage.setNeighbors(new HashSet<Village>(Arrays.asList(v2, v1, v3)));


        this.simulation = new SimulationManager(villages, gameScreen, 20);
        this.simulation.start();

        this.setScreen(gameScreen);
    }
}
