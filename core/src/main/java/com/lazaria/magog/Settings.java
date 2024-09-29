package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class Settings extends Game {
    private static Settings instance;
    private SoundManager soundManager;
    private Character selectedCharacter;

    public static Settings getInstance() {
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
        setSelectedCharacter(new Knight(500,50,600f));
        soundManager = SoundManager.getInstance();
        setScreen(new MainMenuScreen(this));
    }
}
