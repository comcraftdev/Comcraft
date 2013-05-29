/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import net.comcraft.client.Comcraft;

/**
 *
 * @author Piotr Wójcik
 */
public final class MusicPlayer {

    private Comcraft cc;
    private Vector musicList = new Vector(20);
    private Player player;
    private long nextPlayTime;
    private Random rand = new Random();
    private int lastMusic = -1;

    public MusicPlayer(Comcraft cc) {
        this.cc = cc;
    }

    public void loadMusicList() {
        musicList.removeAllElements();

        musicList.addElement("/music/Comcraft.mp3");

        if (!cc.settings.getComcraftFileSystem().isAvailable()) {
            return;
        }

        loadMusicFromMainFolder();
//        loadMusicFromTexturepack();
    }

    private void loadMusicFromTexturepack() {
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("texturepacks/") + "music/", Connector.READ);

            if (!fileConnection.exists()) {
                return;
            }

            Vector elements = FileSystemHelper.getElementsList(fileConnection);

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);

                if (elementName.endsWith(".mp3")) {
                    musicList.addElement(elementName);
                }
            }

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        }
    }

    private void loadMusicFromMainFolder() {
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("music/"), Connector.READ);

            Vector elements = FileSystemHelper.getElementsList(fileConnection);

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);

                if (elementName.endsWith(".mp3") || elementName.endsWith(".wav") || elementName.endsWith(".midi")) {
                    musicList.addElement(elementName);
                }
            }

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        }
    }

    private void startPlayingMusic() {
        int i = rand.nextInt(musicList.size());

        if (musicList.size() > 1) {
            while (i == lastMusic) {
                i = rand.nextInt(musicList.size());
            }
        }

        lastMusic = i;

        String path = (String) musicList.elementAt(i);

        InputStream inputStream = null;

        if (path.startsWith("/")) {
            inputStream = MusicPlayer.class.getResourceAsStream(path);
        } else {
            try {
                inputStream = (InputStream) Connector.openInputStream(path);
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
            }
        }

        if (inputStream != null) {
            try {
                player = Manager.createPlayer(inputStream, getContentType(path));
                player.start();

                VolumeControl control = (VolumeControl) player.getControl("VolumeControl");
                control.setLevel(20);
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
                player = null;
            } catch (MediaException ex) {
                //#debug
//#                 ex.printStackTrace();
                player = null;
            }
        }
    }

    public static String getContentType(String path) {
        if (path.endsWith(".mp3")) {
            return "audio/mpeg";
        }
        if (path.endsWith(".wav")) {
            return "audio/x-wav";
        }
        if (path.endsWith(".midi")) {
            return "audio/midi";
        }

        return "";
    }

    public void tickMusicPlayer() {
        if (cc.isGamePaused || !cc.settings.sounds) {
            if (player != null) {
                try {
                    player.stop();
                } catch (MediaException ex) {
                    //#debug
//#                     ex.printStackTrace();
                }
                player.close();

                player = null;
            }
        } else {
            long currentTime = System.currentTimeMillis();

            if (player == null) {
                if (currentTime > nextPlayTime && !musicList.isEmpty()) {
                    startPlayingMusic();
                }
            } else {
                if (player.getState() == Player.PREFETCHED) {
                    player.close();

                    nextPlayTime = currentTime + rand.nextInt(20) * 1000 + 15000;

                    player = null;
                }
            }
        }
    }
}