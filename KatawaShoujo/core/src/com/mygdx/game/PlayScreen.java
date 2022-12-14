package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import javafx.scene.Parent;

public class PlayScreen implements Screen {
    final Katawa game;

    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private Player player;

    private Vector2 npcVelocity = new Vector2();
    private Vector2 carVelocity = new Vector2();

    private float playerX, playerY;
    private int documentNumber = 1;

    private Sounds soundOfHit;

    private Texture pauseBackground;
    private Texture dialogBox;
    private Sprite npc;
    private Sprite car;
    private Image ending;

    private TextButton exitButton;
    private TextButton backToMenuButton;
    private TextButton.TextButtonStyle buttonStyle;

    private Stage stage;
    private Stage dialog;
    private Stage endingStage;

    private String[] text;
    private String textOutput;
    private String stroke;
    private Label txt;
    private Label name;
    private Label.LabelStyle labelStyle;
    private TextImport reader;

    private Sounds click;
    private Skin skinSlider;
    private Slider sound;
    private int soundValue = 30;
    private Slider.SliderStyle sliderStyle;

    private boolean pause, speaking, npcMoving, end, carSetY, carHit = false;

    public PlayScreen(final Katawa game){
        this.game = game;
    }

    @Override
    public void show() {
        //?????????????????? ??????????
        TiledMap map = new TmxMapLoader().load("maps/map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        //?????????????????? ????????????
        camera = new OrthographicCamera();
        camera.setToOrtho( false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        camera.translate(520,200);
        //?????????????????? ????????????
        player = new Player(new Sprite(new Texture("player/player.png")), (TiledMapTileLayer) map.getLayers().get("col"));
        player.setPosition(520, 33);

        //?????????????????? NPC
        npc = new Sprite(new Texture("NPC/prologNpc.png"));
        npc.setPosition(525,-450);
        npc.setSize(12,30);
        npcVelocity.y = 100;

        //?????????????????? ????????????
        car = new Sprite(new Texture("Texture/Car/car.png"));
        car.setSize(40,30);
        car.setPosition(3000, playerY);
        carVelocity.x = -500;

        //?????????????????? ?????????????? ???????????? ?? ????????????
        ending = new Image(new Texture("Texture/End/ending.png"));
        ending.setPosition(0,0);
        ending.setSize(2000,1000);

        //?????????????????? ?????????? ?????????????????????? ????????
        labelStyle = new Label.LabelStyle();
        labelStyle.font = game.comicSans;
        labelStyle.fontColor = Color.valueOf("#8E8574");

        reader = new TextImport();

        //?????????????????? ???????????? ??????????????
        txt = new Label("", labelStyle);
        txt.setSize(1150,175);
        txt.setWrap(true);
        txt.setFontScale(0.7f);
        txt.setPosition(70,-25);

        name = new Label("",labelStyle);
        name.setSize(1150,175);
        name.setFontScale(0.7f);
        name.setPosition(65,50);
        //?????????????????? ???????????????? ?????????????????????? ????????
        dialogBox = new Texture(Gdx.files.internal("Dialogs/dialogBox.png"));

        //?????????????????? ???????????????? ???????? ??????????
        pauseBackground = new Texture(Gdx.files.internal("Texture/Pause/pauseBackground.png"));

        stage = new Stage(new StretchViewport(game.WIDTH,game.HEIGHT));
        dialog = new Stage(new StretchViewport(game.WIDTH,game.HEIGHT));
        endingStage = new Stage(new StretchViewport(game.WIDTH,game.HEIGHT));

        click = new Sounds("MainMenu");

        //?????????????????? ?????????? ???????????? ??????????
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.comicSans;
        buttonStyle.fontColor = Color.valueOf("#8E8574");
        buttonStyle.overFontColor = Color.valueOf("#aba498");

        //???????????????????? ???????????? ??????????
        exitButton = new TextButton("??????????", buttonStyle);
        exitButton.setPosition(game.WIDTH/2 - 15, game.HEIGHT/2 - 100);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click.playing();
                Gdx.app.exit();
            }
        });
        backToMenuButton = new TextButton("?????????????????? ?? ?????????????? ????????", buttonStyle);
        backToMenuButton.setPosition(game.WIDTH/2 - 170, game.HEIGHT/2 - 60);
        backToMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click.playing();
                game.music.musicSound.stop();
                game.music.music("MainMenu");
                game.music.musicSound.setVolume(0.3f);
                game.music.musicSound.setLooping(true);
                game.setScreen(new MainMenu(game));
            }
        });

        //?????????????????? ?? ???????????????????? ???????????????? ??????????
        final TextureAtlas sliderTexture = new TextureAtlas(Gdx.files.internal("Texture/MainMenu/SoundSlider.pack"));
        skinSlider = new Skin();
        skinSlider.addRegions(sliderTexture);
        sliderStyle = new Slider.SliderStyle(new NinePatchDrawable(skinSlider.getPatch("Slider")), new NinePatchDrawable(skinSlider.getPatch("SliderPoint")));

        sound = new Slider(0, 100, 1, false, sliderStyle);
        sound.setPosition(game.WIDTH/2 - 165,game.HEIGHT/2 + 20);
        sound.setSize(290,sound.getPrefHeight());
        sound.setAnimateDuration(0);
        sound.setValue(30);

        sound.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundValue = (int)sound.getValue();
                game.music.musicSound.setVolume(soundValue * 0.01f);
            }
        });


        //???????????????????? ???????? ?????????????? ???? ??????????
        stage.addActor(sound);
        stage.addActor(backToMenuButton);
        stage.addActor(exitButton);

        dialog.addActor(txt);
        dialog.addActor(name);

        endingStage.addActor(ending);

        camera.update();
    }

    @Override
    public void render(float delta) {
        if(!pause && !player.isDead && !end) {

            //?????????????????? ???????????????? ??????????????????
            Gdx.input.setInputProcessor(player);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            renderer.setView(camera);
            renderer.render();
            renderer.getBatch().begin();
            player.setSize(10, 30);
            player.draw(renderer.getBatch());
            renderer.getBatch().end();
            playerX = player.getX();
            playerY = player.getY();

            //?????????????????????? ?????? ????????????
            float h = camera.viewportWidth;
            float w = camera.viewportHeight;

            camera.position.set(playerX, playerY, 0);

            if (camera.position.x < w - 8) {
                camera.position.x = w - 8;
            } else if (camera.position.x > 1095 - w) {
                camera.position.x = 1095 - w;
            }
            if (camera.position.y < h / 3) {
                camera.position.y = h / 3;
            } else if (camera.position.y > h - h / 3 + 65) {
                camera.position.y = h - h / 3 + 65;
            }

            camera.update();
        }
            if(!pause && !player.isDead) {
                //???????????? ??????????????
                if (Gdx.input.isKeyJustPressed(Input.Keys.E) && playerX >= 485 && playerX <= 600 && playerY >= 250 && playerY <= 350) {
                    speaking = !speaking;
                }

                //???????????? ??????????????
                if (speaking && playerX >= 485 && playerX <= 600 && playerY >= 250 && playerY <= 350) {


                    //???????????????? NPC

                    renderer.getBatch().begin();

                    npc.draw(renderer.getBatch());

                    if (npc.getY() < 220 && npcMoving) {
                        npc.setPosition(npc.getX(), npc.getY() + npcVelocity.y * delta);
                    } else if (npc.getY() >= 220 && npcMoving) {
                        npcVelocity.y = 0;
                        npcMoving = false;
                    }
                    renderer.getBatch().end();

                    //???????????? ?????????????????????? ????????
                    if (!npcMoving && !end) {
                        game.batch.begin();
                        game.batch.draw(dialogBox, 40, 10, 1200, 150);
                        if (documentNumber == 5 || documentNumber == 17 || documentNumber == 23) {
                            textOutput = "??????????";
                            name.setText(textOutput);
                        } else if (documentNumber == 11) {
                            textOutput = "???";
                            name.setText(textOutput);
                        } else if (documentNumber == 20 || documentNumber == 30 || documentNumber == 31 || documentNumber == 32 || documentNumber == 36 || documentNumber == 37 || documentNumber == 39) {
                            textOutput = "??????????????";
                            name.setText(textOutput);
                        } else {
                            name.setText("");
                        }
                        game.batch.end();
                        game.batch.begin();
                        if (documentNumber < 42) {
                            text = reader.reader();
                            textOutput = " ";
                            game.comicSans.draw(game.batch, textOutput, 70, 0);
                            txt.toFront();
                            dialog.draw();
                            stroke = text[documentNumber];
                            for (int j = 0; j < stroke.length(); j++) {
                                textOutput += stroke.charAt(j);
                                txt.setText(textOutput);
                            }
                        }
                        game.batch.end();

                        //???????????? ????????????

                        renderer.getBatch().begin();
                        if (documentNumber == 41 && !end) {
                            if(!carSetY) {
                                game.music.musicSound.stop();
                                game.music.music("Car");
                                game.music.musicSound.setVolume(0.1f);
                                car.setY(playerY);
                                carSetY = true;
                            }
                                player.velocity.x = 0;
                                player.velocity.y = 0;
                            car.draw(renderer.getBatch());

                            if(car.getX() <= playerX + 1200 && car.getX() >= playerX + 1100 && !carHit){
                                soundOfHit = new Sounds("CarHit");
                                soundOfHit.playing();
                                carHit = true;
                            }
                            if (car.getX() > playerX ) {
                                car.setPosition(car.getX() + carVelocity.x * delta, car.getY());
                            } else if (car.getX() <= playerX ) {
                                npcVelocity.x = 0;
                                end = true;
                            }
                        }
                        renderer.getBatch().end();

                        //????????????????????????????
                        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && documentNumber < 41) {
                            documentNumber++;
                            if (documentNumber == 10)
                                npcMoving = true;
                        }
                    }
                    //????????????????
                    if (end) {
                        endingStage.draw();
                        game.music.musicSound.stop();
                        game.batch.begin();
                        game.comicSans.setColor(Color.valueOf("#8E8574"));
                        game.comicSans.draw(game.batch, "?????????????????????? ??????????????...", 475, 360);
                        game.batch.end();
                    }
                }
            }
        //?????????? ??????????

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            if(!pause){
                pause = true;
                Gdx.input.setInputProcessor(stage);
            }else{
                pause = false;
            }
        }

        //???????????? ??????????
            if(pause && !player.isDead) {

                game.batch.begin();
                game.batch.draw(pauseBackground, game.WIDTH / 2 - 200, game.HEIGHT / 2 - 125);
                game.batch.end();

                game.batch.begin();
                game.comicSans.setColor(Color.valueOf("#8E8574"));
                game.comicSans.getData().setScale(0.5f, 0.5f);
                game.comicSans.draw(game.batch, soundValue + "", game.WIDTH / 2 + 130, game.HEIGHT / 2 + 35);
                game.comicSans.getData().setScale(1, 1);
                game.batch.end();

                game.batch.begin();
                game.comicSans.getData().setScale(0.75f, 0.75f);
                game.comicSans.draw(game.batch, "?????????????????? ????????????", game.WIDTH / 2 - 165, game.HEIGHT / 2 + 70);
                game.comicSans.getData().setScale(1, 1);
                game.batch.end();

                if (stage != null) {
                    stage.act(Gdx.graphics.getDeltaTime());
                    stage.draw();
                }
                if (exitButton.isPressed()) {
                    exitButton.setPosition(game.WIDTH / 2 - 15, game.HEIGHT / 2 - 110);
                } else {
                    exitButton.setPosition(game.WIDTH / 2 - 15, game.HEIGHT / 2 - 100);
                }
                if (backToMenuButton.isPressed()) {
                    backToMenuButton.setPosition(game.WIDTH / 2 - 170, game.HEIGHT / 2 - 70);
                } else {
                    backToMenuButton.setPosition(game.WIDTH / 2 - 170, game.HEIGHT / 2 - 60);
                }

            }
            //????????????????
        if(player.isDead){
            game.batch.begin();
            game.batch.draw(pauseBackground, game.WIDTH/2 - 200,game.HEIGHT/2 - 125);
            game.comicSans.setColor(Color.valueOf("#8E8574"));
            game.comicSans.draw(game.batch, "???? ???????????????????????????? ?? ????????????", game.WIDTH/2 - 190, game.HEIGHT/2 + 150);
            game.comicSans.draw(game.batch, "?? ???????????? ???????? ???? ????????", game.WIDTH/2 - 190, game.HEIGHT/2 + 120);
            game.comicSans.draw(game.batch, "???????????????? ?????? ????????????????????????", game.WIDTH/2 - 190, game.HEIGHT/2 + 90);
            game.comicSans.draw(game.batch, "?????? ?????????????????????? ????????", game.WIDTH/2 - 190, game.HEIGHT/2 + 50);
            game.comicSans.draw(game.batch, "?????????????? ???????????? ????????????", game.WIDTH/2 - 190, game.HEIGHT/2 + 20);
            game.comicSans.draw(game.batch, "???????? ?? ?????????? ???????????? ??????", game.WIDTH/2 - 190, game.HEIGHT/2 - 10);
            game.comicSans.draw(game.batch, "???? ?????????????????????? ??????????", game.WIDTH/2 - 190, game.HEIGHT/2 - 40);
            game.batch.end();
            game.music.musicSound.stop();
        }
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
    dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        dialog.dispose();
        endingStage.dispose();

    }
}