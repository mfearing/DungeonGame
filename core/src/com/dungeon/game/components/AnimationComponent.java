package com.dungeon.game.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.dungeon.game.enums.ActionEnum;
import com.dungeon.game.screens.GameScreen;

import java.util.Arrays;
import java.util.HashMap;

public class AnimationComponent {

    GameScreen gs;
    HashMap<ActionEnum, Animation<TextureRegion>> animations;
    float stateTime;

    public AnimationComponent(GameScreen gs){
        this.gs = gs;
        animations = new HashMap<>();
        stateTime = 0f;
    }

    public void addAnimation(ActionEnum state, Array<TextureRegion> frames, float frameDuration){
        Animation<TextureRegion> anim = new Animation<>(frameDuration, frames);
        animations.put(state, anim);
    }

    public TextureRegion getCurrentTexture(ActionEnum state, float delta, boolean isMoving){
        if(isMoving){
            stateTime += delta;
        }

        Animation<TextureRegion> frames = animations.get(state);
        TextureRegion current = frames.getKeyFrame(stateTime);
        int numAnimations = ((Object[]) frames.getKeyFrames()).length;

        if(stateTime > frames.getFrameDuration() * numAnimations){ //length is number of animations, each with frame duration
            stateTime = 0;
        }

        return current;
    }

}
