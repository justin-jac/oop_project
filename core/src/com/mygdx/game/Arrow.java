package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Arrow extends Sprite {
    public static float speedArrow = 300, spawnArrow = 0.6f;
    public float XSpeed = 0, YSpeed = 0, x, y, sizeX, sizeY, angle;
    public boolean changeDir;
    public boolean delete;

    public Arrow() {
        super(MainProyek.getAssetManager().get("Arrow.png", Texture.class));
        Initialized();
        delete = false;
    }

    public Arrow(Texture texture) {
        super(texture);
        Initialized();
    }

    protected void Initialized() {
        MainProyek parentGame = (MainProyek) Gdx.app.getApplicationListener();
        AssetManager assetManager = parentGame.getAssetManager();

        this.setTexture(assetManager.get("Arrow.png", Texture.class));
        changeDir = true;
        setSizeX(this.getWidth());
        setSizeY(this.getHeight());
    }

    public void Update() {
        float delta = Gdx.graphics.getDeltaTime();
        this.setX(this.getX() + XSpeed * delta);
        this.setY(this.getY() + YSpeed * delta);

        float angleRadian = MathUtils.atan2(YSpeed, XSpeed);
        double pi = Math.PI;
        angle = angleRadian * 180 / (float) pi;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(this, getX() - 32, getY() - 30, 0, 28, 64, 56, 1.2f, 1.2f, angle);
    }


    public void setXSpeed(float XSpeed) {
        this.XSpeed = XSpeed;
    }

    public void setYSpeed(float YSpeed) {
        this.YSpeed = YSpeed;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSizeX() {
        return sizeX;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public static void setSpeedArrow(float speedArrow) {
        Arrow.speedArrow = speedArrow;
        if (Arrow.speedArrow > 700)
            Arrow.speedArrow = 700;
    }

    public static void setSpawnArrow(float spawnArrow) {
        Arrow.spawnArrow = spawnArrow;
        if (Arrow.spawnArrow < 0.2f)
            Arrow.spawnArrow = 0.2f;
    }

}