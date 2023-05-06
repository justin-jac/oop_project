package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Loading implements Screen, InputProcessor {
    Game parentGame;
    AssetManager assetManager;
    SpriteBatch batch;
    BitmapFontCache text;
    OrthographicCamera camera;
    Viewport viewport;

    public Loading() {
        parentGame = (Game) Gdx.app.getApplicationListener();
        Initialize();
    }

    public Loading(Game g) {
        parentGame = g;
        Initialize();
    }

    protected void Initialize() {
        assetManager = ((MainProyek) parentGame).getAssetManager();
        camera = new OrthographicCamera(MainProyek.virtualWidth, MainProyek.virtualHeight);
        camera.setToOrtho(true, MainProyek.virtualWidth, MainProyek.virtualHeight);
        viewport = new FitViewport(MainProyek.virtualWidth, MainProyek.virtualHeight, camera);
        batch = new SpriteBatch();

        text = new BitmapFontCache(((MainProyek) parentGame).getLoadingFont());
        text.setColor(Color.WHITE);
        text.setText("Loading 0%", 480, 330);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        draw();
        batch.end();
        update();
    }

    public void draw() {
        text.draw(batch);
    }

    public void update() {
        if (assetManager.update()) {
            this.SwitchToMenuScreen();
        }
        float progress = assetManager.getProgress() * 100;
        String loadtext = String.format("Loading %.2f%%", progress);
        text.setText(loadtext, 480, 330);
    }

    public void SwitchToMenuScreen() {
        if (assetManager.isFinished() && Gdx.input.isTouched())
            parentGame.setScreen(new Menu(parentGame));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
