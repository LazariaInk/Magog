package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public abstract class Character {
    protected Texture idleTexture, runTexture, runAttackTexture, attackTexture;
    protected Animation<TextureRegion> idleAnimation, runAnimation, runAttackAnimation, attackAnimation;
    protected float x, y, speed, elapsedTime = 0f, attackTime = 0f;
    protected boolean movingRight = false, movingLeft = false, facingRight = true;
    protected boolean attacking = false;
    protected final float attackDuration = 1f;
    protected Sound runSound, attackSound;
    private long runSoundId = -1, attackSoundId = -1;

    // Constructor for common Character attributes
    protected Character(Texture idleTexture, Texture runTexture, Texture runAttackTexture, Texture attackTexture,
                        Sound runSound, Sound attackSound, float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.idleTexture = idleTexture;
        this.idleAnimation = createAnimation(idleTexture, 2, 0.3f);
        this.runTexture = runTexture;
        this.runAnimation = createAnimation(runTexture, 7, 0.1f);
        this.runAttackTexture = runAttackTexture;
        this.runAttackAnimation = createAnimation(runAttackTexture, 6, 0.1f);
        this.attackTexture = attackTexture;
        this.attackAnimation = createAnimation(attackTexture, 4, 0.2f);
        this.runSound = runSound;
        this.attackSound = attackSound;
    }

    protected Animation<TextureRegion> createAnimation(Texture texture, int frameCount, float frameDuration) {
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, texture.getWidth() / frameCount, texture.getHeight());
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion[] row : tmpFrames) {
            for (TextureRegion region : row) {
                frames.add(region);
            }
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }

    public Texture getIdleTexture() {
        return idleTexture;
    }

    public void setIdleTexture(Texture idleTexture) {
        this.idleTexture = idleTexture;
    }

    public Texture getRunTexture() {
        return runTexture;
    }

    public float getWidth() {
        return 64f * 4;
    }

    public float getHeight() {
        return 64f * 4;
    }


    public void setRunTexture(Texture runTexture) {
        this.runTexture = runTexture;
    }

    public Texture getRunAttackTexture() {
        return runAttackTexture;
    }

    public void setRunAttackTexture(Texture runAttackTexture) {
        this.runAttackTexture = runAttackTexture;
    }

    public Texture getAttackTexture() {
        return attackTexture;
    }

    public void setAttackTexture(Texture attackTexture) {
        this.attackTexture = attackTexture;
    }

    public Animation<TextureRegion> getIdleAnimation() {
        return idleAnimation;
    }

    public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
        this.idleAnimation = idleAnimation;
    }

    public Animation<TextureRegion> getRunAnimation() {
        return runAnimation;
    }

    public void setRunAnimation(Animation<TextureRegion> runAnimation) {
        this.runAnimation = runAnimation;
    }

    public Animation<TextureRegion> getRunAttackAnimation() {
        return runAttackAnimation;
    }

    public void setRunAttackAnimation(Animation<TextureRegion> runAttackAnimation) {
        this.runAttackAnimation = runAttackAnimation;
    }

    public Animation<TextureRegion> getAttackAnimation() {
        return attackAnimation;
    }

    public void setAttackAnimation(Animation<TextureRegion> attackAnimation) {
        this.attackAnimation = attackAnimation;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public float getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(float attackTime) {
        this.attackTime = attackTime;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public float getAttackDuration() {
        return attackDuration;
    }

    public Sound getRunSound() {
        return runSound;
    }

    public void setRunSound(Sound runSound) {
        this.runSound = runSound;
    }

    public Sound getAttackSound() {
        return attackSound;
    }

    public void setAttackSound(Sound attackSound) {
        this.attackSound = attackSound;
    }

    public long getRunSoundId() {
        return runSoundId;
    }

    public void setRunSoundId(long runSoundId) {
        this.runSoundId = runSoundId;
    }

    public long getAttackSoundId() {
        return attackSoundId;
    }

    public void setAttackSoundId(long attackSoundId) {
        this.attackSoundId = attackSoundId;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
        this.attackTime = 0f;
        if (attacking) {
            playAttackSound();
        }
    }

    // Common update logic
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

        if (attacking) {
            attackTime += delta;
            if (attackTime > attackDuration) {
                attacking = false;
                attackTime = 0f;
                stopAttackSound();
            }
        }
    }

    // Abstract method for performing unique skills
    public abstract void performSkill();

    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = attacking
            ? (movingLeft || movingRight ? runAttackAnimation : attackAnimation)
            : (movingLeft || movingRight ? runAnimation : idleAnimation);

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (facingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (!facingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);

        float scaleFactor = 4.0f;
        float knightWidth = currentFrame.getRegionWidth() * scaleFactor;
        float knightHeight = currentFrame.getRegionHeight() * scaleFactor;
        batch.draw(currentFrame, x, y, knightWidth, knightHeight);
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

    public void dispose() {
        idleTexture.dispose();
        runTexture.dispose();
        runAttackTexture.dispose();
        attackTexture.dispose();
        if (runSound != null) runSound.dispose();
        if (attackSound != null) attackSound.dispose();
    }
}
