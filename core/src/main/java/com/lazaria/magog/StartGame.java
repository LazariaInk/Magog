package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class StartGame extends Game {
    private static StartGame instance;
    private SoundManager soundManager;
    private Character selectedCharacter;

    public static StartGame getInstance() {
        return instance;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public Character getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(Character selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
    }

    @Override
    public void create() {
        instance = this;
        setSelectedCharacter(new Character.Builder()
            .setIdleTexture(new Texture(Gdx.files.internal("idle.png")))
            .setRunTexture(new Texture(Gdx.files.internal("run.png")))
            .setAttackTexture(new Texture(Gdx.files.internal("attack.png")))
            .setRunAttackTexture(new Texture(Gdx.files.internal("runattack.png")))
            .setRunSound(Gdx.audio.newSound(Gdx.files.internal("run.wav")))
            .setAttackSound(Gdx.audio.newSound(Gdx.files.internal("attack.mp3")))
            .setPosition(500, 50)
            .setSpeed(600f)
            .build());
        soundManager = SoundManager.getInstance();
        setScreen(new MainMenuScreen(this));
    }
}
