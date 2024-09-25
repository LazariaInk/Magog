package com.lazaria.magog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ball {
    private float x, y;
    private float speedX, speedY;
    private float speed;
    private Texture texture;
    private float radius;

    public Ball(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.texture = new Texture("ball.png");
        this.radius = 20f;
        this.speedX = speed;
        this.speedY = -speed;
    }

    public void update(float delta, Character knight, Paddle paddle) {
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

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - radius, y - radius, radius * 2, radius * 2);
    }

    public void dispose() {
        texture.dispose();
    }
}
