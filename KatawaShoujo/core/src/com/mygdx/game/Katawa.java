package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Katawa extends Game {


	public SpriteBatch batch;

	BitmapFont comicSans;
	final String madeInProgress = "В процессе разработки";
	final int WIDTH = 1280;
	final int HEIGHT = 720;
	boolean fullScreenMode = false;
	TypeOfMusic music = new TypeOfMusic();;

	@Override
	public void create () {
		batch = new SpriteBatch();

		//Реализуется кириллица в шрифте ComicSans
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FontComicSans.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.characters = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЗЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,!?-0123456789+_=:;*/";
		comicSans = generator.generateFont(parameter);
		generator.dispose();

		//Вызов класса с музыкой
		music.music("MainMenu");
		music.musicSound.setVolume(0.3f);

		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {super.render();}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		comicSans.dispose();
		music.musicSound.dispose();
	}

}