package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    public Sound sound;
    private String typeOfSound;
    public Sounds(String type){
        this.typeOfSound = type;
    }

    public void playing(){
        if(typeOfSound.equals("MainMenu")){
            sound = Gdx.audio.newSound(Gdx.files.internal("Sound/mainMenuClick.mp3"));
            long id = sound.play(1);
            sound.setPitch(id, 0.5f);
        }
    }
}
