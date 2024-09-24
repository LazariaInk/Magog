package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazaria.magog.StartGame;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.Character;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Character knight;
    private SoundManager soundManager;

    public GameScreen(StartGame game) {
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("firstMap.png"));

        knight = new Character.Builder()
            .setIdleTexture(new Texture(Gdx.files.internal("idle.png")))
            .setRunTexture(new Texture(Gdx.files.internal("run.png")))
            .setRunSound(Gdx.audio.newSound(Gdx.files.internal("run.wav")))
            .setPosition(500, 50)
            .setSpeed(600f)
            .build();

        soundManager = game.getSoundManager();
        soundManager.playGameMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        knight.update(delta);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        knight.render(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        knight.dispose();
        stage.dispose();
    }
}
