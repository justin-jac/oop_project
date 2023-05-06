package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BombEnemy extends GroundEnemy {
    int tmp = 0;

    @Override
    public void Initialize() {
        Texture tmp = MainProyek.getAssetManager().get("RunningGolem.png", Texture.class);
        TextureRegion[] frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        moveAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("ExplodeGolem.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 70, 70, 19, true, false);
        attackAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("HurtGolem.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        hurtAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("DyingGolem.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        deadAnim = new Animation<TextureRegion>(0.05f, frames);


        sound = MainProyek.getAssetManager().get("enemyHit.wav", Sound.class);

        stateTime = 0;
        setSpeed(200);

        damage = 10;
        HP = 3;
        condition = State.MOVE;
    }

    @Override
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
            speed = 150;
        }
        if (condition == State.ATTACK) {
            tmpCon = State.ATTACK;
            if (stateTime > 0.9f) {
                attack();
                tmp++;
            }
        }
        if (tmp > 1) condition = State.DELETE;
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

    @Override
    public void attack() {
        if(tmp == 1)Castle.HP -= this.damage;
        stateTime = 0;
    }

    @Override
    public void attacked() {
        if (HP > 0) {
            stateTime = 0;
            condition = State.HURT;
            HP -= 1;
            sound.play(GameScreen.soundVolume);
        }

    }
}
