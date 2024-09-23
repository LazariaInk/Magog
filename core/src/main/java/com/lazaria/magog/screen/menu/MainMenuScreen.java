package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazaria.magog.StartGame;

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
    private Music menuMusic;
    private Sound buttonSound;

    private Actor fadeActor;


    public MainMenuScreen(StartGame game, Music menuMusic, Sound buttonSound) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.buttonSound = buttonSound;
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));

        if (!menuMusic.isPlaying()) {
            menuMusic.setLooping(true);
            menuMusic.setVolume(game.getSoundEffectsVolume());
            menuMusic.play();
        }

        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
        fadeActor = new Actor();
        fadeActor.setColor(1f, 1f, 1f, 0f);
        fadeActor.setSize(1920, 1080);

        stage.addActor(fadeActor);

        Texture playButtonTexture = new Texture(Gdx.files.internal("Start.png"));
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        ImageButton playButton = new ImageButton(playDrawable);
        playButton.setSize(400f, 200f);

        playContainer = new Container<>(playButton);
        playContainer.setTransform(true);
        playContainer.size(400f, 200f);
        playContainer.setOrigin(playContainer.getWidth() / 2, playContainer.getHeight() / 2);
        playContainer.setPosition(viewport.getWorldWidth() - playButton.getWidth(), 200);

        Texture settingsButtonTexture = new Texture(Gdx.files.internal("settings.png"));
        TextureRegionDrawable settingsDrawable = new TextureRegionDrawable(new TextureRegion(settingsButtonTexture));
        ImageButton settingsButton = new ImageButton(settingsDrawable);
        settingsButton.setSize(200f, 100f);

        settingsContainer = new Container<>(settingsButton);
        settingsContainer.setTransform(true);
        settingsContainer.size(200f, 100f);
        settingsContainer.setOrigin(settingsContainer.getWidth() / 2, settingsContainer.getHeight() / 2);
        settingsContainer.setPosition(viewport.getWorldWidth() - settingsButton.getWidth() - 20, viewport.getWorldHeight() - settingsButton.getHeight() - 20);

        Texture profileButtonTexture = new Texture(Gdx.files.internal("profile.png"));
        TextureRegionDrawable profileDrawable = new TextureRegionDrawable(new TextureRegion(profileButtonTexture));
        ImageButton profileButton = new ImageButton(profileDrawable);
        profileButton.setSize(400f, 200f);

        profileContainer = new Container<>(profileButton);
        profileContainer.setTransform(true);
        profileContainer.size(400f, 200f);
        profileContainer.setOrigin(profileContainer.getWidth() / 2, profileContainer.getHeight() / 2);
        profileContainer.setPosition(300, 200);

        ClickListener buttonClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                buttonSound.play(game.getSoundEffectsVolume());

                stage.addAction(Actions.sequence(
                    Actions.delay(1f)
                ));
            }
        };

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Play a fost apăsat!");
                playContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.2f, 1.2f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                buttonClickListener.clicked(event, x, y);
                transitionToPlayScreen();
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Settings a fost apăsat!");
                settingsContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                buttonClickListener.clicked(event, x, y);
                transitionToSettingsScreen();
            }
        });

        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Profile a fost apăsat!");
                profileContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                buttonClickListener.clicked(event, x, y);
            }
        });

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

    private void transitionToSettingsScreen() {

        fadeActor.addAction(Actions.sequence(
            Actions.alpha(0f),
            Actions.fadeIn(0.5f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new SettingsScreen(game, menuMusic, buttonSound));
                }
            })
        ));
    }

    private void transitionToPlayScreen() {
        menuMusic.stop();

        fadeActor.addAction(Actions.sequence(
            Actions.alpha(0f),
            Actions.fadeIn(0.5f),
            Actions.run(() -> game.setScreen(new GameScreen(game, menuMusic, buttonSound)))
        ));
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
        menuMusic.dispose();
        buttonSound.dispose();
    }
}
