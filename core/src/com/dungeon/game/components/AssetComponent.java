package com.dungeon.game.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class AssetComponent {

    private AssetComponent(){}

    private static AssetManager manager = new AssetManager();

    public static AssetManager getManager(){
        if(manager == null){
            manager = new AssetManager();
        }
        return manager;
    }

    public static void loadTileMap(String path){
        getManager().setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        getManager().load(path, TiledMap.class);
        getManager().finishLoading();
    }

    public static void loadAtlas(String path){
        getManager().setLoader(TextureAtlas.class, new TextureAtlasLoader(new InternalFileHandleResolver()));
        getManager().load(path, TextureAtlas.class);
        getManager().finishLoading();
    }

    public static void loadTexture(String path){
        getManager().setLoader(Texture.class, new TextureLoader(new InternalFileHandleResolver()));
        getManager().load(path, Texture.class);
        getManager().finishLoading();
    }

    public static void loadFont(String path){
        getManager().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        getManager().load(path, FreeTypeFontGenerator.class);
        getManager().finishLoading();
    }

    public static void dispose(){
        if(manager != null){
            manager.dispose();
        }
    }

}
