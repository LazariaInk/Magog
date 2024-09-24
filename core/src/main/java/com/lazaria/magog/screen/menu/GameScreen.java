package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazaria.magog.StartGame;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.Character;
import com.lazaria.magog.Paddle;
import com.lazaria.magog.Ball;
import com.lazaria.magog.utils.ButtonFactory;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Character knight;
    private Paddle paddle;
    private Ball ball;
    private SoundManager soundManager;
    private Container<ImageButton> returnContainer;
    private ButtonFactory buttonFactory;

    public GameScreen(StartGame game) {
        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        buttonFactory = new ButtonFactory();
        batch = new SpriteBatch();
        backgroundTexture = new Texture(Gdx.files.internal("firstMap.png"));

        knight = new Character.Builder()
            .setIdleTexture(new Texture(Gdx.files.internal("idle.png")))
            .setRunTexture(new Texture(Gdx.files.internal("run.png")))
            .setRunSound(Gdx.audio.newSound(Gdx.files.internal("run.wav")))
            .setPosition(500, 50)
            .setSpeed(600f)
            .build();
        returnContainer = buttonFactory.createButton("return.png", 200, 100, viewport.getWorldWidth()
            - 200 - 20, viewport.getWorldHeight() - 100 - 20, MainMenuScreen.class, stage);
        paddle = new Paddle(200, 20);
        ball = new Ball(560, 1000f, 500f);

        soundManager = game.getSoundManager();
        stage.addActor(returnContainer);
        soundManager.playGameMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        knight.update(delta);
        ball.update(delta, knight, paddle);  // Update ball with both knight and paddle
        paddle.update(knight.getX(), knight.getY(), knight.getWidth(), knight.getHeight());  // Update paddle to follow knight

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        knight.render(batch);
        paddle.render(batch);  // Render paddle
        ball.render(batch);  // Render ball
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
        ball.dispose();
        paddle.dispose();
        stage.dispose();
    }
}
