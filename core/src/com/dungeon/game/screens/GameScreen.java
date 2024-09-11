package com.dungeon.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dungeon.game.camera.Orthographic;
import com.dungeon.game.components.AssetComponent;
import com.dungeon.game.components.CollisionComponent;
import com.dungeon.game.components.InputComponent;
import com.dungeon.game.components.TileMapComponent;
import com.dungeon.game.entities.Player;

public class GameScreen implements Screen {

    public static final int TILE_SIZE = 32,
            TILE_ROWS_COLS = 15,
            WORLD_SIZE = TILE_SIZE * TILE_ROWS_COLS;
    public static final float UNIT_SCALE = 1f / TILE_SIZE;

    Game game;
    public InputComponent inputComponent;
    private Orthographic orthographic;
    public TileMapComponent tileMapComponent;
    public CollisionComponent collisionComponent;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Player player;

    public GameScreen(Game game){
        this.game = game;
        inputComponent = new InputComponent();
        Gdx.input.setInputProcessor(inputComponent);

        collisionComponent = new CollisionComponent(this);
        shapeRenderer = new ShapeRenderer();

        orthographic = new Orthographic(WORLD_SIZE);
        batch = new SpriteBatch();
        tileMapComponent = new TileMapComponent("maps/dungeonMap.tmx");
    }

    @Override
    public void show() {
        //this is actuall the create for the Screen.  Anything requiring this game screen gets initialized here
        player = new Player(this,64, 64);
    }

    public void update(float delta){
        player.update(delta);
    }

    @Override
    public void render(float v) {
        update(v);

        ScreenUtils.clear(0, 0, 0, 1);
        orthographic.camera.update();
        tileMapComponent.render(orthographic.camera); //has its own batch, everything afterward is rendered on top (I think)
        batch.begin();
        player.render(batch);
        batch.end();

        Rectangle rec = player.getScreenSolidArea();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(rec.x, rec.y, rec.getWidth(), rec.getHeight());
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        orthographic.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        tileMapComponent.dispose();
        player.dispose();
        AssetComponent.dispose();
    }
}
