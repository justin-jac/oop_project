package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Menu<TextureButton> implements Screen, InputProcessor {
    Game parentGame;
    AssetManager assetManager;
    SpriteBatch batch;
    OrthographicCamera camera, stageCamera;
    Viewport viewport;

    Stage stage;
    //Table table;
    Label titleLabel;
    Slider optionMusicSlider;
    TextField musicText;
    TextButton playButton, optionButton, optionDoneButton;
    Window optionWindow;
    Music music;
    public static Float musicVolume = 0.5f;

    InputMultiplexer multiInput;

    public Menu() {
        parentGame = (Game) Gdx.app.getApplicationListener();
        Initialize();
    }

    public Menu(Game g) {
        parentGame = g;
        Initialize();
    }

    protected void Initialize() {
        assetManager = ((MainProyek) parentGame).getAssetManager();
        camera = new OrthographicCamera(MainProyek.virtualWidth, MainProyek.virtualHeight);
        camera.setToOrtho(false, MainProyek.virtualWidth, MainProyek.virtualHeight);
        viewport = new FitViewport(MainProyek.virtualWidth, MainProyek.virtualHeight, camera);
        batch = new SpriteBatch();

        multiInput = new InputMultiplexer();
        multiInput.addProcessor(this);

        stageCamera = new OrthographicCamera(MainProyek.virtualWidth, MainProyek.virtualHeight);
        stageCamera.setToOrtho(false, MainProyek.virtualWidth, MainProyek.virtualHeight);
        stage = new Stage(new FitViewport(MainProyek.virtualWidth, MainProyek.virtualHeight, stageCamera));

        multiInput.addProcessor(stage);

        Skin mySkin = assetManager.get("clean-crispy-ui.json", Skin.class);

        titleLabel = new Label("Defender 0.5", mySkin);
        Label.LabelStyle style = new Label.LabelStyle(titleLabel.getStyle());
        style.font = assetManager.get("bigfontui.ttf", BitmapFont.class);
        titleLabel.setStyle(style);
        titleLabel.setWidth(720);
        titleLabel.setX(280);
        titleLabel.setY(450);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        stage.addActor(titleLabel);

        playButton = new TextButton("Play", mySkin);
        playButton.setHeight(70);
        playButton.setWidth(150);
        playButton.setPosition(570, 300);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x >= 0 && y >= 0 && x <= event.getTarget().getWidth() && y <= event.getTarget().getHeight()) {
                    parentGame.setScreen(new GameScreen());
                    music.stop();
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);


        optionButton = new TextButton("Option", mySkin);
        optionButton.setHeight(70);
        optionButton.setWidth(150);
        optionButton.setPosition(570, 200);
        optionButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x >= 0 && y >= 0 && x <= event.getTarget().getWidth() && y <= event.getTarget().getHeight()) {
                    optionWindow.setVisible(true);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(optionButton);

        optionWindow = new Window("Option", mySkin);
        optionWindow.setHeight(400);
        optionWindow.setWidth(560);
        optionWindow.setPosition(350, 360 - optionWindow.getHeight() / 2);
        optionWindow.setMovable(false);
        optionWindow.setModal(true);
        optionWindow.setResizable(false);
        optionWindow.setVisible(false);
        optionWindow.getTitleLabel().setAlignment(Align.center);
        stage.addActor(optionWindow);

        musicText = new TextArea("Music", mySkin);
        musicText.setPosition(90, 230);
        musicText.setWidth(60);
        musicText.setDisabled(true);
        optionWindow.addActor(musicText);

        optionMusicSlider = new Slider(0f, 1f, 0.1f, false, mySkin);
        optionMusicSlider.setPosition(160, 235);
        optionMusicSlider.setWidth(optionWindow.getWidth() / 2);
        optionMusicSlider.setValue(musicVolume);
        optionWindow.addActor(optionMusicSlider);
        optionMusicSlider.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (optionMusicSlider.isDragging()) {
                    musicVolume = optionMusicSlider.getValue();
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
            }
        });


        optionDoneButton = new TextButton("Done", mySkin);
        optionDoneButton.setWidth(120);
        optionDoneButton.setHeight(36);
        optionDoneButton.setPosition(optionWindow.getWidth() / 2 - optionDoneButton.getWidth() / 2, 72);
        optionDoneButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x >= 0 && y >= 0 && x <= event.getTarget().getWidth() && y <= event.getTarget().getHeight()) {
                    optionWindow.setVisible(false);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        optionWindow.addActor(optionDoneButton);
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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiInput);
        music = assetManager.get("Revolution.mp3", Music.class);
        music.play();
        music.setLooping(true);
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
        stage.act();
        stage.draw();
    }

    public void draw() {
        Texture background = assetManager.get("Background.jpg", Texture.class);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 10; j++)
                batch.draw(background, j * 1280, i * 720);
        }

    }

    public void update() {
        assetManager.update();
        music.setVolume(musicVolume);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height);
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

    }
}
