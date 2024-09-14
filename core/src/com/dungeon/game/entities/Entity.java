package com.dungeon.game.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.dungeon.game.screens.GameScreen;

public class Entity {
    public float x; //x position within the world
    public float y; //y position within the world
    public Vector2 velocity;
    public float speed; //World Size and Tile Size are 1 to 1, so 2.5 tiles per second is the speed.
    public Rectangle solidArea;

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
        this.solidArea = new Rectangle(0, 0, GameScreen.TILE_SIZE, GameScreen.TILE_SIZE); //standard tile size
        this.speed = 0;
        velocity = new Vector2(0, 0);
    }

    //returns a rectangle that is relative to the entity's position on the screen
    public Rectangle getScreenSolidArea(){
        return new Rectangle(x + solidArea.x, y+ solidArea.y, solidArea.width, solidArea.height);
    }

    public Vector2 getScreenSolidAreaCenterVector(){
        Rectangle rec = getScreenSolidArea();
        return rec.getCenter(new Vector2());
    }

}
