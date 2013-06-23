package net.comcraft.src;
import java.util.Vector;
import java.io.IOException;
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
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("mods/"), Connector.READ);
            if (!fileConnection.exists()){
                System.out.println("missing folder '/mods'");
                return;
            }
            hasInitialized=true;

            Vector elements = FileSystemHelper.getElementsList(fileConnection);

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);
                System.out.println(elementName);
                if (elementName.endsWith("/")) {
                    Mod mod = new Mod(elementName);
                    Mods.addElement(mod);
                }
            }

            fileConnection.close();
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
}