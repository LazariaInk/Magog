package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Character {
    private Texture idleTexture;
    private Animation<TextureRegion> idleAnimation;
    private Texture runTexture;
    private Animation<TextureRegion> runAnimation;
    private float x;
    private float y;
    private float speed;
    private boolean movingRight = false;
    private boolean movingLeft = false;
    private boolean facingRight = true;
    private float elapsedTime = 0f;
    private Sound runSound;
    private long runSoundId = -1;

    private Character(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.speed = builder.speed;

        this.idleTexture = builder.idleTexture;
        TextureRegion[][] tmpIdleFrames = TextureRegion.split(idleTexture, idleTexture.getWidth() / 2, idleTexture.getHeight());
        Array<TextureRegion> idleFrames = new Array<>();
        for (TextureRegion[] row : tmpIdleFrames) {
            for (TextureRegion region : row) {
                idleFrames.add(region);
            }
        }
        this.idleAnimation = new Animation<>(0.3f, idleFrames, Animation.PlayMode.LOOP);

        this.runTexture = builder.runTexture;
        TextureRegion[][] tmpRunFrames = TextureRegion.split(runTexture, runTexture.getWidth() / 7, runTexture.getHeight());
        Array<TextureRegion> runFrames = new Array<>();
        for (TextureRegion[] row : tmpRunFrames) {
            for (TextureRegion region : row) {
                runFrames.add(region);
            }
        }
        this.runAnimation = new Animation<>(0.1f, runFrames, Animation.PlayMode.LOOP);

        this.runSound = builder.runSound;
    }

    public void update(float delta) {
        elapsedTime += delta;
        movingLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        movingRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        if (movingRight) {
            x += speed * delta;
            facingRight = true;
            playRunSound();
        } else if (movingLeft) {
            x -= speed * delta;
            facingRight = false;
            playRunSound();
        } else {
            stopRunSound();
        }
    }

    private void playRunSound() {
        if (runSound != null && runSoundId == -1) {
            runSoundId = runSound.loop(0.1f);
        }
    }

    private void stopRunSound() {
        if (runSoundId != -1) {
            runSound.stop(runSoundId);
            runSoundId = -1;
        }
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation;
        if (movingLeft || movingRight) {
            currentAnimation = runAnimation;
        } else {
            currentAnimation = idleAnimation;
        }
        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (facingRight && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (!facingRight && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        float scaleFactor = 4.0f;
        float knightWidth = currentFrame.getRegionWidth() * scaleFactor;
        float knightHeight = currentFrame.getRegionHeight() * scaleFactor;

        batch.draw(currentFrame, x, y, knightWidth, knightHeight);
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void dispose() {
        idleTexture.dispose();
        runTexture.dispose();
        if (runSound != null) {
            runSound.dispose();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return 64f * 4;  // Example character width (based on scaling factor used in render method)
    }

    public float getHeight() {
        return 64f * 4;  // Example character height (based on scaling factor used in render method)
    }

    public static class Builder {
        private Texture idleTexture;
        private Texture runTexture;
        private Sound runSound;
        private float x = 0;
        private float y = 0;
        private float speed = 600f;

        public Builder setIdleTexture(Texture idleTexture) {
            this.idleTexture = idleTexture;
            return this;
        }

        public Builder setRunTexture(Texture runTexture) {
            this.runTexture = runTexture;
            return this;
        }

        public Builder setRunSound(Sound runSound) {
            this.runSound = runSound;
            return this;
        }

        public Builder setPosition(float x, float y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Character build() {
            if (idleTexture == null || runTexture == null) {
                throw new IllegalStateException("Idle and Run textures must be set before building Character.");
            }
            return new Character(this);
        }
    }
}
