package com.dungeon.game;

import com.badlogic.gdx.Game;
import com.dungeon.game.screens.TitleScreen;

public class DungeonGame extends Game {

	@Override
	public void create() {
		setScreen(new TitleScreen(this));
	}

}
