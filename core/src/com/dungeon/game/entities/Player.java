package com.dungeon.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dungeon.game.components.AnimationComponent;
import com.dungeon.game.components.AssetComponent;
import com.dungeon.game.enums.ActionEnum;
import com.dungeon.game.screens.GameScreen;

import java.util.*;

public class Player extends Entity{

    private static final String PLAYER_ATLAS = "atlas/player.atlas";
    private TextureAtlas atlas;
    private float animSpeed = .5f;
    private long xVelocityCount = 0,
                yVelocityCount = 0;
    private boolean isActing = false;
    private float actingCounter = 0.0f;
    private final float MAX_ACTING = animSpeed / 2.0f;

    public ActionEnum currentState, previousState;
    public TextureRegion currentTexture;
    public AnimationComponent animationComponent;
    public Map<ActionEnum, ActionEnum> actingMap = new HashMap<>();

    GameScreen gs;

    public Player(GameScreen gs, float x, float y) {
        super(x, y);
        this.gs = gs;
        initPlayer();
    }

    public void initPlayer(){

        //initializing variables
        currentState = previousState = ActionEnum.WALK_DOWN;
        speed = 4f * GameScreen.TILE_SIZE; //approximately 4 tiles per second.
        solidArea = new Rectangle(4, 0, 24, 24);
        animationComponent = new AnimationComponent(gs);
        atlas = AssetComponent.loadAtlas(PLAYER_ATLAS);
        //atlas = AssetComponent.getManager().get(PLAYER_ATLAS, TextureAtlas.class);

        //set animations map, key - action being taken, value - animations for that action
        Map<ActionEnum, List<String>> animationsMap = new HashMap<>();
        animationsMap.put(ActionEnum.WALK_UP, Arrays.asList("playerUp1", "playerUp2"));
        animationsMap.put(ActionEnum.WALK_DOWN, Arrays.asList("playerDown1", "playerDown2"));
        animationsMap.put(ActionEnum.WALK_LEFT, Arrays.asList("playerLeft1", "playerLeft2"));
        animationsMap.put(ActionEnum.WALK_RIGHT, Arrays.asList("playerRight1", "playerRight2"));
        animationsMap.put(ActionEnum.ATTACK_UP, Collections.singletonList("playerAttackUp"));
        animationsMap.put(ActionEnum.ATTACK_DOWN, Collections.singletonList("playerAttackDown"));
        animationsMap.put(ActionEnum.ATTACK_LEFT, Collections.singletonList("playerAttackLeft"));
        animationsMap.put(ActionEnum.ATTACK_RIGHT, Collections.singletonList("playerAttackRight"));

        //create Animations and send to the AnimationComponent based on the animationsMap
        Array<TextureRegion> regions = new Array<>();
        for(ActionEnum action : animationsMap.keySet()){
            for(String string : animationsMap.get(action)){
                regions.add(atlas.findRegion(string));
            }
            animationComponent.addAnimation(action, regions, animSpeed);
            regions.clear();
        }

        //actingMap ties actions together so that we know what action to use and
        // when to return from that action to the previous action state
        actingMap.put(ActionEnum.WALK_UP, ActionEnum.ATTACK_UP);
        actingMap.put(ActionEnum.WALK_DOWN, ActionEnum.ATTACK_DOWN);
        actingMap.put(ActionEnum.WALK_LEFT, ActionEnum.ATTACK_LEFT);
        actingMap.put(ActionEnum.WALK_RIGHT, ActionEnum.ATTACK_RIGHT);

    }

    public void update(float delta){
        //get velocity and current position
        velocity = gs.inputComponent.getVelocityWithCondition(speed, delta, !isActing);
        //get state prior to collision detection because this is the player's intended state
        currentState = getCurrentState(velocity);
        //checks against tile collision and resets velocity if necessary
        gs.collisionComponent.checkTile(this);

        updateIsActing(delta);

        //update x and y positions based on velocity after collision checking
        x = x + velocity.x;
        y = y + velocity.y;

        currentTexture = animationComponent.getCurrentTexture(currentState, delta, gs.inputComponent.isMoving());
    }

    public void updateIsActing(float delta){

        if(gs.inputComponent.enterPressed && !isActing){ //user presses enter and isn't already acting
            isActing = true;
            gs.inputComponent.enterPressed = false;
            previousState = currentState;
            currentState = actingMap.get(currentState);
        } else if (gs.inputComponent.enterPressed){
            //don't want the user pressing enter again while acting; not sure if this is needed.
            gs.inputComponent.enterPressed = false;
        }

        if(isActing){
            velocity.x = 0;
            velocity.y = 0;
            actingCounter += delta;
            if(actingCounter >= MAX_ACTING){
                isActing = false;
                actingCounter = 0f;
                currentState = previousState;
            }
        }

    }

    public void render(SpriteBatch batch){
        batch.draw(currentTexture, x, y);
    }

    public void dispose(){
        atlas.dispose();
    }

    public ActionEnum getCurrentState(Vector2 v){
        //CHECK VERTICAL MOVEMENT
        if(gs.inputComponent.upPressed || gs.inputComponent.downPressed) {
            yVelocityCount++;
        } else {
            yVelocityCount = 0;
        }

        //CHECK HORIZONTAL MOVEMENT
        if(gs.inputComponent.leftPressed || gs.inputComponent.rightPressed) {
            xVelocityCount++;
        } else {
            xVelocityCount = 0;
        }

        //CALCULATE DIRECTION STATE BASED ON VELOCITY
        if (xVelocityCount > yVelocityCount){
            if(v.x == 0){
                return currentState;
            }else if(v.x > 0){
                return ActionEnum.WALK_RIGHT;
            }
            return ActionEnum.WALK_LEFT;
        } else {
            if(v.y == 0){
                return currentState;
            }else if(v.y > 0){
                return ActionEnum.WALK_UP;
            }
            return ActionEnum.WALK_DOWN;
        }
    }

}
