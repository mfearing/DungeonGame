package com.dungeon.game.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMapComponent {

    private OrthogonalTiledMapRenderer renderer;
    public TiledMap tiledMap;
    private String mapPath;

    public TileMapComponent(String mapPath){
        this.mapPath = mapPath;
        AssetComponent.loadTileMap(mapPath);
        this.tiledMap = AssetComponent.getManager().get(this.mapPath);
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
