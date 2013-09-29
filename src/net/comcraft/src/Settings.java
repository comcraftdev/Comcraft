package net.comcraft.src;
/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.m3g.Graphics3D;

import net.comcraft.client.Comcraft;

public final class Settings {

    private final String recordStoreName = "comcraft_settings_1.2.3";
    private final Comcraft cc;
    private ComcraftFileSystem comcraftFileSystem;
    private RMS rms;
    public String texturepack;
    public boolean showFps;
    public boolean debugInfo;
    public int renderDistance;
    public boolean renderGui;
    public boolean fancyGraphics;
    public boolean memorySaveMode;
    public boolean ignoreOutOfMemory;
    public int autosaveTime;
    private boolean isFirstRun;
    public boolean antialiasing;
    public boolean vibrations;
    public boolean sounds;
    public boolean fog;
    public float fov;
    public String language;
    public boolean screenshotMode;
    public int resolutionScale;
    public int screenshotResolution;
    public String[] disabledMods;

    public Settings(Comcraft cc) {
        this.cc = cc;
        comcraftFileSystem = new ComcraftFileSystem(cc, null);
        texturepack = "Default";
        showFps = false;
        debugInfo = false;
        renderDistance = 1;
        renderGui = true;
        fancyGraphics = true;
        memorySaveMode = false;
        ignoreOutOfMemory = false;
        autosaveTime = -1;
        isFirstRun = false;
        vibrations = true;
        sounds = true;
        fov = 90f;
        fog = false;
        language = "/lang/en.lng";
        screenshotMode = false;
        resolutionScale = 1;
        screenshotResolution = 2;
        disabledMods = new String[0];

        Boolean antialiasingB = (Boolean) (Graphics3D.getProperties().get("supportAntialiasing"));
        antialiasing = antialiasingB.booleanValue();

        rms = RMS.openRecordStore(recordStoreName, true);

        if (!rms.hasAnyRecord()) {
            initRecordStore(rms);
            isFirstRun = true;
        }

        rms.closeRecordStore();
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    private void initRecordStore(RMS rms) {
        rms.addRecord();
        saveOptions();
    }

    public void loadOptions() {
        rms = RMS.openRecordStore(recordStoreName, false);
        DataInputStream dataInputStream = rms.getRecord(1);

        try {
            comcraftFileSystem = new ComcraftFileSystem(cc, dataInputStream.readUTF());
            texturepack = dataInputStream.readUTF();
            fancyGraphics = dataInputStream.readBoolean();
            renderDistance = dataInputStream.readInt();
            memorySaveMode = dataInputStream.readBoolean();
            ignoreOutOfMemory = dataInputStream.readBoolean();
            autosaveTime = dataInputStream.readInt();
            antialiasing = dataInputStream.readBoolean();
            vibrations = dataInputStream.readBoolean();
            sounds = dataInputStream.readBoolean();
            fov = dataInputStream.readFloat();
            fog = dataInputStream.readBoolean();
            language = dataInputStream.readUTF();
            resolutionScale = dataInputStream.readInt();
            screenshotResolution = dataInputStream.readInt();
            readDisabledMods(dataInputStream);
        } catch (IOException ex) {
            //#debug debug
//#             ex.printStackTrace();
        }

        try {
            dataInputStream.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        }

        rms.closeRecordStore();

        comcraftFileSystem.initComcraftFileSystem();
    }


    public void saveOptions() {
        rms = RMS.openRecordStore(recordStoreName, false);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF(comcraftFileSystem.getComcraftPath());
            dataOutputStream.writeUTF(texturepack);
            dataOutputStream.writeBoolean(fancyGraphics);
            dataOutputStream.writeInt(renderDistance);
            dataOutputStream.writeBoolean(memorySaveMode);
            dataOutputStream.writeBoolean(ignoreOutOfMemory);
            dataOutputStream.writeInt(autosaveTime);
            dataOutputStream.writeBoolean(antialiasing);
            dataOutputStream.writeBoolean(vibrations);
            dataOutputStream.writeBoolean(sounds);
            dataOutputStream.writeFloat(fov);
            dataOutputStream.writeBoolean(fog);
            dataOutputStream.writeUTF(language);
            dataOutputStream.writeInt(resolutionScale);
            dataOutputStream.writeInt(screenshotResolution);
            writeDisabledMods(dataOutputStream);
        } catch (IOException ex) {
            //#debug debug
//#             ex.printStackTrace();
        }

        rms.setRecord(1, byteArrayOutputStream);

        rms.closeRecordStore();
    }

    public ComcraftFileSystem getComcraftFileSystem() {
        return comcraftFileSystem;
    }

    public void setComcraftFileSystem(ComcraftFileSystem comcraftFileSystem) {
        this.comcraftFileSystem = comcraftFileSystem;
    }
    private void readDisabledMods(DataInputStream dataInputStream) throws IOException {
        int len= dataInputStream.readShort();
        disabledMods = new String[len];
        for (int i = 0; i < len; i++) {
            disabledMods[i]=dataInputStream.readUTF();
        }
    }
    private void writeDisabledMods(DataOutputStream dataOutputStream) throws IOException {
        String[] disMods = new String[disabledMods.length];
        int x = 0;
        for (int i = 0; i < disabledMods.length; i++) {
            if (disabledMods[i] != null) {
                disMods[x++] = disabledMods[i];
            }
        }
        disabledMods = new String[x];
        System.arraycopy(disMods, 0, disabledMods, 0, x);
        dataOutputStream.writeShort(disabledMods.length);
        for (int i = 0; i < disabledMods.length; i++) {
            dataOutputStream.writeUTF(disabledMods[i]);
        }
    }
}
