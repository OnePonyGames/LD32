package com.oneponygames.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Icewind on 18.04.2015.
 */
public class GameScreen extends InputAdapter implements Screen {

    private static final float SEASON_CHANGE_TIME = 3.5f;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Map<Season, Texture> backgrounds;
    private Texture ui;
    private Season season;
    private Season newSeason;
    private float seasonWait;
    private List<String> texts = new ArrayList<String>();
    private List<Village> villages;
    private Village playerVillage;
    private boolean inputEnabled = false;
    private int input = 0;
    private String inputText;
    private int inputMin;
    private int inputMax;
    private int year;

    @Override
    public void show() {
        this.season = Season.WINTER;

        batch = new SpriteBatch();
        batch.enableBlending();
        font = new BitmapFont();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);

        this.backgrounds = new HashMap<Season, Texture>();
        this.backgrounds.put(Season.SPRING, new Texture(Gdx.files.internal("background_1_spring.png")));
        this.backgrounds.put(Season.SUMMER, new Texture(Gdx.files.internal("background_2_summer.png")));
        this.backgrounds.put(Season.AUTUMN, new Texture(Gdx.files.internal("background_3_autumn.png")));
        this.backgrounds.put(Season.WINTER, new Texture(Gdx.files.internal("background_4_winter.png")));

        this.ui = new Texture(Gdx.files.internal("UI.png"));
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if(newSeason == null)
            this.batch.draw(this.backgrounds.get(this.season), 0, 150);
        else {
            Color c = this.batch.getColor();
            this.batch.setColor(c.r, c.b, c.g, 1f);
            this.batch.draw(this.backgrounds.get(this.season), 0, 150);

            this.batch.setColor(c.r, c.b, c.g, this.seasonWait/SEASON_CHANGE_TIME);
            this.batch.draw(this.backgrounds.get(this.newSeason), 0, 150);

            if(!this.inputEnabled) {
                this.seasonWait += delta;
                if (this.seasonWait > SEASON_CHANGE_TIME) {
                    this.seasonWait = 0;
                    this.season = newSeason;
                    this.newSeason = null;
                }
            }
        }

        Color c = this.batch.getColor();
        this.batch.setColor(c.r, c.b, c.g, 1f);

        this.batch.draw(this.ui, 0, 0);

        font.setColor(new Color(96 / 255, 90 / 255, 108 / 255, 1f));
        this.font.draw(this.batch, "Your Village, year "+this.year, 650, 130);
        this.font.draw(this.batch, "x "+this.playerVillage.getWarriors(), 655, 100);
        this.font.draw(this.batch, "x "+this.playerVillage.getFarmers(), 655, 55);
        this.font.draw(this.batch, "x "+this.playerVillage.getFood(), 740, 100);
        this.font.draw(this.batch, "x "+this.playerVillage.getFoodRequirement(), 740, 55);

        for (int i = 0; i < this.texts.size(); i++) {
            String text = this.texts.get(i);
            this.font.draw(this.batch, text, 30, 120 - i * 16);
        }
        if(this.texts.size()>5)
            this.texts.remove(0);

        if(inputEnabled) {
            this.font.draw(this.batch, this.input+inputText+" (Use up and down to change.)", 30, 120 - this.texts.size() * 16);
        }

        this.font.setColor(Color.WHITE);
        for(Village v : this.villages) {
            this.font.draw(this.batch, v.getName(), v.getX(), v.getY());
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void setSeason(Season season) {
        if(season == Season.SPRING)
            year++;
        this.newSeason = season;
        try {
            Thread.sleep((long) (SEASON_CHANGE_TIME * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addText(String text) {
        this.texts.add(text);
    }

    public void setVillages(List<Village> villages) {
        this.villages = villages;
    }

    public void setPlayerVillage(Village playerVillage) {
        this.playerVillage = playerVillage;
    }

    public int getNumberInput(String inputText, int min, int max) {
        this.inputEnabled = true;
        this.inputText = inputText;
        this.inputMin = min;
        this.inputMax = max;
        int input;
        this.input = min;
        try {
            synchronized(this) {
                this.wait();
                input = this.input;
                this.input = 0;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return input;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(inputEnabled) {
            if(keycode == Input.Keys.UP) {
                input = Math.min(input+1, this.inputMax);
            }
            if(keycode == Input.Keys.DOWN) {
                input = Math.max(input-1, this.inputMin);
            }
            if(keycode == Input.Keys.ENTER) {
                synchronized(this) {
                    this.inputEnabled = false;
                    this.addText(input + this.inputText);
                    this.notifyAll();
                }
            }
        }
        return true;
    }

    public void playerLost() {
        this.addText("You lost, all your people starved to death.");
    }
}
