package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.lazaria.magog.audio.SoundManager;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class StartGame extends Game {
    private SoundManager soundManager;

    public SoundManager getSoundManager() {
        return soundManager;
    }

    @Override
    public void create() {
        soundManager = SoundManager.getInstance();
        setScreen(new MainMenuScreen(this));
    }
}
