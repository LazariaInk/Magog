package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Mage extends Character {
    public Mage(float x, float y, float speed) {
        super(
            new Texture(Gdx.files.internal("graphic/mage_idle.png")),
            new Texture(Gdx.files.internal("graphic/mage_run.png")),
            new Texture(Gdx.files.internal("graphic/mage_runattack.png")),
            new Texture(Gdx.files.internal("graphic/mage_attack.png")),
            Gdx.audio.newSound(Gdx.files.internal("mage_run.wav")),
            Gdx.audio.newSound(Gdx.files.internal("mage_attack.mp3")),
            x, y, speed
        );
    }

    @Override
    public void performSkill() {
        System.out.println("Mage is casting a fireball!");
    }
}
