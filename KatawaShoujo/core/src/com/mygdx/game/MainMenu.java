package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;


public class MainMenu implements Screen {

    final Katawa game;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton exitButton;
    private TextButton optionsButton;
    private TextButton loadButton;
    private TextButton startButton;
    private OrthographicCamera camera;
    private Sounds sound;
    private Texture background;
    private Stage stage;


    public MainMenu(final Katawa game) {
        this.game = game;

        background = new Texture(Gdx.files.internal("Texture/MainMenu/menuBackground.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        sound = new Sounds("MainMenu");


        stage = new Stage(new StretchViewport(game.WIDTH,game.HEIGHT));

        Gdx.input.setInputProcessor(stage);

        //Задаётся стиль кнопок
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.comicSans;
        buttonStyle.fontColor = Color.valueOf("#8E8574");
        buttonStyle.overFontColor = Color.valueOf("#aba498");

        //Реализуется кнопка выхода
        exitButton = new TextButton("Выход", buttonStyle);
        exitButton.setPosition(135, 80);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.playing();
                Gdx.app.exit();
            }
        });

        //Реализуется кнопка настроек
        optionsButton = new TextButton("Настройки", buttonStyle);
        optionsButton.setPosition(135,130);
        optionsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.playing();
                game.setScreen(new Options(game));
                dispose();
            }
        });

        //Реализуется кнопка загрузки
        loadButton = new TextButton("Загрузить", buttonStyle);
        loadButton.setPosition(135,180);
        loadButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.playing();
                game.setScreen(new Loads(game));
                dispose();
            }
        });

        //Реализуется кнопка начала новой игры
        startButton = new TextButton("Новая игра", buttonStyle);
        startButton.setPosition(135,230);
        startButton.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.playing();
                game.setScreen(new PlayScreen(game));
                game.music.musicSound.stop();
                game.music.music("FirstScreen");
                game.music.musicSound.setVolume(0.3f);
                game.music.musicSound.setLooping(true);
                dispose();
            }
        });


        stage.addActor(startButton);
        stage.addActor(loadButton);
        stage.addActor(exitButton);
        stage.addActor(optionsButton);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        camera.update();

        game.batch.begin();
        game.batch.draw(background, 0, 0, game.WIDTH, game.HEIGHT);
        game.batch.end();


        if (stage != null) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        if (startButton.isPressed()) {
            startButton.setPosition(135, 220);
        } else {
            startButton.setPosition(135, 230);
        }

        if (loadButton.isPressed()) {
            loadButton.setPosition(135, 170);
        } else {
            loadButton.setPosition(135, 180);
        }

        if (optionsButton.isPressed()) {
            optionsButton.setPosition(135, 120);
        } else {
            optionsButton.setPosition(135, 130);
        }

        if (exitButton.isPressed()) {
            exitButton.setPosition(135, 70);
        } else {
            exitButton.setPosition(135, 80);
        }

        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        if(stage != null)stage.getViewport().update(width, height, true);
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
        background.dispose();
        stage.dispose();
    }
}