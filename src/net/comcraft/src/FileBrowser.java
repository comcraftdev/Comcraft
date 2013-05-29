package net.comcraft.src;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public final class FileBrowser {

    private String currentPath;
    private Vector currentElements;

    public FileBrowser() {
        currentElements = new Vector();
    }

    public void openDirectory(String path) {
        if (!FileSystemHelper.isDirectory(path)) {
            return;
        }

        if (path.equals("file:///")) {
            openRoot();
        } else {
            openFolder(path);
        }
        
        currentPath = path;
    }

    private void openRoot() {
        currentElements = FileSystemHelper.getElementsList(FileSystemRegistry.listRoots());
    }

    private void openFolder(String path) {
        try {
            FileConnection fileConnection = (FileConnection) Connector.open(path, Connector.READ);

            if (fileConnection.exists()) {
                currentElements = FileSystemHelper.getElementsList(fileConnection);
            }
            
            fileConnection.close();
        } catch (IOException ex) {
            //#debug debug
//#             ex.printStackTrace();
            throw new ComcraftException("IOException while entering selected directory!", ex);
        } catch (SecurityException ex) {
            //#debug debug
//#             ex.printStackTrace();
        }
    }

    public void openPreviousFolder() {
        String path = currentPath;

        if (path.equals("file:///")) {
            return;
        }

        path = path.substring(0, path.length() - 1);

        while (!FileSystemHelper.isDirectory(path)) {
            path = path.substring(0, path.length() - 1);
        }

        openDirectory(path);
    }
    
    public boolean canWriteCurrentDirectory() {
        return !currentPath.equals("file:///");
    }
    
    public String getCurrentPath() {
        return currentPath;
    }
    
    public Vector getCurrentElements() {
        return currentElements;
    }
}
