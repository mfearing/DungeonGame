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

public class Player extends Entity{

    private static final String PLAYER_ATLAS = "atlas/player.atlas";
    private TextureAtlas atlas;
    private float animSpeed = .5f;
    private long xVelocityCount = 0,
                yVelocityCount = 0;
    public ActionEnum currentState;
    public TextureRegion currentTexture;

    public AnimationComponent animationComponent;

    GameScreen gs;

    public Player(GameScreen gs, float x, float y) {
        super(x, y);
        this.gs = gs;
        initPlayer();
    }

    public void initPlayer(){
        //speed
        speed = 4f * GameScreen.TILE_SIZE;

        //solidArea hit box
        solidArea = new Rectangle(4, 0, 24, 24);

        //player animations
        animationComponent = new AnimationComponent(gs);
        AssetComponent.loadAtlas(PLAYER_ATLAS);
        atlas = AssetComponent.getManager().get(PLAYER_ATLAS, TextureAtlas.class);

        Array<TextureRegion> regions = new Array<>();
        regions.add(atlas.findRegion("playerDown1"));
        regions.add(atlas.findRegion("playerDown2"));
        animationComponent.addAnimation(ActionEnum.WALK_DOWN, regions, animSpeed); //duration is in what units?!?

        regions.clear();
        regions.add(atlas.findRegion("playerUp1"));
        regions.add(atlas.findRegion("playerUp2"));
        animationComponent.addAnimation(ActionEnum.WALK_UP, regions, animSpeed); //duration is in what units?!?

        regions.clear();
        regions.add(atlas.findRegion("playerLeft1"));
        regions.add(atlas.findRegion("playerLeft2"));
        animationComponent.addAnimation(ActionEnum.WALK_LEFT, regions, animSpeed); //duration is in what units?!?

        regions.clear();
        regions.add(atlas.findRegion("playerRight1"));
        regions.add(atlas.findRegion("playerRight2"));
        animationComponent.addAnimation(ActionEnum.WALK_RIGHT, regions, animSpeed); //duration is in what units?!?

        currentState = ActionEnum.WALK_DOWN;
        currentTexture = atlas.findRegion("playerDown1");
    }

    public void update(float delta){
        //get velocity and current position
        velocity = gs.inputComponent.getVelocity(speed, delta);

        //checks against tile collision and resets velocity if necessary
        gs.collisionComponent.checkTile(this);

        //update x and y positions based on velocity after collision checking
        x = x + velocity.x;
        y = y + velocity.y;


        currentState = getCurrentState(velocity);
        currentTexture = animationComponent.getCurrentTexture(currentState, delta, gs.inputComponent.isMoving());
    }

    public void render(SpriteBatch batch){
        batch.draw(currentTexture, x, y);
    }

    public void dispose(){
        atlas.dispose();
    }

    public ActionEnum getCurrentState(Vector2 v){
        //VERTICAL MOVEMENT
        if(gs.inputComponent.upPressed || gs.inputComponent.downPressed) {
            yVelocityCount++;
        } else {
            yVelocityCount = 0;
        }

        //HORIZONTAL MOVEMENT
        if(gs.inputComponent.leftPressed || gs.inputComponent.rightPressed) {
            xVelocityCount++;
        } else {
            xVelocityCount = 0;
        }

        //Calculate direction state via velocity
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
