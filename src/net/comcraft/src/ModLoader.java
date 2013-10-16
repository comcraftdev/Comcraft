package net.comcraft.src;

import java.util.Hashtable;
import java.util.Vector;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.google.minijoe.sys.JsFunction;
import com.java4ever.apime.io.GZIP;

import net.comcraft.client.Comcraft;

public class ModLoader {
    public static final int API_VERSION = 4;
    public static final int MIN_API_VERSION = 2;
    private static final int PACKAGE = 0x10;
    private static final int MOD_DESCRIPTOR = 0x20;
    private static final int RESOURCE = 0x30;
    public static final String version = "0.4";
    private Comcraft cc;
    private Vector Mods;
    private boolean hasInitialized = false;
    private BaseMod global;
    private Hashtable resourcedata;

    public ModLoader(Comcraft cc) {
        this.cc = cc;
        Mods = new Vector();
        System.out.println("Comcraft ModLoader " + version + " Initialized");
    }

    public void initMods() {
        resourcedata = new Hashtable();
        if (!cc.settings.getComcraftFileSystem().isAvailable()) {
            System.out.println("files not initialized");
            return;
        }
        System.out.println("scanning mods folder");
        Vector elements;
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("mods/"), Connector.READ);
            if (!fileConnection.exists()) {
                fileConnection.mkdir();
            }
            hasInitialized = true;
            elements = FileSystemHelper.getElementsList(fileConnection);
            fileConnection.close();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            return;
        }
        global = new BaseMod();
        for (int i = 0; i < elements.size(); ++i) {
            String elementName = (String) elements.elementAt(i);
            if (elementName.endsWith("/")) {
                continue;
            }
            String modFileName = elementName.substring(elementName.lastIndexOf(0x2F) + 1, elementName.length());
            byte[] RawData = null;
            Object[] info = new Object[4];
            try {
                FileConnection GZModFile = open(elementName);
                byte[] GZData = new byte[(int) GZModFile.fileSize()];
                if (GZData.length < 20) {
                    throw new IOException("Not enough bytes in mod file");
                }
                GZModFile.openInputStream().read(GZData, 0, GZData.length);
                GZModFile.close();
                RawData = GZIP.inflate(GZData);
            } catch (IOException modEx) {
                modEx.printStackTrace();
                info = new Object[] { modFileName, "", modEx.getMessage(), new Boolean(false) };
            }
            if (RawData != null) {
                try {
                    info = ReadModFile(new DataInputStream(new ByteArrayInputStream(RawData)));
                } catch (IOException e) {
                    info = new Object[] { modFileName, "", e.getMessage(), new Boolean(false) };
                }
            }
            Mods.addElement(new Mod(this, (String) info[0], (String) info[1], (String) info[2], ((Boolean) info[3]).booleanValue()));
        }

    }

    private Object[] ReadModFile(DataInputStream dis) throws IOException {
        byte[] b = new byte[4];
        dis.read(b);
        if (!new String(b).equals("CCML")) {
            throw new IOException("Malformed Mod File");
        }
        int version = dis.read();
        if (version > API_VERSION) {
            throw new IOException("Mod built for a later version of comcraft");
        }
        if (version < MIN_API_VERSION) {
            throw new IOException("Mod built for an earlier version of comcraft");
        }
        String main = null;
        Object[] info = new Object[4];
        info[2] = "No Mod Info";
        info[3] = new Boolean(false);
        int flags = dis.read();
        while (dis.available() > 0) {
            int opt = dis.read();
            switch (opt) {
            case MOD_DESCRIPTOR:
                info[0] = dis.readUTF();
                info[1] = dis.readUTF();
                main = dis.readUTF();
                if (version >= 4) { // does anyone prefer if(version > 3) ?
                    info[2] = dis.readUTF();
                }
                break;
            case RESOURCE:
                int l = dis.read();
                for (int i = 0; i < l; i++) {
                    String resname = dis.readUTF();
                    if (version < 4) {
                        resname = resname.substring("/res".length()).replace('\\', '/');
                    }
                    String content = dis.readUTF();
                    if (resourcedata.containsKey(resname)) {
                        // Do something here maybe
                    } else {
                        resourcedata.put(resname, content);
                    }
                }
                break;
            case PACKAGE:
                l = dis.read();
                for (int i = 0; i < l; i++) {
                    String packageName = dis.readUTF();
                    int flen = dis.read();
                    for (int x = 0; x < flen; x++) {
                        String filename = dis.readUTF();
                        if (version >= 4 && (flags & 1) == 1) {
                            dis.readUTF(); // Skip past source code
                        }
                        int length = dis.readInt();
                        if ((packageName + "." + filename).equals(main)) {
                            try {
                                JsFunction.exec(dis, global);
                                info[3] = new Boolean(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                                info[2] = e.getMessage() + "\n\nIn file " + packageName + "." + filename;
                            }
                        } else {
                            dis.read(new byte[length]);
                            // Currently only the main class is executed
                        }
                    }
                }
                break;
            case -1:
                break;
            default:
                System.out.println("<Unknown Data " + opt + ">");
                break;
            }
        }
        dis.close();
        return info;
    }

    public InputStream getResourceAsStream(String filename) {
        // Emulates standard Class.getResourceAsStream
        String content = (String) resourcedata.get(filename);
        if (content == null) {
            return Class.class.getResourceAsStream(filename);
        }
        return new ByteArrayInputStream(content.getBytes());
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