package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GroundEnemy implements Enemy {
    public enum State {
        MOVE, ATTACK, HURT, DEAD, DELETE
    }

    public float X, Y, speed = 100, stateTime, HP;
    public State condition = State.MOVE, tmpCon;
    public Animation<TextureRegion> moveAnim, attackAnim, hurtAnim, deadAnim;
    public float damage;
    public Sound sound;

    GroundEnemy() {
        Initialize();
    }

    public void Initialize() {
        Texture tmp = MainProyek.getAssetManager().get("WalkingMino.png", Texture.class);
        TextureRegion[] frames = MainProyek.CreateAnimationFrames(tmp, 96, 66, 18, true, false);
        moveAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("AttackingMino.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 72, 56, 12, true, false);
        attackAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("HurtMino.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        hurtAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("DyingMino.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        deadAnim = new Animation<TextureRegion>(0.05f, frames);

        sound = MainProyek.getAssetManager().get("enemyHit.wav", Sound.class);

        stateTime = 0;

        damage = 1;
        HP = 3;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = new TextureRegion();
        if (condition == State.MOVE)
            currentFrame = moveAnim.getKeyFrame(stateTime, true);
        else if (condition == State.ATTACK)
            currentFrame = attackAnim.getKeyFrame(stateTime, true);
        else if (condition == State.HURT)
            currentFrame = hurtAnim.getKeyFrame(stateTime, true);
        else if (condition == State.DEAD)
            currentFrame = deadAnim.getKeyFrame(stateTime, true);

        batch.draw(currentFrame, getX() - 32, getY() - 28);
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;
        if (getX() > 400)
            this.setX(this.getX() - speed * delta);
        else {
            this.setX(400);
            if (HP > 0) condition = State.ATTACK;
        }
        if (condition == State.MOVE) {
            tmpCon = State.MOVE;
            speed = 100;
        }
        if (condition == State.ATTACK) {
            tmpCon = State.ATTACK;
            if (stateTime > 0.6f) {
                attack();
            }
        }
        if (condition == State.HURT && HP > 0) {
            speed = 30;
            if (stateTime > 0.6f) condition = tmpCon;
        }
        if (HP <= 0) {
            condition = State.DEAD;
            speed = 0;
            if (stateTime > 0.6f) condition = State.DELETE;
        }
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public void attack() {
        Castle.HP -= this.damage;
        stateTime = 0;
    }

    public void attacked() {
        if (HP > 0) {
            stateTime = 0;
            condition = State.HURT;
            HP -= 1;
            sound.play(GameScreen.soundVolume);
        }

    }

    public GroundEnemy.State getCondition() {
        return condition;
    }

    public boolean hit(Arrow a) {
        float dx = getX() - a.getX();
        float dy = getY() - a.getY();
        return (dx * dx) < 1024 && (dy * dy) < 800;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
