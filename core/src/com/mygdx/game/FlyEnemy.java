package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;

public class FlyEnemy extends GroundEnemy {
    ArrayList<Spell> spell;

    @Override
    public void Initialize() {
        Texture tmp = MainProyek.getAssetManager().get("WalkingWraith.png", Texture.class);
        TextureRegion[] frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        moveAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("AttackingWraith.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        attackAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("HurtWraith.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        hurtAnim = new Animation<TextureRegion>(0.05f, frames);

        tmp = MainProyek.getAssetManager().get("DyingWraith.png", Texture.class);
        frames = MainProyek.CreateAnimationFrames(tmp, 64, 56, 12, true, false);
        deadAnim = new Animation<TextureRegion>(0.05f, frames);


        spell = new ArrayList<>();
        sound = MainProyek.getAssetManager().get("enemyHit.wav", Sound.class);

        stateTime = 0;

        damage = 1;
        HP = 3;

    }

    @Override
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

        for (Spell sp : spell) {
            sp.draw(batch);
        }

    }

    @Override
    public void update() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;
        if (getX() > 600) {
            this.setX(this.getX() - speed * delta);
        } else {
            this.setX(600);
            if (HP > 0) condition = State.ATTACK;

        }
        if (condition == State.MOVE) {
            tmpCon = State.MOVE;
            speed = 80;
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
        for (Spell s : spell) {
            s.Update();
        }

        for (Iterator<Spell> iter = spell.iterator(); iter.hasNext(); ) {
            Spell s = iter.next();
            s.Update();
            if (s.getX() < 400 || s.delete) iter.remove();
        }
    }

    @Override
    public void attack() {
        Castle.HP -= this.damage;
        stateTime = 0;
        Vector2 direc = new Vector2(300, 200);

        float speedX = direc.x - this.getX();
        float speedY = direc.y - this.getY();

        float d = (float) Math.sqrt(speedX * speedX + speedY * speedY);

        float tempX = speedX / d * 400;
        float tempY = speedY / d * 400;


        Spell s = new Spell();
        s.setX(580);
        s.setY(this.getY());
        s.setXSpeed(tempX);
        s.setYSpeed(tempY);
        spell.add(s);
    }
}
