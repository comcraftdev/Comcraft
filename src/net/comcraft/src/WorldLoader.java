package net.comcraft.src;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.comcraft.client.Comcraft;

public final class WorldLoader {

    private final Comcraft cc;
    private Vector worldList;
    private String savesPath;

    public WorldLoader(Comcraft cc) {
        this.cc = cc;
        worldList = new Vector();
    }

    public SaveInfo getSaveLoader(String worldName) {
        return new SaveInfo(savesPath + worldName + "/");
    }

    public void updateAvailableWorldList() {
        try {
            savesPath = cc.settings.getComcraftFileSystem().getPathToFolder("saves/");

            worldList.removeAllElements();

            if (!cc.settings.getComcraftFileSystem().isAvailable()) {
                return;
            }

            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("saves/"), Connector.READ);

            Vector elements = FileSystemHelper.getElementsList(fileConnection);

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);

                if (elementName.endsWith("/")) {
                    worldList.addElement(new WorldSaveType(elementName));
                }
            }

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public Vector getWorldList() {
        return worldList;
    }

    public void deleteWorld(WorldSaveType worldInfo) {
        try {
            FileConnection fileConnection1 = (FileConnection) Connector.open(worldInfo.getWorldPath() + "level.info", Connector.READ_WRITE);
            if (fileConnection1.exists()) {
                fileConnection1.delete();
            }
            fileConnection1.close();

            FileConnection fileConnection2 = (FileConnection) Connector.open(worldInfo.getWorldPath() + "world.data", Connector.READ_WRITE);
            if (fileConnection2.exists()) {
                fileConnection2.delete();
            }
            fileConnection2.close();

            FileConnection fileConnection3 = (FileConnection) Connector.open(worldInfo.getWorldPath(), Connector.READ_WRITE);
            if (fileConnection3.exists()) {
                fileConnection3.delete();
            }
            fileConnection3.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }
}
