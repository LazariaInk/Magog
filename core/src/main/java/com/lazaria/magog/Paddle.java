package com.lazaria.magog;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Paddle {
    private float x, y;
    private float width, height;
    private Texture texture;

    public Paddle(float width, float height) {
        this.width = width;
        this.height = height;
        this.texture = new Texture("paddle.png");
    }

    public void update(float knightX, float knightY, float knightWidth, float knightHeight) {
        this.x = knightX + knightWidth / 2 - width / 2;
        this.y = knightY + knightHeight;
    }

    public void render(SpriteBatch batch) {
        batch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
        batch.draw(texture, x, y, width, height);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
