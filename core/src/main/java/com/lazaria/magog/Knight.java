package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Knight extends Character {
    private Paddle paddle;
    private boolean shieldActive = false;
    private float shieldDuration = 3.0f;
    private float shieldTimer = 0f;

    private Texture defendTexture;
    private Animation<TextureRegion> defendAnimation;

    public Knight(float x, float y, float speed, Paddle paddle) {
        super(
            new Texture(Gdx.files.internal("knight_idle.png")),
            new Texture(Gdx.files.internal("knight_run.png")),
            new Texture(Gdx.files.internal("knight_runattack.png")),
            new Texture(Gdx.files.internal("knight_attack.png")),
            Gdx.audio.newSound(Gdx.files.internal("knight_run.wav")),
            Gdx.audio.newSound(Gdx.files.internal("knight_attack.mp3")),
            x, y, speed
        );
        this.paddle = paddle;

        this.defendTexture = new Texture(Gdx.files.internal("defend.png"));
        this.defendAnimation = createAnimation(defendTexture, 5, 0.2f);
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    @Override
    public void performSkill() {
        if (!shieldActive) {
            shieldActive = true;
            shieldTimer = shieldDuration;
            paddle.setWidth(paddle.getWidth() + 180);
            System.out.println("Knight is holding the shield!");
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) {
                shieldActive = false;
                paddle.setWidth(paddle.getWidth() - 180);
                System.out.println("Shield deactivated!");
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation;

        if (shieldActive) {
            currentAnimation = defendAnimation;
        } else {
            currentAnimation = attacking
                ? (movingLeft || movingRight ? runAttackAnimation : attackAnimation)
                : (movingLeft || movingRight ? runAnimation : idleAnimation);
        }

        TextureRegion currentFrame = currentAnimation.getKeyFrame(elapsedTime, true);
        if (facingRight && currentFrame.isFlipX()) currentFrame.flip(true, false);
        if (!facingRight && !currentFrame.isFlipX()) currentFrame.flip(true, false);

        float scaleFactor = 4.0f;
        float knightWidth = currentFrame.getRegionWidth() * scaleFactor;
        float knightHeight = currentFrame.getRegionHeight() * scaleFactor;
        batch.draw(currentFrame, x, y, knightWidth, knightHeight);
    }

    @Override
    public void dispose() {
        super.dispose();
        defendTexture.dispose();
    }
    public Animation<TextureRegion> createAnimation(Texture texture, int frameCount, float frameDuration) {
        TextureRegion[][] tmpFrames = TextureRegion.split(texture, texture.getWidth() / frameCount, texture.getHeight());
        Array<TextureRegion> frames = new Array<>();
        for (TextureRegion[] row : tmpFrames) {
            for (TextureRegion region : row) {
                frames.add(region);
            }
        }
        return new Animation<>(frameDuration, frames, Animation.PlayMode.LOOP);
    }
}
