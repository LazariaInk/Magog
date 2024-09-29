package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lazaria.magog.Settings;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.utils.ButtonFactory;

import java.util.ArrayList;

public class ProfileScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture leafTexture;
    private ArrayList<FallingLeaf> fallingLeaves;
    private Container<ImageButton> returnContainer;
    private Container<ImageButton> knightButtonContainer;
    private Container<ImageButton> necromantButtonContainer;
    private Container<ImageButton> assassinButtonContainer;
    private Actor fadeActor;
    private SoundManager soundManager;
    private ButtonFactory buttonFactory;

    public ProfileScreen(Settings game) {
        soundManager = game.getSoundManager();
        buttonFactory = new ButtonFactory();

        viewport = new FitViewport(1920, 1080);
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);

        fadeActor = new Actor();
        fadeActor.setColor(1f, 1f, 1f, 0f);
        fadeActor.setSize(1920, 1080);

        stage.addActor(fadeActor);

        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));

        fallingLeaves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FallingLeaf leaf = new FallingLeaf(leafTexture, viewport.getWorldWidth());
            fallingLeaves.add(leaf);
            stage.addActor(leaf);
        }
        returnContainer = buttonFactory.createButton("return.png", 200, 100, viewport.getWorldWidth()
            - 200 - 20, viewport.getWorldHeight() - 100 - 20, MainMenuScreen.class, stage);

        knightButtonContainer = buttonFactory.createButton("knight.png",500,600,500, viewport.getWorldHeight()-550, MainMenuScreen.class,stage);

        necromantButtonContainer = buttonFactory.createButton("necromant.png",500,600,1000,viewport.getWorldHeight()-550, MainMenuScreen.class,stage);

        assassinButtonContainer=buttonFactory.createButton("shadow.png",500,600,1500,viewport.getWorldHeight()-550, MainMenuScreen.class,stage);

        addHoverEffect(knightButtonContainer);
        addHoverEffect(necromantButtonContainer);
        addHoverEffect(assassinButtonContainer);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (100 * Gdx.graphics.getDensity());
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        stage.addActor(returnContainer);
        stage.addActor(knightButtonContainer);
        stage.addActor(necromantButtonContainer);
        stage.addActor(assassinButtonContainer);


    }
    private void addHoverEffect(Container<ImageButton> buttonContainer) {

        buttonContainer.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                return super.mouseMoved(event, x, y);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                System.out.println("An intrat in hover");
                buttonContainer.addAction(Actions.scaleTo(1.1f, 1.1f, 0.2f));
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                System.out.println("Am iesit din hover");
                buttonContainer.addAction(Actions.scaleTo(1f, 1f, 0.2f));
                super.exit(event, x, y, pointer, toActor);
            }
        });
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
