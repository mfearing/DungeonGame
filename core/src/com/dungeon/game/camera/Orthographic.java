package com.dungeon.game.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Orthographic {

    public OrthographicCamera camera;
    public Viewport viewport;

    public Orthographic(int worldSize){
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(worldSize, worldSize,worldSize,worldSize, camera);
    }

    public void resize(int width, int height){
        viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }
}
