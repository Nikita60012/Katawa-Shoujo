package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TextImport {
   public String text = "";
    public String reader(int number) {
        StringBuffer input = new StringBuffer();
        try (FileReader fr = new FileReader("D:/Users/Никита/Desktop/KatawaShoujo/assets/Dialogs/prologueText/text" + number + ".txt")) {
           int textChar;
           while ((textChar = fr.read()) != -1){
               input.append((char) textChar);
           }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = input.toString();
        return text;
    }
}

