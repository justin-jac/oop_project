package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Castle extends Sprite {
    public static int HP;

    enum State {
        FULL,
        DYING,
        BROKE
    }

    float DX, DY, angle, stateTime;
    Animation<TextureRegion> bowGo, bowIdle;
    boolean go;
    Texture full, dying, broke;

    State state;

    public Castle() {
        super(MainProyek.getAssetManager().get("Castle.png", Texture.class));
        HP = 200;
        this.flip(true, false);
        this.setOriginCenter();
        Initialize();
        state = State.FULL;
    }

    public Castle(Texture texture) {
        super(texture);
        this.flip(true, true);
        this.setOriginCenter();
        state = State.FULL;
    }

    public void Initialize() {
        MainProyek parentGame = (MainProyek) Gdx.app.getApplicationListener();
        AssetManager assetManager = parentGame.getAssetManager();

        full = assetManager.get("Castle.png", Texture.class);
        dying = assetManager.get("Castle2.png", Texture.class);
        broke = assetManager.get("Castle3.png", Texture.class);

        Texture bowPic = assetManager.get("Bow.png", Texture.class);
        TextureRegion[] frames = MainProyek.CreateAnimationFrames(bowPic, 70, 90, 24, false, false);
        bowGo = new Animation<TextureRegion>(Arrow.spawnArrow / 24, frames);

        bowPic = assetManager.get("Bow.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(bowPic, 70, 90, 1, false, false);
        bowIdle = new Animation<TextureRegion>(Arrow.spawnArrow / 24, frames);
        go = false;
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        checkHp(HP);

        Texture bowPic = MainProyek.assetManager.get("Bow.png", Texture.class);
        TextureRegion[] frames = MainProyek.CreateAnimationFrames(bowPic, 70, 90, 24, false, false);
        bowGo = new Animation<TextureRegion>(Arrow.spawnArrow / 24, frames);

        float angleRadian = MathUtils.atan2(DY, DX);
        double pi = Math.PI;
        angle = angleRadian * 180 / (float) pi;
    }

    public void draw(SpriteBatch batch) {
        if (state == State.BROKE) this.setTexture(broke);
        else if (state == State.DYING) this.setTexture(dying);
        else if (state == State.FULL) this.setTexture(full);

        TextureRegion bowDraw;
        if (go) bowDraw = bowGo.getKeyFrame(stateTime, true);
        else bowDraw = bowIdle.getKeyFrame(stateTime, true);

        if (state == State.BROKE) batch.draw(this, -400, -65, 850, 550);
        else batch.draw(this, -390, -65);
        batch.draw(bowDraw, 300 - 30, 360 - 35, 0, 35, 90, 70, 1, 1, angle);
    }

    public void checkHp(int hp) {
        if (hp <= 10) {
            HP = 0;
            state = State.BROKE;
        } else if (hp <= 75) {
            state = State.DYING;
        } else {
            state = State.FULL;
        }
    }

    public float getDX() {
        return DX;
    }

    public void setDX(float DX) {
        this.DX = DX;
    }

    public float getDY() {
        return DY;
    }

    public void setDY(float DY) {
        this.DY = DY;
    }

    public void setBowGo() {
        this.go = true;
    }

    public void setBowIdle() {
        this.go = false;
    }
}
