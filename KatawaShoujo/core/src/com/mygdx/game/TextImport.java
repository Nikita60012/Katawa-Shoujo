package com.mygdx.game;

import com.badlogic.gdx.Gdx;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

public class TextImport {
   public String[] text = new String[41];
    public String[] reader() {
        String path = System.getProperty("user.dir") + "/assets/Dialogs/prologueText/dialogs.txt";
        path = path.replaceAll("\\\\","/");
        StringBuffer input = new StringBuffer();
        try (FileReader fr = new FileReader(path)) {
            int textChar;
           while ((textChar = fr.read()) != -1){
               input.append((char) textChar);
           }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = input.toString().split("\\r\\n");
        return text;
    }
}

