package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Options implements Screen {

    final Katawa game;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton backButton;
    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;
    private CheckBox.CheckBoxStyle checkBoxStyle;
    private CheckBox fullScreen;
    private Slider.SliderStyle sliderStyle;
    private Slider sound;
    private Sounds clickSound;
    private int soundValue = 30;
    private boolean fullscreenSwitch;
    Skin skinSlider;
    Skin skinCheckBox;

    public Options(final Katawa game){
        this.game = game;


        background = new Texture(Gdx.files.internal("Texture/MainMenu/menuBackground.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        clickSound = new Sounds("MainMenu");

        stage = new Stage(new StretchViewport(game.WIDTH, game.HEIGHT));

        Gdx.input.setInputProcessor(stage);

        //Подгрузка и установка текстур ползунка со звуком
        final TextureAtlas sliderTexture = new TextureAtlas(Gdx.files.internal("Texture/MainMenu/SoundSlider.pack"));
        skinSlider = new Skin();
        skinSlider.addRegions(sliderTexture);
        sliderStyle = new Slider.SliderStyle(new NinePatchDrawable(skinSlider.getPatch("Slider")), new NinePatchDrawable(skinSlider.getPatch("SliderPoint")));

        //Установка расположения ползунка со звуком и задание параметров
        sound = new Slider(0, 100, 1, false, sliderStyle);
        sound.setPosition(135,350);
        sound.setSize(290,sound.getPrefHeight());
        sound.setAnimateDuration(0);
        sound.setValue(30);

        //Реализация изменения громкости звука
        sound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundValue = (int)sound.getValue();
                game.music.musicSound.setVolume(soundValue * 0.01f);
            }
        });
        stage.addActor(sound);

        //Подгрузка текстур для checkBox
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("Texture/MainMenu/fullScreen_checkBox.pack"));
        skinCheckBox = new Skin();

        //Установка текстур для checkBox
        skinCheckBox.addRegions(atlas);
        skinCheckBox.add("font",game.comicSans,BitmapFont.class);

        //Позволяет постоянно показывать текстуру активированного checkbox, если включён полноэкранный режим
        fullscreenSwitch = game.fullScreenMode;

        //Применение стиля для checkBox
        checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = game.comicSans;
        checkBoxStyle.fontColor = Color.valueOf("#8E8574");

        // Реализация изменения текстуры checkbox в зависимости от активности полноэкранного режима
        if(!fullscreenSwitch) {
            checkBoxStyle.up = new NinePatchDrawable(skinCheckBox.getPatch("unchecked_box"));
            checkBoxStyle.down = new NinePatchDrawable(skinCheckBox.getPatch("checked_box"));
        }else{
            checkBoxStyle.up = new NinePatchDrawable(skinCheckBox.getPatch("checked_box"));
            checkBoxStyle.down = new NinePatchDrawable(skinCheckBox.getPatch("unchecked_box"));
        }

        //Установка checkbox на экране
        fullScreen = new CheckBox("", checkBoxStyle);
        fullScreen.setChecked(fullscreenSwitch);
        fullScreen.setPosition(135,200);

        //Изменение полноэкранного режима
        fullScreen.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                clickSound.playing();

                fullscreenSwitch = fullScreen.isChecked();

                if(fullscreenSwitch){
                    checkBoxStyle.up = new NinePatchDrawable(skinCheckBox.getPatch("checked_box"));
                    checkBoxStyle.down = new NinePatchDrawable(skinCheckBox.getPatch("unchecked_box"));
                    Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                    Gdx.graphics.setFullscreenMode(currentMode);
                    game.fullScreenMode = true;

                }else{
                    checkBoxStyle.up = new NinePatchDrawable(skinCheckBox.getPatch("unchecked_box"));
                    checkBoxStyle.down = new NinePatchDrawable(skinCheckBox.getPatch("checked_box"));
                    Gdx.graphics.setWindowedMode(game.WIDTH, game.HEIGHT);
                    game.fullScreenMode = false;
                }



            }

        });

        //Установка стиля кнопки возврата
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.comicSans;
        buttonStyle.fontColor = Color.valueOf("#8E8574");
        buttonStyle.overFontColor = Color.valueOf("#aba498");

        //Реализация кнопки возврата
        backButton= new TextButton("Назад",buttonStyle);
        backButton.setPosition(135, 80);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickSound.playing();
                game.setScreen(new MainMenu(game));
                dispose();
            }
        });
        stage.addActor(fullScreen);
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
        game.comicSans.draw(game.batch, game.madeInProgress, game.WIDTH/2 - 130, game.HEIGHT/2 + 20);
        game.batch.end();

        game.batch.begin();
        game.comicSans.setColor(Color.valueOf("#8E8574"));
        game.comicSans.getData().setScale(0.5f,0.5f);
        game.comicSans.draw(game.batch, soundValue + "", 440, 365);
        game.comicSans.getData().setScale(1,1);
        game.batch.end();

        game.batch.begin();
        game.comicSans.getData().setScale(0.75f,0.75f);
        game.comicSans.draw(game.batch, "Громкость музыки",135,390 );
        game.comicSans.getData().setScale(1,1);
        game.batch.end();

        game.batch.begin();
        game.comicSans.getData().setScale(0.6f,0.6f);
        game.comicSans.draw(game.batch, "Полноэкрамный режим",135,290 );
        game.comicSans.getData().setScale(1,1);
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