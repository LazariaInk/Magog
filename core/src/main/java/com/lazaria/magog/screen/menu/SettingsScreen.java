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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.lazaria.magog.StartGame;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private Music menuMusic;
    private Sound buttonSound;
    private Skin skin;
    private StartGame game;

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture leafTexture;
    private ArrayList<FallingLeaf> fallingLeaves;
    private float soundEffectsVolume = 1.0f;

    private Container<ImageButton> returnContainer;

    private Actor fadeActor;

    public SettingsScreen(StartGame game, Music menuMusic, Sound buttonSound) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.buttonSound = buttonSound;

        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        fadeActor = new Actor();
        fadeActor.setColor(1f, 1f, 1f, 0f);
        fadeActor.setSize(1920, 1080);

        stage.addActor(fadeActor);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));

        fallingLeaves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FallingLeaf leaf = new FallingLeaf(leafTexture, viewport.getWorldWidth());
            fallingLeaves.add(leaf);
            stage.addActor(leaf);
        }

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        // Stil personalizat pentru slider
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("default-slider", 0.4f, 0.4f, 0.4f, 1f);  // Slider mai vizibil
        sliderStyle.knob = skin.newDrawable("default-slider-knob", 1f, 1f, 1f, 1f);
        sliderStyle.background.setMinHeight(20f);  // Grosimea slider-ului
        sliderStyle.knob.setMinHeight(40f);        // Dimensiunea knob-ului

        // Stil personalizat pentru label (font mai mare)
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default-font");
        labelStyle.font.getData().setScale(2f);  // Mărește fontul

        // Creăm label și slider pentru volum muzică
        Label musicLabel = new Label("Music Volume", labelStyle);
        Slider musicSlider = new Slider(0f, 1f, 0.1f, false, sliderStyle);
        musicSlider.setValue(menuMusic.getVolume());

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuMusic.setVolume(musicSlider.getValue());
            }
        });

        // Creăm label și slider pentru efectele sonore
        Label effectsLabel = new Label("Effects Volume", labelStyle);
        Slider effectsSlider = new Slider(0f, 1f, 0.1f, false, sliderStyle);
        effectsSlider.setValue(game.getSoundEffectsVolume());

        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundEffectsVolume = effectsSlider.getValue();
                buttonSound.play(soundEffectsVolume);
            }
        });

        // Buton de returnare la meniul principal
        Texture returnButtonTexture = new Texture(Gdx.files.internal("return.png"));
        TextureRegionDrawable returnDrawable = new TextureRegionDrawable(new TextureRegion(returnButtonTexture));
        ImageButton returnButton = new ImageButton(returnDrawable);
        returnButton.setSize(200f, 100f);

        returnContainer = new Container<>(returnButton);
        returnContainer.setTransform(true);
        returnContainer.size(200f, 100f);
        returnContainer.setOrigin(returnContainer.getWidth() / 2, returnContainer.getHeight() / 2);
        returnContainer.setPosition(viewport.getWorldWidth() - returnButton.getWidth() - 20, viewport.getWorldHeight() - returnButton.getHeight() - 20);  // Poziționează în dreapta sus

        ClickListener buttonClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonSound.play(game.getSoundEffectsVolume());

                stage.addAction(Actions.sequence(
                    Actions.delay(1f)
                ));
            }
        };

        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Settings a fost apăsat!");
                returnContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                System.out.println("Returning to Main Menu");
                game.setSoundEffectsVolume(soundEffectsVolume);
                buttonClickListener.clicked(event, x, y);
                transitionToMainMenuScreenScreen();
            }
        });

        table.add(musicLabel).pad(20);
        table.row();
        table.add(musicSlider).width(600).pad(20);
        table.row();
        table.add(effectsLabel).pad(20);
        table.row();
        table.add(effectsSlider).width(600).pad(20);
        table.row();

        stage.addActor(returnContainer);
        stage.addActor(table);
    }

    private void transitionToMainMenuScreenScreen() {
        fadeActor.addAction(Actions.sequence(
            Actions.alpha(0f),
            Actions.fadeIn(0.5f),
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenuScreen(game, menuMusic, buttonSound));
                }
            })
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
