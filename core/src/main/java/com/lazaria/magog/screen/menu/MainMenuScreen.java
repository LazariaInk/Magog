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
    private Music menuMusic;  // Obiectul pentru muzică
    private Sound buttonSound;  // Obiectul pentru sunetul butonului
    // Actor care face tranziția
    private Actor fadeActor;


    public MainMenuScreen(StartGame game, Music menuMusic, Sound buttonSound) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.buttonSound = buttonSound;
        batch = new SpriteBatch();
        viewport = new FitViewport(1920, 1080);
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        leafTexture = new Texture(Gdx.files.internal("leaf.png"));  // Încarcă textura pentru frunze

        stage = new Stage(viewport);

        // Setează stage-ul ca InputProcessor
        Gdx.input.setInputProcessor(stage);
        fadeActor = new Actor();
        fadeActor.setColor(1f, 1f, 1f, 0f);  // Setăm alfa la 0 (invizibil)
        fadeActor.setSize(1920, 1080);  // Acoperă întregul ecran

        // Adaugă actorul la scenă
        stage.addActor(fadeActor);

        // 1. Creează butonul Play
        Texture playButtonTexture = new Texture(Gdx.files.internal("Start.png"));
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(playButtonTexture));
        ImageButton playButton = new ImageButton(playDrawable);
        playButton.setSize(400f, 200f);

        // 2. Creează container pentru butonul Play
        playContainer = new Container<>(playButton);
        playContainer.setTransform(true);
        playContainer.size(400f, 200f);
        playContainer.setOrigin(playContainer.getWidth() / 2, playContainer.getHeight() / 2);
        playContainer.setPosition(viewport.getWorldWidth() - playButton.getWidth(), 200);

        // 3. Creează butonul Settings
        Texture settingsButtonTexture = new Texture(Gdx.files.internal("settings.png"));
        TextureRegionDrawable settingsDrawable = new TextureRegionDrawable(new TextureRegion(settingsButtonTexture));
        ImageButton settingsButton = new ImageButton(settingsDrawable);
        settingsButton.setSize(200f, 100f);

        // 4. Creează container pentru butonul Settings și îl poziționează în colțul din dreapta sus
        settingsContainer = new Container<>(settingsButton);
        settingsContainer.setTransform(true);
        settingsContainer.size(200f, 100f);
        settingsContainer.setOrigin(settingsContainer.getWidth() / 2, settingsContainer.getHeight() / 2);
        settingsContainer.setPosition(viewport.getWorldWidth() - settingsButton.getWidth() - 20, viewport.getWorldHeight() - settingsButton.getHeight() - 20);  // Poziționează în dreapta sus

        // 5. Creează butonul Profile
        Texture profileButtonTexture = new Texture(Gdx.files.internal("profile.png"));
        TextureRegionDrawable profileDrawable = new TextureRegionDrawable(new TextureRegion(profileButtonTexture));
        ImageButton profileButton = new ImageButton(profileDrawable);
        profileButton.setSize(400f, 200f);

        // 6. Creează container pentru butonul Profile și îl poziționează în stânga, la același nivel cu butonul Play
        profileContainer = new Container<>(profileButton);
        profileContainer.setTransform(true);
        profileContainer.size(400f, 200f);
        profileContainer.setOrigin(profileContainer.getWidth() / 2, profileContainer.getHeight() / 2);
        profileContainer.setPosition(300, 200);  // Poziționează în partea stângă jos, la același nivel Y cu Play

        // Funcție comună pentru acțiunile de redare a sunetului butonului și de ajustare a volumului muzicii
        ClickListener buttonClickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                // Redă sunetul butonului cu un volum mai mare (1.0 = maxim)
                buttonSound.play(game.getSoundEffectsVolume());

                // După 1 secundă, restabilește volumul muzicii
                stage.addAction(Actions.sequence(
                    Actions.delay(1f)
                ));
            }
        };

        // 7. Adaugă listener pentru butonul Play
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Play a fost apăsat!");
                playContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.2f, 1.2f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                // Redă sunetul și ajustează volumul muzicii
                buttonClickListener.clicked(event, x, y);
            }
        });

        // 8. Adaugă listener pentru butonul Settings
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Settings a fost apăsat!");
                settingsContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                // Redă sunetul și ajustează volumul muzicii
                buttonClickListener.clicked(event, x, y);
                transitionToSettingsScreen();
            }
        });

        // 9. Adaugă listener pentru butonul Profile
        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Butonul Profile a fost apăsat!");
                profileContainer.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                // Redă sunetul și ajustează volumul muzicii
                buttonClickListener.clicked(event, x, y);
            }
        });

        // 10. Adaugă butoanele la scenă
        stage.addActor(playContainer);
        stage.addActor(settingsContainer);
        stage.addActor(profileContainer);

        // Creează frunzele
        fallingLeaves = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FallingLeaf leaf = new FallingLeaf(leafTexture, viewport.getWorldWidth());
            fallingLeaves.add(leaf);
            stage.addActor(leaf);
        }
    }

    @Override
    public void render(float delta) {
        // Curăță ecranul
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Aplică viewport-ul
        viewport.apply();

        // Desenează textura de fundal
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Actualizează și desenează scena (inclusiv frunzele și butoanele)
        stage.act(delta);
        stage.draw();
    }

    private void transitionToSettingsScreen() {

        // Fade out în 0.5 secunde și apoi comută ecranele
        fadeActor.addAction(Actions.sequence(
            Actions.alpha(0f),  // Asigură că fadeActor este invizibil la început
            Actions.fadeIn(0.5f),  // Fade out timp de 0.5 secunde
            Actions.run(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new SettingsScreen(game, menuMusic, buttonSound));  // Comută la ecranul Settings
                }
            })
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
        menuMusic.dispose();  // Oprește și eliberează resursele muzicii
        buttonSound.dispose();  // Oprește și eliberează resursele pentru sunet
    }
}
