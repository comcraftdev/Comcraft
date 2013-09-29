package net.comcraft.src;

import java.util.Vector;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.comcraft.client.Comcraft;

public class ModLoader {
    private String version = "0.2";
    private Comcraft cc;
    private Vector Mods;
    private boolean hasInitialized = false;

    public ModLoader(Comcraft cc) {
        this.cc = cc;
        Mods = new Vector();
        System.out.println("Comcraft ModLoader " + version + " Initialized");
    }

    public void initMods() {
        try {
            if (!cc.settings.getComcraftFileSystem().isAvailable()) {
                System.out.println("files not initialized");
                return;
            }
            System.out.println("scanning mods folder");
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("mods/"), Connector.READ);
            if (!fileConnection.exists()) {
                System.out.println("missing folder '/mods'");
                return;
            }
            hasInitialized = true;

            Vector elements = FileSystemHelper.getElementsList(fileConnection);
            fileConnection.close();

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);
                String ModName, ModDescription, MainClass;
                if (elementName.endsWith("/")) {
                    FileConnection modInfFile = open(elementName + "ccmod.info");
                    DataInputStream dis;
                    if (!modInfFile.exists()) {
                        continue;
                        // ModName="Missing Info";
                        // ModDescription="Folder found, ccmod.info missing";
                    } else {
                        BufferedReader ModInfo = new BufferedReader(new InputStreamReader(modInfFile.openInputStream()));
                        ModName = ModInfo.readLine().trim();
                        ModDescription = ModInfo.readLine().trim();
                        MainClass = ModInfo.readLine().trim().replace('.', '/');
                        ModInfo.close();
                        FileConnection classfile;
                        classfile = open(elementName + MainClass + ".ccm");
                        if (!classfile.exists()) {
                            classfile.close();
                            classfile = open(elementName + MainClass + ".jclass");
                        }
                        if (!classfile.exists()) {
                            classfile.close();
                            continue;
                        }
                        dis = new DataInputStream(classfile.openInputStream());
                    }
                    modInfFile.close();
                    Mods.addElement(new Mod(this, ModName, ModDescription, dis));
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public Vector ListMods() {
        return Mods;
    }

    public boolean isInitialized() {
        return hasInitialized;
    }

    private FileConnection open(String filename) throws IOException {
        return (FileConnection) Connector.open(filename, Connector.READ);
    }

    public boolean isDisabled(String name) {
        for (int i = 0; i < cc.settings.disabledMods.length; i++) {
            if (cc.settings.disabledMods[i].equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void enable(String name) {
        if (!isDisabled(name)) {
            return;
        }
        for (int i = 0; i < cc.settings.disabledMods.length; i++) {
            if (cc.settings.disabledMods[i].equals(name)) {
                cc.settings.disabledMods[i] = null;
            }
        }

    }

    public void disable(String name) {
        if (isDisabled(name)) {
            return;
        }
        String[] newarr = new String[cc.settings.disabledMods.length + 1];
        System.arraycopy(cc.settings.disabledMods, 0, newarr, 0, cc.settings.disabledMods.length);
        newarr[newarr.length - 1] = name;
        cc.settings.disabledMods = newarr;
    }
}