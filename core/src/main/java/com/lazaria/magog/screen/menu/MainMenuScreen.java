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
import com.lazaria.magog.utils.ButtonFactory;

import java.util.ArrayList;

public class MainMenuScreen extends ScreenAdapter {
    private StartGame game;
    private Texture backgroundTexture;
    private Texture leafTexture;
    private SpriteBatch batch;
    private Stage stage;
    private FitViewport viewport;
    private Container<ImageButton> playContainer;
    private Container<ImageButton> settingsContainer;
    private Container<ImageButton> profileContainer;
    private ArrayList<FallingLeaf> fallingLeaves;
    private ButtonFactory buttonFactory;

    public MainMenuScreen(StartGame game) {
        this.game = game;
        buttonFactory = new ButtonFactory();
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));
        game.getSoundManager().playMenuMusic();
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        initButtons();
        stage.addActor(playContainer);
        stage.addActor(settingsContainer);
        stage.addActor(profileContainer);

        fallingLeaves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FallingLeaf leaf = new FallingLeaf(leafTexture, viewport.getWorldWidth());
            fallingLeaves.add(leaf);
            stage.addActor(leaf);
        }
    }

    private void initButtons() {
        playContainer = buttonFactory.createButton("Start.png", 400f, 200f, viewport.getWorldWidth() - 400f, 200,
            GameScreen.class, stage
        );
        settingsContainer = buttonFactory.createButton("settings.png", 200f, 100f, viewport.getWorldWidth() - 200f - 20,
            viewport.getWorldHeight() - 100f - 20, SettingsScreen.class, stage);

        profileContainer = buttonFactory.createButton("profile.png", 400f, 200f, 300, 200,
            SettingsScreen.class, stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
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
        leafTexture.dispose();
        stage.dispose();
    }
}
