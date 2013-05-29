package net.comcraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.comcraft.client.Comcraft;

public class TexturePackList {

    private static final TexturePack texturepackDefault = new TexturePackDefault();
    private TexturePack selectedTexturepack;
    private final Comcraft cc;
    private Vector texturepacksList;

    public TexturePackList(Comcraft cc) {
        this.cc = cc;
        texturepacksList = new Vector();
        selectedTexturepack = texturepackDefault;
    }

    public InputStream getResourceAsStream(String resourcePath) {
        return selectedTexturepack.getResourceAsStream(resourcePath);
    }

    public final boolean setTexturePack(TexturePack newTexturePack) {
        if (selectedTexturepack.getTexturePackName().equals(newTexturePack.getTexturePackName())) {
            return false;
        } else {
            selectedTexturepack = newTexturePack;
            cc.textureProvider.reloadTextures();
            cc.render.reloadRender();
            cc.settings.texturepack = selectedTexturepack.getTexturePackName();
            cc.settings.saveOptions();
            return true;
        }
    }

    public void updateAvailableTexturePacks() {
        try {
            texturepacksList.removeAllElements();
            texturepacksList.addElement(texturepackDefault);

            if (!cc.settings.getComcraftFileSystem().isAvailable()) {
                return;
            }

            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("texturepacks/"), Connector.READ);

            Vector elements = FileSystemHelper.getElementsList(fileConnection);

            for (int i = 0; i < elements.size(); ++i) {
                String elementName = (String) elements.elementAt(i);

                if (elementName.endsWith("/") || elementName.endsWith(".zip") || elementName.endsWith(".rar")) {
                    TexturePack texturePack = new TexturePackCustom(cc, elementName);

                    if (!texturePack.getTexturePackName().equals("Default")) {
                        texturepacksList.addElement(texturePack);

                        if (cc.settings.texturepack.equals(texturePack.getTexturePackName())) {
                            setTexturePack(texturePack);
                        }
                    }
                }
            }

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public Vector getTexturepacksList() {
        return texturepacksList;
    }
}
