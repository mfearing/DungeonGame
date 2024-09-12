package com.dungeon.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.dungeon.game.camera.Orthographic;
import com.dungeon.game.components.AssetComponent;
import com.dungeon.game.components.InputComponent;

import java.util.HashMap;
import java.util.Map;

public class TitleScreen implements Screen {

    private static final String FONT_PATH = "fonts/pixel_font.ttf";

    Game game;
    public static final int TILE_SIZE = 32,
            TILE_ROWS_COLS = 15,
            WORLD_SIZE = TILE_SIZE * TILE_ROWS_COLS;

    private InputComponent inputComponent;
    private Orthographic orthographic;
    private SpriteBatch spriteBatch;
    private BitmapFont titleFont;
    private BitmapFont optionFont;
    private BitmapFont controlFont;
    private GlyphLayout layout;

    TextureAtlas atlas;

    private final Map<Integer, String> options = new HashMap<>();
    private boolean currentOption;

    public TitleScreen(Game game){
        this.game = game;
    }

    @Override
    public void show() {
        inputComponent = new InputComponent();
        orthographic = new Orthographic(WORLD_SIZE);
        spriteBatch = new SpriteBatch();
        layout = new GlyphLayout();

        Gdx.input.setInputProcessor(inputComponent);

        AssetComponent.loadFont(FONT_PATH);
        FreeTypeFontGenerator generator = AssetComponent.getManager().get(FONT_PATH, FreeTypeFontGenerator.class);

        AssetComponent.loadAtlas("atlas/player.atlas");
        atlas = AssetComponent.getManager().get("atlas/player.atlas", TextureAtlas.class);

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.size = TILE_SIZE * 2 - 4;
        titleFont = generator.generateFont(param);

        param.size = (int)(TILE_SIZE * 1.5f);
        optionFont = generator.generateFont(param);

        param.size = TILE_SIZE;
        controlFont = generator.generateFont(param);

        options.put(1, "Start Game");
        options.put(2, "Exit Game");
        currentOption = true;
    }

    @Override
    public void render(float v) {
        update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        orthographic.camera.update();
        spriteBatch.setProjectionMatrix(orthographic.camera.combined);

        spriteBatch.begin();
        drawTitle();
        drawOptions();
        spriteBatch.end();
    }

    public void update(){
        if(inputComponent.upPressed || inputComponent.downPressed){
            currentOption = !currentOption;
            inputComponent.upPressed = false;
            inputComponent.downPressed = false;
        }

        if(inputComponent.enterPressed){
            if(currentOption){
                game.setScreen(new GameScreen(game));
            } else {
                Gdx.app.exit();
            }
            inputComponent.enterPressed = false;
        }
    }

    public void drawTitle(){
        String title = "Dungeon Game";
        layout.setText(titleFont, title);
        float x = (orthographic.camera.viewportWidth - layout.width) / 2;
        float y = (orthographic.camera.viewportHeight - layout.height);
        titleFont.draw(spriteBatch, title, x, y);
    }

    public void drawOptions(){
        float x, y;

        TextureRegion textureRegion = atlas.findRegion("playerDown1");

        //options
        layout.setText(optionFont, options.get(1));
        x = (orthographic.camera.viewportWidth - layout.width) / 2;
        y = (orthographic.camera.viewportHeight - layout.height) / 2 + TILE_SIZE*3;
        optionFont.draw(spriteBatch, options.get(1), x, y);
        if(currentOption){
            spriteBatch.draw(textureRegion, x - TILE_SIZE - 4, y - TILE_SIZE);
        }

        layout.setText(optionFont, options.get(2));
        x = (orthographic.camera.viewportWidth - layout.width) / 2;
        y = y - TILE_SIZE - layout.height;
        optionFont.draw(spriteBatch, options.get(2), x, y);
        if(!currentOption){
            spriteBatch.draw(textureRegion, x - TILE_SIZE - 4, y - TILE_SIZE);
        }

        //controls
        layout.setText(controlFont, "WASD - Move");
        x = (orthographic.camera.viewportWidth - layout.width) / 2;
        y = y - TILE_SIZE * 4;
        controlFont.draw(spriteBatch, "WASD - Move", x, y);

        layout.setText(controlFont, "ENTER - Interact");
        x = (orthographic.camera.viewportWidth - layout.width) / 2;
        y = y - TILE_SIZE;
        controlFont.draw(spriteBatch, "ENTER - Interact", x, y);

    }


    @Override
    public void resize(int i, int i1) {
        orthographic.resize(i, i1);
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
        spriteBatch.dispose();
        titleFont.dispose();
        optionFont.dispose();
        controlFont.dispose();
    }
}
