package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Ball {
    private float x, y;
    private float speedX, speedY;
    private float speed;
    private float radius;
    private float elapsedTime = 0f;

    private Animation<TextureRegion> ballAnimation;

    public Ball(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.radius = 20f;
        this.speedX = speed;
        this.speedY = -speed;

        // Create the ball animation using 5 frames
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= 5; i++) {
            Texture ballTexture = new Texture(Gdx.files.internal("ball" + i + ".png"));
            frames.add(new TextureRegion(ballTexture));
        }
        ballAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    public void update(float delta, Character knight, Paddle paddle) {
        elapsedTime += delta;
        x += speedX * delta;
        y += speedY * delta;

        if (collidesWith(paddle)) {
            float hitPosition = (x - (paddle.getX() + paddle.getWidth() / 2)) / (paddle.getWidth() / 2);
            speedY = Math.abs(speedY);
            speedX = hitPosition * speed;

            if (knightMoving()) {
                knight.setAttacking(true);
            } else {
                knight.setAttacking(true);
            }
        }
        if (x < 0 || x > 1920 - radius * 2) {
            speedX = -speedX;
        }
        if (y > 1080 - radius * 2) {
            speedY = -speedY;
        }
        if (y < 0) {
            resetBall();
            knight.setAttacking(false);
        }
    }

    private boolean knightMoving() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D);
    }

    private void resetBall() {
        x = 960;
        y = 1000;
        speedX = speed;
        speedY = -speed;
    }

    private boolean collidesWith(Paddle paddle) {
        return x + radius > paddle.getX() && x - radius < paddle.getX() + paddle.getWidth()
            && y - radius < paddle.getY() + paddle.getHeight() && y + radius > paddle.getY();
    }

    // Calculate the rotation angle based on the ball's movement
    private float calculateRotationAngle() {
        return (float) Math.toDegrees(Math.atan2(speedY, speedX)) - 90; // Adjust by 90 degrees to match the ball's orientation
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = ballAnimation.getKeyFrame(elapsedTime, true);
        float rotationAngle = calculateRotationAngle(); // Calculate the angle based on movement

        // Draw the ball with rotation
        batch.draw(currentFrame,
            x - radius, y - radius,       // Position the ball at its center
            radius, radius,               // Origin of rotation is the ball's center
            radius * 2, radius * 2,       // Width and height of the ball
            1, 1,                         // Scaling
            rotationAngle);               // Rotation based on movement angle
    }

    public void dispose() {
        for (TextureRegion region : ballAnimation.getKeyFrames()) {
            region.getTexture().dispose();  // Dispose each texture used in the animation
        }
    }
}
