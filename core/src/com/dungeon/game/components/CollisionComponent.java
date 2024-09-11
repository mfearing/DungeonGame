package com.dungeon.game.components;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.dungeon.game.entities.Entity;
import com.dungeon.game.screens.GameScreen;

public class CollisionComponent {

    GameScreen gs;

    public CollisionComponent(GameScreen gs){
        this.gs = gs;
    }

    public void checkTile(Entity entity){

        //these need to be replaced with a collision rectangle for the player
        Rectangle screenSolidArea = entity.getScreenSolidArea();
        float entityLeftX = screenSolidArea.x;
        float entityRightX = screenSolidArea.x + screenSolidArea.width;
        float entityTopY = screenSolidArea.y + screenSolidArea.height;
        float entityBottomY = screenSolidArea.y;

        if( //trying to keep collision from checking outside the bounds of the screen to prevent errors when getting tiles
            entityLeftX < 0 ||
            entityBottomY < 0 ||
            ((entityRightX + entity.velocity.x)) >= GameScreen.WORLD_SIZE ||
            ((entityTopY + entity.velocity.y)) > GameScreen.WORLD_SIZE
        ){
            return;
        }

        int entityLeftCol = (int)entityLeftX / GameScreen.TILE_SIZE; //left x coord in terms of tile array
        int entityRightCol = (int)entityRightX / GameScreen.TILE_SIZE; //right x coord in terms of tile array
        int entityTopRow; //find top y coord later
        int entityBottomRow; //find bottom y coord later

        TiledMapTileLayer layer = (TiledMapTileLayer)gs.tileMapComponent.tiledMap.getLayers().get("layer_1");

        if(entity.velocity.y > 0){ // up
            entityTopRow = (int)((entityTopY + entity.velocity.y) / GameScreen.TILE_SIZE);
            boolean topLeftCollides = "true".equals(layer.getCell(entityLeftCol, entityTopRow).getTile().getProperties().get("collidable"));
            boolean topRightCollides = "true".equals(layer.getCell(entityRightCol, entityTopRow).getTile().getProperties().get("collidable"));
            if(topLeftCollides || topRightCollides){
                entity.velocity.y = 0;
                entity.y = entityTopRow * GameScreen.TILE_SIZE - screenSolidArea.height - 1;
            }
        } else if(entity.velocity.y < 0){ // down
            entityBottomRow = (int)((entityBottomY + entity.velocity.y) / GameScreen.TILE_SIZE);
            boolean bottomLeftCollides = "true".equals(layer.getCell(entityLeftCol, entityBottomRow).getTile().getProperties().get("collidable"));
            boolean bottomRightCollides = "true".equals(layer.getCell(entityRightCol, entityBottomRow).getTile().getProperties().get("collidable"));
            if(bottomLeftCollides || bottomRightCollides){
                entity.velocity.y = 0;
                entity.y = entityBottomRow * GameScreen.TILE_SIZE + GameScreen.TILE_SIZE;
            }
        }

        //reset for x, which doesn't add y velocity
        entityTopRow = (int)(entityTopY / GameScreen.TILE_SIZE);
        entityBottomRow = (int)(entityBottomY / GameScreen.TILE_SIZE);

        if(entity.velocity.x > 0){ // right
            entityRightCol = (int)((entityRightX + entity.velocity.x) / GameScreen.TILE_SIZE);
            boolean topRightCollides = "true".equals(layer.getCell(entityRightCol, entityTopRow).getTile().getProperties().get("collidable"));
            boolean bottomRightCollides = "true".equals(layer.getCell(entityRightCol, entityBottomRow).getTile().getProperties().get("collidable"));
            if(topRightCollides || bottomRightCollides){
                entity.velocity.x = 0;
                entity.x = entityRightCol * GameScreen.TILE_SIZE - entity.solidArea.x - screenSolidArea.width - 1; //closes gap to wall
            }

        } else if(entity.velocity.x < 0){ // left
            entityLeftCol = (int)((entityLeftX + entity.velocity.x) / GameScreen.TILE_SIZE);
            boolean topLefttCollides = "true".equals(layer.getCell(entityLeftCol, entityTopRow).getTile().getProperties().get("collidable"));
            boolean bottomLeftCollides = "true".equals(layer.getCell(entityLeftCol, entityBottomRow).getTile().getProperties().get("collidable"));
            if(topLefttCollides || bottomLeftCollides){
                entity.velocity.x = 0;
                entity.x = entityLeftCol * GameScreen.TILE_SIZE + entity.solidArea.x + screenSolidArea.width;
            }
        }


    }


}
