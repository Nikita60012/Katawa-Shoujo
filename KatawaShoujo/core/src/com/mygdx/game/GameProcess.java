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

public class GameProcess implements Screen {
    final Katawa game;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton backButton;
    private OrthographicCamera camera;
    private Texture background;
    private Sounds sound;
    private Stage stage;


    public GameProcess(final Katawa game){
        this.game = game;


        background = new Texture(Gdx.files.internal("Texture/MainMenu/menuBackground.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        sound = new Sounds("MainMenu");

        stage = new Stage(new StretchViewport(game.WIDTH, game.HEIGHT));

        Gdx.input.setInputProcessor(stage);


        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.comicSans;
        buttonStyle.fontColor = Color.valueOf("#8E8574");
        buttonStyle.overFontColor = Color.valueOf("#aba498");

        backButton= new TextButton("Назад",buttonStyle);
        backButton.setPosition(135, 80);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.playing();
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });
        stage.addActor(backButton);
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

        if(stage != null){
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
        game.batch.begin();
        game.comicSans.setColor( Color.BROWN);
        game.comicSans.getData().setScale(1,1);
        game.comicSans.draw(game.batch, game.madeInProgress, game.WIDTH /2 - 130, game.HEIGHT/2 + 20);
        game.batch.end();

        if(backButton.isPressed()){
            backButton.setPosition(135, 70);
        }else{
            backButton.setPosition(135, 80);
        }

        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int width, int height) {
        if(stage != null)stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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