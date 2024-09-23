package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.lazaria.magog.StartGame;

public class GameScreen extends ScreenAdapter {
    private Stage stage;
    private FitViewport viewport;
    private Music menuMusic;
    private Sound buttonSound;
    private StartGame game;

    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture knightTexture;
    private Animation<TextureRegion> knightAnimation;
    private float elapsedTime = 0f;

    private float knightX = 500;
    private float knightY = 200;
    private float knightSpeed = 600f;

    private boolean movingRight = false;
    private boolean movingLeft = false;
    private boolean facingRight = true;

    private Texture idleTexture;
    private Animation<TextureRegion> idleAnimation;

    private float soundEffectsVolume = 1.0f;

    private Container<ImageButton> returnContainer;

    private Actor fadeActor;

    private Sound runSound;
    private long runSoundId = -1;
    private Music gameMusic;


    public GameScreen(StartGame game, Music menuMusic, Sound buttonSound) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.buttonSound = buttonSound;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("battle.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(game.getSoundEffectsVolume());
        gameMusic.play();

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

        batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("firstMap.png"));

        idleTexture = new Texture(Gdx.files.internal("idle.png"));
        TextureRegion[][] tmpIdleFrames = TextureRegion.split(idleTexture, idleTexture.getWidth() / 2, idleTexture.getHeight());
        Array<TextureRegion> idleFrames = new Array<>();

        for (TextureRegion[] row : tmpIdleFrames) {
            for (TextureRegion region : row) {
                idleFrames.add(region);
            }
        }
        idleAnimation = new Animation<>(0.3f, idleFrames, Animation.PlayMode.LOOP);

        knightTexture = new Texture(Gdx.files.internal("run.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(knightTexture, knightTexture.getWidth() / 7, knightTexture.getHeight());
        Array<TextureRegion> knightFrames = new Array<>();

        for (TextureRegion[] row : tmpFrames) {
            for (TextureRegion region : row) {
                knightFrames.add(region);
            }
        }

        knightAnimation = new Animation<>(0.1f, knightFrames, Animation.PlayMode.LOOP);

        runSound = Gdx.audio.newSound(Gdx.files.internal("run.wav"));

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
                game.setSoundEffectsVolume(soundEffectsVolume);
                transitionToMainMenuScreenScreen();
            }
        });
        stage.addActor(returnContainer);
    }

    private void transitionToMainMenuScreenScreen() {
        gameMusic.stop();

        menuMusic.setLooping(true);
        menuMusic.setVolume(game.getSoundEffectsVolume());
        menuMusic.play();

        fadeActor.addAction(Actions.sequence(
            Actions.alpha(0f),
            Actions.fadeIn(0.5f),
            Actions.run(() -> game.setScreen(new MainMenuScreen(game, menuMusic, buttonSound)))
        ));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(viewport.getCamera().combined);

        elapsedTime += delta;
        movingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        movingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);

        if (movingLeft || movingRight) {
            if (runSoundId == -1) {
                runSoundId = runSound.loop(0.1f);
            }
        } else {
            if (runSoundId != -1) {
                runSound.stop(runSoundId);
                runSoundId = -1;
            }
        }

        if (movingRight) {
            knightX += knightSpeed * delta;
            facingRight = true;
        } else if (movingLeft) {
            knightX -= knightSpeed * delta;
            facingRight = false;
        }

        Animation<TextureRegion> currentAnimation;
        if (movingLeft || movingRight) {
            currentAnimation = knightAnimation;
        } else {
            currentAnimation = idleAnimation;
        }

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);

        if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }


        float scaleFactor = 4.0f;
        float knightWidth = currentFrame.getRegionWidth() * scaleFactor;
        float knightHeight = currentFrame.getRegionHeight() * scaleFactor;

        knightY = 50;

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.draw(currentFrame, knightX, knightY, knightWidth, knightHeight);

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
        knightTexture.dispose();
        idleTexture.dispose();
        stage.dispose();
        gameMusic.dispose();
    }
}
