package com.dungeon.game;

import com.badlogic.gdx.Game;
import com.dungeon.game.screens.GameScreen;

public class DungeonGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen(this));
	}

}
