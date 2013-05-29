/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public final class SoundProvider {

    private Comcraft cc;
    private Hashtable soundsList = new Hashtable(10);
    private boolean soundsLoaded = false;

    public SoundProvider(Comcraft cc) {
        this.cc = cc;
    }

    public Player getSound(String name) {
        if (!soundsLoaded) {
            loadSounds();
        }
        
        return (Player) soundsList.get(name);
    }
    
    public void playSound(String path) {
        try {
            Player player = getSound(path);
            
            if (player != null) {
                player.start();
            }
        } catch (MediaException ex) {
            //#debug
//#             ex.printStackTrace();
        }
    }
    
    public void loadSounds() {
        if (!cc.settings.sounds) {
            return;
        }
        
        loadSoundPlayer("/sound/block_placed.wav");
        
        soundsLoaded = true;
    }

    private void loadSoundPlayer(String path) {
        InputStream inputStream = SoundProvider.class.getResourceAsStream(path);

        Player player = null;

        try {
            player = Manager.createPlayer(inputStream, MusicPlayer.getContentType(path));
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        } catch (MediaException ex) {
            //#debug
//#             ex.printStackTrace();
        }

        soundsList.put(path, player);
    }
}