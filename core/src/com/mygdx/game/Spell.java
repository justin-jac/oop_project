package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Spell extends Arrow {
    public Spell() {
        super(MainProyek.getAssetManager().get("WraithSpell.png", Texture.class));
        Initialized();
        delete = false;
    }

    public Spell(Texture texture) {
        super(texture);
        Initialized();
    }

    @Override
    protected void Initialized() {
        MainProyek parentGame = (MainProyek) Gdx.app.getApplicationListener();
        AssetManager assetManager = parentGame.getAssetManager();

        this.setTexture(assetManager.get("WraithSpell.png", Texture.class));
        setSizeX(this.getWidth());
        setSizeY(this.getHeight());
    }


    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this, getX() - 15, getY() - 15, 15, 15, 30, 30,
                1, 1, angle);
    }
}
