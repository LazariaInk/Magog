package com.lazaria.magog.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    private static SoundManager instance;

    private Music menuMusic;
    private Music gameMusic;
    private Sound buttonSound;
    private float musicVolume = 0.5f;
    private float soundEffectsVolume = 1.0f;

    private SoundManager() {
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu-music.mp3"));
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("battle.mp3"));
        this.buttonSound = Gdx.audio.newSound(Gdx.files.internal("button.mp3"));
        menuMusic.setLooping(true);
        gameMusic.setLooping(true);
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playMenuMusic() {
        stopGameMusic();
        if (!menuMusic.isPlaying()) {
            menuMusic.setVolume(musicVolume);
            menuMusic.play();
        }
    }

    public void stopMenuMusic() {
        if (menuMusic.isPlaying()) {
            menuMusic.stop();
        }
    }

    public void playGameMusic() {
        stopMenuMusic();
        if (!gameMusic.isPlaying()) {
            gameMusic.setVolume(musicVolume);
            gameMusic.play();
        }
    }

    public void stopGameMusic() {
        if (gameMusic.isPlaying()) {
            gameMusic.stop();
        }
    }

    public void playSoundEffect() {
        long soundId = buttonSound.play(soundEffectsVolume);
        buttonSound.setVolume(soundId, soundEffectsVolume);
    }


    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (menuMusic.isPlaying()) {
            menuMusic.setVolume(volume);
        }
        if (gameMusic.isPlaying()) {
            gameMusic.setVolume(volume);
        }
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setSoundEffectsVolume(float volume) {
        this.soundEffectsVolume = volume;
    }

    public float getSoundEffectsVolume() {
        return soundEffectsVolume;
    }
}
