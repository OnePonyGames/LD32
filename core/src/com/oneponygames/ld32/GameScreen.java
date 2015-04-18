package com.oneponygames.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Icewind on 18.04.2015.
 */
public class GameScreen implements Screen {

    private static final float SEASON_CHANGE_TIME = 1.5f;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Map<Season, Texture> backgrounds;
    private Season season;
    private Season newSeason;
    private float seasonWait;

    @Override
    public void show() {
        this.season = Season.SPRING;

        batch = new SpriteBatch();
        batch.enableBlending();

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 800, 600);

        this.backgrounds = new HashMap<Season, Texture>();
        this.backgrounds.put(Season.SPRING, new Texture(Gdx.files.internal("background_1_spring.png")));
        this.backgrounds.put(Season.SUMMER, new Texture(Gdx.files.internal("background_2_summer.png")));
        this.backgrounds.put(Season.AUTUMN, new Texture(Gdx.files.internal("background_3_autumn.png")));
        this.backgrounds.put(Season.WINTER, new Texture(Gdx.files.internal("background_4_winter.png")));
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

            this.seasonWait += delta;
            if(this.seasonWait > SEASON_CHANGE_TIME) {
                this.seasonWait = 0;
                this.season = newSeason;
                this.newSeason = null;
            }
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
        this.newSeason = season;
        try {
            Thread.sleep((long) (SEASON_CHANGE_TIME * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
