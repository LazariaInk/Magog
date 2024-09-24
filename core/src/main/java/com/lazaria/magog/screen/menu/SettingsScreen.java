package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
import com.lazaria.magog.audio.SoundManager;

import java.util.ArrayList;

public class SettingsScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private Skin skin;
    private StartGame game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture leafTexture;
    private ArrayList<FallingLeaf> fallingLeaves;
    private Container<ImageButton> returnContainer;
    private Actor fadeActor;
    private SoundManager soundManager;

    public SettingsScreen(StartGame game) {
        this.game = game;
        soundManager = game.getSoundManager();

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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (100 * Gdx.graphics.getDensity());
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = skin.newDrawable("default-slider", 0.4f, 0.4f, 0.4f, 1f);
        sliderStyle.knob = skin.newDrawable("default-slider-knob", 1f, 1f, 1f, 1f);
        sliderStyle.background.setMinHeight(20f);
        sliderStyle.knob.setMinHeight(40f);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Label musicLabel = new Label("Music Volume", labelStyle);
        Slider musicSlider = new Slider(0f, 1f, 0.1f, false, sliderStyle);
        musicSlider.setValue(soundManager.getMusicVolume());

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setMusicVolume(musicSlider.getValue());
            }
        });
        Label effectsLabel = new Label("Effects Volume", labelStyle);
        Slider effectsSlider = new Slider(0f, 1f, 0.1f, false, sliderStyle);
        effectsSlider.setValue(soundManager.getSoundEffectsVolume());
        effectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.setSoundEffectsVolume(effectsSlider.getValue());
                soundManager.playSoundEffect();  // Redă sunetul butonului pentru feedback vizual
            }
        });
        Texture returnButtonTexture = new Texture(Gdx.files.internal("return.png"));
        TextureRegionDrawable returnDrawable = new TextureRegionDrawable(new TextureRegion(returnButtonTexture));
        ImageButton returnButton = new ImageButton(returnDrawable);
        returnButton.setSize(200f, 100f);

        returnContainer = new Container<>(returnButton);
        returnContainer.setTransform(true);
        returnContainer.size(200f, 100f);
        returnContainer.setOrigin(returnContainer.getWidth() / 2, returnContainer.getHeight() / 2);
        returnContainer.setPosition(viewport.getWorldWidth() - returnButton.getWidth() - 20, viewport.getWorldHeight() - returnButton.getHeight() - 20);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                returnContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                soundManager.playSoundEffect();
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
            Actions.run(() -> game.setScreen(new MainMenuScreen(game)))
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
