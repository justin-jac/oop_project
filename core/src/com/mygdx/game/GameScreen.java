package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen, InputProcessor {
    enum State {
        Playing,
        Paused,
        GameOver
    }

    Game parentGame;
    public static AssetManager assetManager;
    SpriteBatch batch;
    BitmapFontCache castleTxt, scoreTxt;
    OrthographicCamera camera, stageCamera;
    Viewport viewport;

    Random randomizer;

    Stage stage;
    InputMultiplexer multiInput;
    State state;
    ArrayList<Arrow> arrowList;

    float stateTime, score;
    int finalScore;
    float spawnEnemy, timeSpeed, timeSpawArr, timeEnemy;
    Castle cs;
    Skin mySkin;

    ArrayList<Enemy> enemyList;
    Sound bowSound;

    Window optionWindow, gameoverWindow;
    Music music;
    public static float musicVolume, soundVolume;
    TextButton optionDoneButton, playAgainButton, exitButton;
    Label scoreLabel;
    Slider optionMusicSlider, optionSoundSlider;
    TextField musicText, soundText, scoreText;

    public GameScreen() {
        parentGame = (Game) Gdx.app.getApplicationListener();
        Initialize();
    }

    public GameScreen(Game g) {
        parentGame = g;
        Initialize();
    }

    protected void Initialize() {
        state = State.Playing;
        musicVolume = Menu.musicVolume;
        soundVolume = Menu.musicVolume;
        assetManager = ((MainProyek) parentGame).getAssetManager();

        camera = new OrthographicCamera(MainProyek.virtualWidth, MainProyek.virtualHeight);
        camera.setToOrtho(false, MainProyek.virtualWidth, MainProyek.virtualHeight);
        viewport = new FitViewport(MainProyek.virtualWidth, MainProyek.virtualHeight, camera);
        batch = new SpriteBatch();

        cs = new Castle();
        score = 0;
        randomizer = new Random();

        GroundEnemy m = new GroundEnemy();
        m.setX(1280);
        m.setY(200);
        enemyList = new ArrayList<>();
        enemyList.add(m);

        arrowList = new ArrayList<>();
        bowSound = MainProyek.getAssetManager().get("bowShoot.wav", Sound.class);

        castleTxt = new BitmapFontCache(assetManager.get("smallfont.ttf", BitmapFont.class));
        castleTxt.setColor(Color.GOLD);
        castleTxt.setText("Health : " + Castle.HP, 200, 650);
        scoreTxt = new BitmapFontCache(assetManager.get("smallfont.ttf", BitmapFont.class));
        scoreTxt.setColor(Color.GOLD);
        scoreTxt.setText("Score : ", 1080, 650);
        spawnEnemy = 7;

        multiInput = new InputMultiplexer();
        multiInput.addProcessor(this);

        stageCamera = new OrthographicCamera(MainProyek.virtualWidth, MainProyek.virtualHeight);
        stageCamera.setToOrtho(false, MainProyek.virtualWidth, MainProyek.virtualHeight);
        stage = new Stage(new FitViewport(MainProyek.virtualWidth, MainProyek.virtualHeight, stageCamera));

        multiInput.addProcessor(stage);

        mySkin = assetManager.get("clean-crispy-ui.json", Skin.class);

        optionWindow = new Window("Option", mySkin);
        optionWindow.setHeight(400);
        optionWindow.setWidth(560);
        optionWindow.setPosition(350, 360 - optionWindow.getHeight() / 2);
        optionWindow.setMovable(false);
        optionWindow.setModal(false);
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
        optionMusicSlider.setValue(Menu.musicVolume);
        optionWindow.addActor(optionMusicSlider);
        optionMusicSlider.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (optionMusicSlider.isDragging()) {
                    musicVolume = optionMusicSlider.getValue();
                    music.setVolume(musicVolume);
                }
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
            }
        });

        soundText = new TextArea("Sound", mySkin);
        soundText.setPosition(90, 200);
        soundText.setWidth(60);
        soundText.setDisabled(true);
        optionWindow.addActor(soundText);

        optionSoundSlider = new Slider(0f, 1f, 0.1f, false, mySkin);
        optionSoundSlider.setPosition(160, 205);
        optionSoundSlider.setWidth(optionWindow.getWidth() / 2);
        optionSoundSlider.setValue(Menu.musicVolume);
        optionWindow.addActor(optionSoundSlider);
        optionSoundSlider.addListener(new DragListener() {
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                if (optionSoundSlider.isDragging()) {
                    soundVolume = optionSoundSlider.getValue();
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
                    if (state != State.Playing) {
                        state = State.Playing;
                    }
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
    public void show() {
        Gdx.input.setInputProcessor(multiInput);
        music = assetManager.get("GameMusic.mp3", Music.class);
        music.play();
        music.setLooping(true);
    }

    @Override
    public void render(float v) {
        switch (state) {
            case Paused:
                batch.begin();
                batch.end();
                processInput();
                stage.act();
                stage.draw();
                optionWindow.setVisible(true);
                break;
            case Playing:
                ScreenUtils.clear(0, 0, 0, 1);
                camera.update();
                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                draw();
                batch.end();
                update();
                processInput();
                stage.act();
                stage.draw();
                break;
            case GameOver:
                batch.begin();
                batch.end();
                processInput();
                stage.act();
                stage.draw();
                gameoverWindow.setVisible(true);
                break;
        }
    }

    public void draw() {
        batch.draw(assetManager.get("Background.png", Texture.class), 0, 0, 1280, 720);
        cs.draw(batch);
        for (Arrow a : arrowList) {
            a.draw(batch);
        }
        for (Enemy m : enemyList) {
            m.draw(batch);
        }
        castleTxt.draw(batch);
        scoreTxt.draw(batch);
    }

    public void update() {
        for (Enemy m : enemyList) {
            m.update();
            for (Arrow a : arrowList) {
                if (m.hit(a) && m.getCondition() != GroundEnemy.State.DEAD) {
                    m.attacked();
                    a.delete = true;
                }
            }
        }

        for (Iterator<Enemy> iter = enemyList.iterator(); iter.hasNext(); ) {
            Enemy a = iter.next();
            if (a.getCondition() == GroundEnemy.State.DELETE) {
                iter.remove();
                addScore(10);
            }
        }

        for (Iterator<Arrow> iter = arrowList.iterator(); iter.hasNext(); ) {
            Arrow a = iter.next();
            a.Update();
            if (a.getY() > 720 || a.getY() < 0 || a.getX() > 1280 || a.delete) iter.remove();
        }

        castleTxt.setText("Health : " + Castle.HP, 200, 650);
        score += Gdx.graphics.getDeltaTime();
        scoreTxt.setText("Score : " + (int) score * 10, 980, 650);
        cs.update();
        music.setVolume(musicVolume);
        timeEnemy += Gdx.graphics.getDeltaTime();
        timeSpawArr += Gdx.graphics.getDeltaTime();
        timeSpeed += Gdx.graphics.getDeltaTime();
        if (timeSpawArr > 45) {
            Arrow.setSpawnArrow(Arrow.spawnArrow - 0.1f);
            timeSpawArr = 0;
        }
        if (timeSpeed > 30) {
            Arrow.setSpeedArrow(Arrow.speedArrow + 100);
            timeSpeed = 0;
        }
        if (timeEnemy > spawnEnemy) {
            timeEnemy = 0;
            if (spawnEnemy > 1.2f) spawnEnemy -= 0.2f;
            int x = randomizer.nextInt(3);
            if (x == 2) {
                FlyEnemy e = new FlyEnemy();
                int y = randomizer.nextInt(4);
                e.setX(1280);
                e.setY(250 + y * 100);
                enemyList.add(e);
            } else {
                Enemy e;
                if (x == 1) e = new GroundEnemy();
                else e = new BombEnemy();
                int y = randomizer.nextInt(6);
                e.setX(1280);
                e.setY(100 + y * 50);
                enemyList.add(e);
            }
        }

        if (Castle.HP <= 0) {
            finalScore = (int) score * 10;
            if (state != State.GameOver) {
                state = State.GameOver;
            }
            GameOver();
        }
    }

    public void GameOver() {
        gameoverWindow = new Window("Game Over", mySkin);
        gameoverWindow.setHeight(400);
        gameoverWindow.setWidth(560);
        gameoverWindow.setPosition(350, 360 - gameoverWindow.getHeight() / 2);
        gameoverWindow.setMovable(false);
        gameoverWindow.setModal(false);
        gameoverWindow.setResizable(false);
        gameoverWindow.setVisible(false);
        gameoverWindow.getTitleLabel().setAlignment(Align.center);
        stage.addActor(gameoverWindow);

        scoreLabel = new Label("Score: " + finalScore, mySkin);
        Label.LabelStyle style = new Label.LabelStyle(scoreLabel.getStyle());
        style.font = assetManager.get("bigfontui.ttf", BitmapFont.class);
        scoreLabel.setStyle(style);
        scoreLabel.setWidth(720);
        scoreLabel.setX(-80);
        scoreLabel.setY(320);
        scoreLabel.setAlignment(Align.center);
        scoreLabel.setColor(Color.BLACK);
        gameoverWindow.addActor(scoreLabel);

        playAgainButton = new TextButton("Play Again", mySkin);
        playAgainButton.setWidth(160);
        playAgainButton.setHeight(55);
        playAgainButton.setPosition(gameoverWindow.getWidth() / 2 - playAgainButton.getWidth() / 2, 200);
        gameoverWindow.addActor(playAgainButton);
        playAgainButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x >= 0 && y >= 0 && x <= event.getTarget().getWidth() && y <= event.getTarget().getHeight()) {
                    gameoverWindow.setVisible(false);
                    parentGame.setScreen(new GameScreen());
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        exitButton = new TextButton("Exit", mySkin);
        exitButton.setWidth(160);
        exitButton.setHeight(55);
        exitButton.setPosition(gameoverWindow.getWidth() / 2 - exitButton.getWidth() / 2, 130);
        gameoverWindow.addActor(exitButton);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (x >= 0 && y >= 0 && x <= event.getTarget().getWidth() && y <= event.getTarget().getHeight()) {
                    gameoverWindow.setVisible(false);
                    parentGame.setScreen(new Menu<>());
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    public void addScore(int add) {
        score += add;
        castleTxt.setText("Score: " + score, 20, 20);
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
        batch.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        return true;
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

    public void processInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (this.state != State.Paused) {
                this.state = State.Paused;
            } else {
                this.state = State.Playing;
            }
        }

        float tempX = 0, tempY = 0;
        if (Gdx.input.isTouched() && this.state == State.Playing) {

            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            if (stateTime > Arrow.spawnArrow) {
                Arrow arrow = new Arrow();
                arrow.setX(300);
                arrow.setY(360);
                arrow.setXSpeed(tempX);
                arrow.setYSpeed(tempY);
                arrowList.add(arrow);
                stateTime = 0;
                bowSound.play(soundVolume);
            }
            cs.setBowGo();

            float delta = Gdx.graphics.getDeltaTime();
            stateTime += delta;

            float speedX = touchPos.x - 300;
            float speedY = touchPos.y - 360;

            float d = (float) Math.sqrt(speedX * speedX + speedY * speedY);

            tempX = speedX / d * Arrow.speedArrow;
            if (tempX <= 0) tempX = 0;
            tempY = speedY / d * Arrow.speedArrow;
            cs.setDX(tempX);
            cs.setDY(tempY);

            for (Arrow sprite : arrowList) {
                if (sprite.changeDir) {
                    sprite.setXSpeed(tempX);
                    sprite.setYSpeed(tempY);
                    sprite.changeDir = false;
                }
            }
        } else {
            stateTime = 0;
            cs.setBowIdle();
        }
    }
}
