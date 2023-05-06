package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainProyek extends Game implements InputProcessor {
    public static AssetManager assetManager;
    BitmapFont loadingFont;
    final public static int virtualWidth = 1280;
    final public static int virtualHeight = 720;

    @Override
    public void create() {
        assetManager = new AssetManager();


        assetManager.load("Arrow.png", Texture.class);
        assetManager.load("Bow.png", Texture.class);
        assetManager.load("Castle.png", Texture.class);
        assetManager.load("Castle2.png", Texture.class);
        assetManager.load("Castle3.png", Texture.class);
        assetManager.load("Background.png", Texture.class);

        assetManager.load("enemyHit.wav", Sound.class);
        assetManager.load("bowShoot.wav", Sound.class);

        assetManager.load("WalkingMino.png", Texture.class);
        assetManager.load("AttackingMino.png", Texture.class);
        assetManager.load("DyingMino.png", Texture.class);
        assetManager.load("HurtMino.png", Texture.class);

        assetManager.load("RunningGolem.png", Texture.class);
        assetManager.load("HurtGolem.png", Texture.class);
        assetManager.load("ExplodeGolem.png", Texture.class);
        assetManager.load("DyingGolem.png", Texture.class);

        assetManager.load("WalkingWraith.png", Texture.class);
        assetManager.load("AttackingWraith.png", Texture.class);
        assetManager.load("HurtWraith.png", Texture.class);
        assetManager.load("DyingWraith.png", Texture.class);
        assetManager.load("WraithSpell.png", Texture.class);

        assetManager.load("Background.jpg", Texture.class);//Background Menu
        assetManager.load("Revolution.mp3", Music.class);
        assetManager.load("GameMusic.mp3", Music.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("BLKCHCRY.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
        parameter.flip = true;
        loadingFont = generator.generateFont(parameter);
        generator.dispose();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));


        // First, let's define the params and then load our smaller font
        FreetypeFontLoader.FreeTypeFontLoaderParameter mySmallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mySmallFont.fontFileName = "BLKCHCRY.ttf";
        mySmallFont.fontParameters.size = 32;
        mySmallFont.fontParameters.flip = false;
        assetManager.load("smallfont.ttf", BitmapFont.class, mySmallFont);

// Next, let's define the params and then load our bigger font
        FreetypeFontLoader.FreeTypeFontLoaderParameter myBigFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        myBigFont.fontFileName = "BLKCHCRY.ttf";
        myBigFont.fontParameters.size = 64;
        myBigFont.fontParameters.flip = true;
        assetManager.load("bigfont.ttf", BitmapFont.class, myBigFont);

        FreetypeFontLoader.FreeTypeFontLoaderParameter myBigFontui = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        myBigFontui.fontFileName = "BLKCHCRY.ttf";
        myBigFontui.fontParameters.size = 64;
        assetManager.load("bigfontui.ttf", BitmapFont.class, myBigFontui);

        SkinLoader.SkinParameter skinParam = new SkinLoader.SkinParameter("clean-crispy-ui.atlas");
        assetManager.load("clean-crispy-ui.json", Skin.class, skinParam);

        this.setScreen(new Loading(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        //camera.setToOrtho(true, width, height);

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public static AssetManager getAssetManager() {
        return assetManager;
    }

    public BitmapFont getLoadingFont() {
        return loadingFont;
    }

    public static TextureRegion[] CreateAnimationFrames(Texture tex, int frameWidth, int frameHeight, int frameCount, boolean flipx, boolean flipy) {
        TextureRegion[][] tmp = TextureRegion.split(tex, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCount];
        int index = 0;
        int row = tex.getHeight() / frameHeight;
        int col = tex.getWidth() / frameWidth;
        for (int i = 0; i < row && index < frameCount; i++) {
            for (int j = 0; j < col && index < frameCount; j++) {
                frames[index] = tmp[i][j];
                frames[index].flip(flipx, flipy);
                index++;
            }
        }
        return frames;
    }
}