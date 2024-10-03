package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Rogue extends Character {
    public Rogue(float x, float y, float speed) {
        super(
            new Texture(Gdx.files.internal("graphic/rogue_idle.png")),
            new Texture(Gdx.files.internal("graphic/rogue_run.png")),
            new Texture(Gdx.files.internal("graphic/rogue_runattack.png")),
            new Texture(Gdx.files.internal("graphic/rogue_attack.png")),
            Gdx.audio.newSound(Gdx.files.internal("rogue_run.wav")),
            Gdx.audio.newSound(Gdx.files.internal("rogue_attack.mp3")),
            x, y, speed
        );
    }

    @Override
    public void performSkill() {
        System.out.println("Rogue is teleporting!");
    }
}
