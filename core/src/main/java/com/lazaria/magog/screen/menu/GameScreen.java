package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazaria.magog.*;
import com.lazaria.magog.Character;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.utils.ButtonFactory;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Character character;
    private Paddle paddle;
    private Ball ball;
    private SoundManager soundManager;
    private Container<ImageButton> returnContainer;
    private ButtonFactory buttonFactory;
    private Array<Crystal> crystals;

    public GameScreen() {
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        buttonFactory = new ButtonFactory();
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("firstMap.png"));

        crystals = new Array<>();

        for (int i = 0; i < 20; i++) {
            float x = MathUtils.random(100, 1800);
            float y = MathUtils.random(500, 1000);
            int type = MathUtils.random(1, 10);
            crystals.add(new Crystal(x, y, type));
        }

        character = Settings.getInstance().getSelectedCharacter();
        returnContainer = buttonFactory.createButton("return.png", 200, 100, viewport.getWorldWidth()
            - 200 - 20, viewport.getWorldHeight() - 100 - 20, MainMenuScreen.class, stage);
        paddle = new Paddle(200, 20);
        Knight knight = (Knight) character;
        knight.setPaddle(paddle);
        ball = new Ball(560, 1000f, 700f);

        soundManager = Settings.getInstance().getSoundManager();
        stage.addActor(returnContainer);
        soundManager.playGameMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        character.update(delta);
        ball.update(delta, character, paddle);
        paddle.update(character.getX(), character.getY(), character.getWidth(), character.getHeight());

        for (Crystal crystal : crystals) {
            crystal.update(delta);

            if (!crystal.isDestroyed() && crystal.collidesWith(ball)) {
                ball.bounce();
                crystal.hit();
            }
        }

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        character.render(batch);
        paddle.render(batch);
        ball.render(batch);

        for (Crystal crystal : crystals) {
            crystal.render(batch);
        }

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
        character.dispose();
        ball.dispose();
        paddle.dispose();
        stage.dispose();

        for (Crystal crystal : crystals) {
            crystal.dispose();
        }
    }
}
