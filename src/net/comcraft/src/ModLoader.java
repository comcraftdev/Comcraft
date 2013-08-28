package net.comcraft.src;
import java.util.Vector;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.comcraft.client.Comcraft;

public class ModLoader {
    private float version=0.1f;
    private Comcraft cc;
    private Vector Mods;
    private boolean hasInitialized=false;
    public ModLoader(Comcraft cc) {
        this.cc=cc;
        Mods=new Vector();
        System.out.println("Comcraft ModLoader "+version+" Initialized");
    }
    public void initMods(){
        try {
            if (!cc.settings.getComcraftFileSystem().isAvailable()) {
                System.out.println("files not initialized");
                return;
            }
            System.out.println("scanning mods folder");
            FileConnection fileConnection = (FileConnection) Connector.open(
                    cc.settings.getComcraftFileSystem()
                            .getPathToFolder("mods/"), Connector.READ);
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
                    Mods.addElement(new Mod(ModName, ModDescription, dis));
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
}