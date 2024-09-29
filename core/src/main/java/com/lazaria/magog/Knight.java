package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Knight extends Character {
    public Knight(float x, float y, float speed) {
        super(
            new Texture(Gdx.files.internal("knight_idle.png")),
            new Texture(Gdx.files.internal("knight_run.png")),
            new Texture(Gdx.files.internal("knight_runattack.png")),
            new Texture(Gdx.files.internal("knight_attack.png")),
            Gdx.audio.newSound(Gdx.files.internal("knight_run.wav")),
            Gdx.audio.newSound(Gdx.files.internal("knight_attack.mp3")),
            x, y, speed
        );
    }

    @Override
    public void performSkill() {
        System.out.println("Knight is holding the shield!");
    }
}
