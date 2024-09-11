package com.dungeon.game.components;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputComponent implements InputProcessor {

    //directional flags
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode){
            case Keys.W:
                upPressed = true;
                break;
            case Keys.S:
                downPressed = true;
                break;
            case Keys.A:
                leftPressed = true;
                break;
            case Keys.D:
                rightPressed = true;
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode){
            case Keys.W:
                upPressed = false;
                break;
            case Keys.S:
                downPressed = false;
                break;
            case Keys.A:
                leftPressed = false;
                break;
            case Keys.D:
                rightPressed = false;
                break;
        }
        return true;
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
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
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

    public boolean isMoving(){
        return upPressed || downPressed || leftPressed || rightPressed;
    }

    public Vector2 getVelocity(float speed, float delta){
        //TODO: Does this actually need to be here?  It's not part of input
        float xVelocity;

        if(leftPressed || rightPressed){
            if(leftPressed && rightPressed){
                xVelocity = 0;
            } else if(leftPressed){
                xVelocity = -speed * delta;
            } else {
                xVelocity = speed * delta;
            }
        } else {
            xVelocity = 0;
        }

        float yVelocity;
        if(upPressed || downPressed){
            if(upPressed && downPressed){
                yVelocity = 0;
            } else if(upPressed){
                yVelocity = speed * delta;
            } else {
                yVelocity = -speed * delta;
            }
        } else {
            yVelocity = 0;
        }

        return new Vector2(xVelocity, yVelocity);
    }
}
