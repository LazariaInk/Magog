package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Crystal {
    private float x, y;
    private int hits;
    private final Animation<TextureRegion> crystalAnimation;
    private float elapsedTime;
    private boolean destroyed;
    private static final int MAX_HITS = 1;
    private final float radius = 80f;

    private final Sound crystalHitSound;

    public Crystal(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.hits = 0;
        this.destroyed = false;

        String filePath = "graphic/crystal/crystal" + type + ".png";
        Texture texture = new Texture(Gdx.files.internal(filePath));

        TextureRegion[][] tempFrames = TextureRegion.split(texture, texture.getWidth() / 24, texture.getHeight());

        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 24; i++) {
            frames.add(tempFrames[0][i]);
        }

        crystalAnimation = new Animation<>(0.05f, frames, Animation.PlayMode.LOOP);
        crystalHitSound = Gdx.audio.newSound(Gdx.files.internal("sound/crystal.wav"));
    }

    public void update(float delta) {
        if (!destroyed) {
            elapsedTime += delta;
        }
    }

    public void render(SpriteBatch batch) {
        if (!destroyed) {
            TextureRegion currentFrame = crystalAnimation.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrame, x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    public boolean collidesWith(Ball ball) {
        float distance = (float) Math.sqrt(Math.pow(ball.getX() - x, 2) + Math.pow(ball.getY() - y, 2));
        return distance < (ball.getRadius() + radius);
    }

    public void hit() {
        hits++;
        crystalHitSound.play();
        if (hits >= MAX_HITS) {
            destroyed = true;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void dispose() {
        for (TextureRegion region : crystalAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }
}
