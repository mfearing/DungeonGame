package com.dungeon.game.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapComponent {

    private final OrthogonalTiledMapRenderer renderer;
    public TiledMap tiledMap;
    private String mapPath;

    public TileMapComponent(String mapPath){
        this.mapPath = mapPath;
        this.tiledMap = AssetComponent.loadTileMap(mapPath);
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose(){
        renderer.dispose();
        tiledMap.dispose();
    }

}
