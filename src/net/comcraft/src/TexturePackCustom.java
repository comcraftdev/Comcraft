package net.comcraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.comcraft.client.Comcraft;

public class TexturePackCustom extends TexturePack2 {

    private boolean isThereAnyError;
    private String errorMessage;

    public TexturePackCustom(Comcraft cc, String path) {
        super(cc, path);
    }

    public InputStream getResourceAsStream(String path) {
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(this.path + path, Connector.READ);

            if (!fileConnection.exists()) {
                return super.getResourceAsStream(path);
            }

            InputStream inputStream = fileConnection.openInputStream();

            fileConnection.close();

            return inputStream;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    protected void initTexturePack() {
        try {
            setTexturePackName(FileSystemHelper.getLastPathName(path));

            if (path.endsWith(".zip") || path.endsWith(".rar")) {
                isThereAnyError = true;
                errorMessage = cc.langBundle.getText("GuiSlotSelectTexturepack.unpackTexturepack");

                return;
            }

            FileConnection fileConnection = (FileConnection) Connector.open(path + "info.txt", Connector.READ);

            if (!fileConnection.exists()) {
                isThereAnyError = true;
                errorMessage = cc.langBundle.getText("GuiSlotSelectTexturepack.infoNotFound");

                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileConnection.openInputStream()));

            setTexturePackDescription(bufferedReader.readLine());

            String widthLine = bufferedReader.readLine();
            String heightLine = bufferedReader.readLine();

            bufferedReader.close();

            if (widthLine == null || heightLine == null) {
                return;
            }

            int width = 0;
            int height = 0;

            try {
                width = Integer.parseInt(widthLine.trim());
                height = Integer.parseInt(heightLine.trim());
            } catch (Exception ex) {
                //#debug
//#                 ex.printStackTrace();
            }

            setResolution(width, height);

            fileConnection.close();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public boolean isThereAnyError() {
        return isThereAnyError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
