package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
    public Vector2 velocity = new Vector2();
    public float speed = 10 * 10;
    private TiledMapTileLayer collisionLayer;
    private String blockedKey = "blocked";
    private String deadKey = "deadChance";
    public int deadChance = 0;
    public boolean isDead = false;
    boolean collideX, collideY,
            wasCollideX, wasCollideY,
            wasCollideLeft, wasCollideRight,
            wasCollideTop, wasCollideBottom,
            xBlocked, yBlocked = false;


    public Player(Sprite sprite,  TiledMapTileLayer collisionLayer){
        super(sprite);
        this.collisionLayer = collisionLayer;
    }


@Override
    public void draw(Batch spriteBatch){
        super.draw(spriteBatch);
        update(Gdx.graphics.getDeltaTime());

    }
    private void update(float delta) {

        float oldX = getX(), oldY = getY(), tileWidth = collisionLayer.getTileWidth(), tileHeight = collisionLayer.getTileHeight();

        setX(getX() + velocity.x * delta);

        if (velocity.x < 0) {
            collideX = collidesLeft();
            wasCollideLeft = collideX;
        } else if (velocity.x > 0) {
            collideX = collidesRight();
            wasCollideRight = collideX;
        }
        if (collideX) {
            setX(oldX);
            velocity.x = 0;
            wasCollideX = true;
        }else {
            wasCollideX = false;
        }

        setY(getY() + velocity.y * delta);

        if (velocity.y < 0) {
            collideY = collidesBottom();
            wasCollideBottom = collideY;
        } else if (velocity.y > 0) {
            collideY = collidesTop();
            wasCollideTop = collideY;
        }

        if (collideY) {
            setY(oldY);
            velocity.y = 0;
            wasCollideY = true;

        } else {
            wasCollideY = false;
        }


        if (velocity.x < 0) {
            if(deadLeft()){
                deadChance++;

            }


        } else if (velocity.x > 0) {
            if(deadRight()){
                deadChance++;
            }

        }
        if (velocity.y < 0) {
            if(deadBottom()){
                deadChance++;

            }

        } else if (velocity.y > 0) {
            if(deadTop()){
                deadChance++;
            }
        }
        if(deadChance >= 3000){
            isDead = true;
        }
    }

    public void setCollisionLayer(TiledMapTileLayer collisionLayer){
        this.collisionLayer = collisionLayer;
    }
    public TiledMapTileLayer getCollisionLayer(){
        return collisionLayer;
    }

    public float getSpeed(){
        return speed;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public void setVelocity(Vector2 velocity){
        this.velocity = velocity;
    }
    public Vector2 getVelocity(){
        return velocity;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                velocity.y += speed;

                break;
            case Input.Keys.S:
                velocity.y -= speed;

                break;
            case Input.Keys.A:
                velocity.x -= speed;

                break;
            case Input.Keys.D:
                velocity.x += speed;

                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                if (!wasCollideTop){
                    velocity.y -= speed;
                }
                wasCollideTop = false;
                if (!yBlocked){
                    velocity.x -= speed;
                }
                if (!yBlocked){
                    velocity.x += speed;
                }
                break;
            case Input.Keys.S:
                if(!wasCollideBottom) {
                    velocity.y += speed;
                }
                wasCollideBottom = false;
                if (!yBlocked){
                    velocity.x -= speed;
                }
                if (!yBlocked){
                    velocity.x += speed;
                }
                break;
            case Input.Keys.A:
                if(!wasCollideLeft) {
                    velocity.x += speed;
                }
                wasCollideLeft = false;
                if (!xBlocked){
                    velocity.y -= speed;
                }
                if (!xBlocked){
                    velocity.y += speed;
                }
                break;
            case Input.Keys.D:
                if(!wasCollideRight) {
                    velocity.x -= speed;
                }
                wasCollideRight = false;
                if (!xBlocked){
                    velocity.y -= speed;
                }
                if (!xBlocked){
                    velocity.y += speed;
                }
                break;
        }
        return true;
    }

    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
    }

    public boolean collidesRight() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellBlocked(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        return false;
    }

    public boolean collidesBottom() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellBlocked(getX() + step, getY()))
                return true;
        return false;
    }

    private boolean isCellDead(float x, float y){
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(deadKey);
    }
    public boolean deadRight() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellDead(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean deadLeft() {
        for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
            if(isCellDead(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean deadTop() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellDead(getX() + step, getY() + getHeight()))
                return true;
        return false;
    }

    public boolean deadBottom() {
        for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
            if(isCellDead(getX() + step, getY()))
                return true;
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
}

