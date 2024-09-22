package com.lazaria.magog.screen.menu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.MathUtils;

public class FallingLeaf extends Actor {
    private Texture leafTexture;
    private float speed;
    private float rotationSpeed;
    private float worldWidth;

    public FallingLeaf(Texture leafTexture, float worldWidth) {
        this.leafTexture = leafTexture;
        this.worldWidth = worldWidth;

        setX(MathUtils.random(0, worldWidth));
        setY(MathUtils.random(1080, 1280));

        setWidth(64);
        setHeight(64);

        speed = MathUtils.random(50f, 150f);
        rotationSpeed = MathUtils.random(-30f, 30f);
    }

    @Override
    public void act(float delta) {
        setY(getY() - speed * delta);

        setRotation(getRotation() + rotationSpeed * delta);

        if (getY() + getHeight() < 0) {
            setY(MathUtils.random(1080, 1280));
            setX(MathUtils.random(0, worldWidth));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float leftBoundary = 0.25f * worldWidth;
        float rightBoundary = 0.75f * worldWidth;

        if (getX() >= leftBoundary && getX() <= rightBoundary) {
            batch.draw(leafTexture, getX(), getY(), getWidth() / 2, getHeight() / 2,
                getWidth(), getHeight(), 1f, 1f, getRotation(), 0, 0,
                leafTexture.getWidth(), leafTexture.getHeight(), false, false);
        }
    }
}
