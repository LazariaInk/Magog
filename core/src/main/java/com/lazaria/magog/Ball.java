package com.lazaria.magog;

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
        this.radius = 20f;  // Assuming the ball has a radius of 20 pixels
        this.speedX = speed;  // Initial speed on X-axis
        this.speedY = -speed; // Initial speed on Y-axis (downwards)
    }

    public void update(float delta, Character knight, Paddle paddle) {
        // Update position
        x += speedX * delta;
        y += speedY * delta;

        // Check collision with paddle
        if (collidesWith(paddle)) {
            // Calculate bounce direction based on where it hit the paddle
            float hitPosition = (x - (paddle.getX() + paddle.getWidth() / 2)) / (paddle.getWidth() / 2);
            speedY = Math.abs(speedY);  // Reverse Y direction (ball goes up)
            speedX = hitPosition * speed;  // Adjust X direction based on hit position
        }

        // Check collision with walls
        if (x < 0 || x > 1920 - radius * 2) {
            speedX = -speedX;  // Reverse direction on X-axis
        }

        if (y > 1080 - radius * 2) {
            speedY = -speedY;  // Bounce back from the top
        }

        // Check if the ball goes out of bounds (below the screen)
        if (y < 0) {
            resetBall();  // You can reset the ball or handle a life loss here
        }
    }

    // Method to reset ball position and speed
    private void resetBall() {
        x = 960;
        y = 540;
        speedX = speed;
        speedY = -speed;
    }

    // Collision detection with the paddle
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
