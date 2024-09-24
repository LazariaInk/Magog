package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class StartGame extends Game {
    private static StartGame instance;
    private SoundManager soundManager;

    public static StartGame getInstance() {
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
