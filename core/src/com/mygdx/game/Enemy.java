package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Enemy {
    public void Initialize();

    public void draw(SpriteBatch batch);

    public void update();

    public float getX();

    public float getY();

    public void setX(float x);

    public void setY(float y);

    public void attack();

    public void attacked();

    public boolean hit(Arrow a);

    public GroundEnemy.State getCondition();
}
