package com.dungeon.game.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.HashMap;
import java.util.Map;

public class TileMapComponent {

    public static final String MAP_PATH = "maps/";
    private OrthogonalTiledMapRenderer renderer;
    public TiledMap tiledMap;
    public Map<PathEnum, PathObject> paths;

    public TileMapComponent(String mapPath){
        loadTileMap(mapPath);
    }

    public void loadTileMap(String mapPath){
        this.tiledMap = AssetComponent.loadTileMap(MAP_PATH + mapPath);
        this.paths = new HashMap<>();

        MapLayer pathLayer = getLayer("path_layer");

        pathLayer.getObjects().forEach(mapObject -> {
            PathObject pa = new PathObject(
                    (int) mapObject.getProperties().get(mapObject.getName()),
                    (int) mapObject.getProperties().get("row"),
                    (int) mapObject.getProperties().get("col")
            );
            paths.put(PathEnum.valueOf(mapObject.getName()), pa);
        });
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public MapLayer getLayer(String layer){
        return tiledMap.getLayers().get(layer);
    }

    public void render(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public void dispose(){
        renderer.dispose();
        tiledMap.dispose();
    }

    public static class PathObject {
        public int roomId;
        public int row;
        public int col;

        public PathObject(int roomId, int row, int col){
            this.roomId = roomId;
            this.row = row;
            this.col = col;
        }
    }

    public enum PathEnum {
        north,
        south,
        east,
        west
    }

}
