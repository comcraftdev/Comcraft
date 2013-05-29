package net.comcraft.src;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Vector;
import net.comcraft.client.Comcraft;

public class HelloWords {

    private Vector helloWordsList;
    private Comcraft cc;

    public HelloWords(Comcraft cc) {
        this.cc = cc;
        helloWordsList = new Vector(50);
        loadWords();
    }

    public String getRandomWord() {
        if (helloWordsList.isEmpty()) {
            return "";
        }

        Random random = new Random();

        return (String) helloWordsList.elementAt(random.nextInt(helloWordsList.size()));
    }

    private void loadWords() {
        BufferedReader bufferedReader;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(cc.texturePackList.getResourceAsStream("hello_words.txt"), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            //#debug
//#             ex.printStackTrace();
            bufferedReader = new BufferedReader(new InputStreamReader(cc.texturePackList.getResourceAsStream("hello_words.txt")));
        }

        while (!bufferedReader.getReachedTheEnd()) {
            String word = bufferedReader.readLine();

            if (word != null) {
                helloWordsList.addElement(word);
            }
        }
    }
}
