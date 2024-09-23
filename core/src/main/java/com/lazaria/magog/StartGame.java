package com.lazaria.magog;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.lazaria.magog.screen.menu.MainMenuScreen;

public class StartGame extends Game {
    private float soundEffectsVolume = 1.0f;
    private float musicVolume = 0.5f;

    @Override
    public void create() {
        Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu-music.mp3"));
        Sound buttonSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));

        menuMusic.setLooping(true);
        menuMusic.setVolume(musicVolume);
        menuMusic.play();

        setScreen(new MainMenuScreen(this, menuMusic, buttonSound));
    }

    public float getSoundEffectsVolume() {
        return soundEffectsVolume;
    }

    public void setSoundEffectsVolume(float volume) {
        this.soundEffectsVolume = volume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
    }
}
