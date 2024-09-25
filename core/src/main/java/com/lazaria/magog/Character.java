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
    private Texture runAttackTexture, attackTexture;
    private Animation<TextureRegion> runAttackAnimation, attackAnimation;
    private float x, y, speed, elapsedTime = 0f, attackTime = 0f;
    private boolean movingRight = false, movingLeft = false, facingRight = true;
    private boolean attacking = false;
    private final float attackDuration = 1f;
    private Sound runSound, attackSound;
    private long runSoundId = -1, attackSoundId = -1;

    private Character(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.speed = builder.speed;
        this.idleTexture = builder.idleTexture;
        this.idleAnimation = createAnimation(idleTexture, 2, 0.3f);
        this.runTexture = builder.runTexture;
        this.runAnimation = createAnimation(runTexture, 7, 0.1f);
        this.runAttackTexture = builder.runAttackTexture;
        this.runAttackAnimation = createAnimation(runAttackTexture, 6, 0.1f);
        this.attackTexture = builder.attackTexture;
        this.attackAnimation = createAnimation(attackTexture, 4, 0.2f);
        this.runSound = builder.runSound;
        this.attackSound = builder.attackSound;
    }

    private Animation<TextureRegion> createAnimation(Texture texture, int frameCount, float frameDuration) {
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, texture.getWidth() / frameCount, texture.getHeight());
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion[] row : tmpFrames) {
            for (TextureRegion region : row) {
                frames.add(region);
            }
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        elapsedTime += delta;

        // Handle movement input
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

        if (attacking) {
            attackTime += delta;
            if (attackTime > attackDuration) {
                attacking = false;
                attackTime = 0f;
                stopAttackSound();
            }
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return 64f * 4;
    }

    public float getHeight() {
        return 64f * 4;
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

    private void playAttackSound() {
        if (attackSound != null && attackSoundId == -1) {
            attackSoundId = attackSound.play(1.0f);
        }
    }

    private void stopAttackSound() {
        if (attackSoundId != -1) {
            attackSound.stop(attackSoundId);
            attackSoundId = -1;
        }
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
        this.attackTime = 0f;
        if (attacking) {
            playAttackSound();
        }
    }

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation;
        if (attacking) {
            if (movingLeft || movingRight) {
                currentAnimation = runAttackAnimation;
            } else {
                currentAnimation = attackAnimation;
            }
        } else if (movingLeft || movingRight) {
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

    public void dispose() {
        idleTexture.dispose();
        runTexture.dispose();
        runAttackTexture.dispose();
        attackTexture.dispose();
        if (runSound != null) {
            runSound.dispose();
        }
        if (attackSound != null) {
            attackSound.dispose();
        }
    }

    public static class Builder {
        private Texture idleTexture, runTexture, runAttackTexture, attackTexture;
        private Sound runSound, attackSound;
        private float x = 0, y = 0, speed = 600f;

        public Builder setIdleTexture(Texture idleTexture) {
            this.idleTexture = idleTexture;
            return this;
        }

        public Builder setRunTexture(Texture runTexture) {
            this.runTexture = runTexture;
            return this;
        }

        public Builder setRunAttackTexture(Texture runAttackTexture) {
            this.runAttackTexture = runAttackTexture;
            return this;
        }

        public Builder setAttackTexture(Texture attackTexture) {
            this.attackTexture = attackTexture;
            return this;
        }

        public Builder setRunSound(Sound runSound) {
            this.runSound = runSound;
            return this;
        }

        public Builder setAttackSound(Sound attackSound) {
            this.attackSound = attackSound;
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
            if (idleTexture == null || runTexture == null || runAttackTexture == null || attackTexture == null) {
                throw new IllegalStateException("All textures must be set before building Character.");
            }
            return new Character(this);
        }
    }
}
