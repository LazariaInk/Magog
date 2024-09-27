package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class Settings extends Game {
    private static Settings instance;
    private SoundManager soundManager;

    public static Settings getInstance() {
        return instance;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void create() {
        instance = this;
        soundManager = SoundManager.getInstance();
        setScreen(new MainMenuScreen(this));
    }
}
