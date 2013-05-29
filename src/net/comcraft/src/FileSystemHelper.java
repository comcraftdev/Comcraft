package net.comcraft.src;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.file.FileConnection;

public final class FileSystemHelper {

    private FileSystemHelper() {
    }

    public static Vector getElementsList(FileConnection fileConnection) {
        try {
            Enumeration enumeration = fileConnection.list();

            Vector elements = new Vector();

            while (enumeration.hasMoreElements()) {
                String elementPath = fileConnection.getURL() + (String) enumeration.nextElement();
                elements.addElement(elementPath);
            }

            return elements;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public static Vector getElementsList(Enumeration enumeration) {
        Vector elements = new Vector();

        while (enumeration.hasMoreElements()) {
            String elementPath = "file:///" + (String) enumeration.nextElement();
            elements.addElement(elementPath);
        }

        return elements;
    }

    public static String getLastPathName(String path) {
        if (isDirectory(path)) {
            path = path.substring(0, path.length() - 1);
        }

        String pathName = "";

        for (int i = path.length(); !pathName.startsWith("/") && i >= 0; --i) {
            pathName = path.substring(i);
        }

        pathName = pathName.substring(1);

        return pathName;
    }

    public static String getPathWithoutPrefix(String path) {
        return path.substring(8);
    }

    public static boolean isDirectory(String path) {
        return path.endsWith("/");
    }
}
