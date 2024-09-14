package com.dungeon.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dungeon.game.screens.GameScreen;

public class TransitionComponent {

    GameScreen gs;

    float alpha = 1f; // Alpha value for fade
    private boolean fadingToBlack = true;
    private boolean fadingFromBlack = false;
    private boolean transitionDone = false;
    public boolean doTransition = false;
    private float fadeTime = 0f; //counter for fading in/out
    public TileMapComponent.PathObject pathObject;

    public TransitionComponent(GameScreen gs){
        this.gs = gs;
    }

    public void resetTransition(){
        this.fadingToBlack = true;
        this.fadingFromBlack = false;
        this.transitionDone = false;
        this.doTransition = false;
        this.pathObject = null;
        this.fadeTime = 0f;
        this.alpha = 1f;
    }

    public void update(float delta) {
        if (!transitionDone) {
            fadeTime += delta;

            //Duration of fade in/out
            float fadeDuration = 0.25f;
            if (fadingToBlack) {
                //Fade-out alpha goes from 1 to 0
                alpha = fadeTime / fadeDuration;

                //when fade-out is done
                if (fadeTime >= fadeDuration) {
                    fadingToBlack = false;
                    fadingFromBlack = true;
                    fadeTime = 0f;
                    gs.tileMapComponent.loadTileMap("room_" + pathObject.roomId + ".tmx");
                    repositionPlayer();
                }
            } else if(fadingFromBlack) {
                //fade in alpha goes from 0 to 1
                alpha = 1f - fadeTime / fadeDuration;

                //When fade-in is done
                if(fadeTime >= fadeDuration){
                    transitionDone = true;
                }
            }
        }

        //Here we draw the fade-out effect
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        gs.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        gs.shapeRenderer.setColor(0, 0, 0, alpha);
        gs.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gs.shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void repositionPlayer(){
        switch(gs.player.currentState){
            case ATTACK_UP:
                gs.player.y = GameScreen.TILE_SIZE * 2;
                break;
            case ATTACK_DOWN:
                gs.player.y = GameScreen.WORLD_SIZE - GameScreen.TILE_SIZE;
                break;
            case ATTACK_LEFT:
                gs.player.x = GameScreen.WORLD_SIZE - GameScreen.TILE_SIZE * 2;
                break;
            case ATTACK_RIGHT:
                gs.player.x = GameScreen.TILE_SIZE;
                break;
        }
    }

    public boolean isTransitionDone(){
        return transitionDone;
    }

}
