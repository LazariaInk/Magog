package com.lazaria.magog.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.lazaria.magog.Settings;

public class ButtonFactory {
    private Actor fadeActor;

    public ButtonFactory() {
        fadeActor = new Actor();
        fadeActor.setColor(1f, 1f, 1f, 0f);
        fadeActor.setSize(1920, 1080);
    }

    public Container<ImageButton> createButton(String texturePath, float width, float height, float posX, float posY,
                                               Class<? extends Screen> screenClass, Stage stage) {
        Texture buttonTexture = new Texture(Gdx.files.internal(texturePath));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        ImageButton button = new ImageButton(drawable);
        button.setSize(width, height);
        Container<ImageButton> container = new Container<>(button);
        container.setTransform(true);
        container.size(width, height);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setPosition(posX, posY);
        if (!stage.getActors().contains(fadeActor, true)) {
            stage.addActor(fadeActor);
        }
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                container.addAction(Actions.sequence(
                    Actions.scaleTo(1.1f, 1.1f, 0.2f),
                    Actions.scaleTo(1f, 1f, 0.2f)
                ));
                Settings.getInstance().getSoundManager().playSoundEffect();
                try {
                    Screen nextScreen = screenClass.getDeclaredConstructor(Settings.class).newInstance(Settings.getInstance());
                    fadeActor.addAction(Actions.sequence(
                        Actions.alpha(0f),
                        Actions.fadeIn(0.5f),
                        Actions.run(() -> Settings.getInstance().setScreen(nextScreen))
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return container;
    }
}
