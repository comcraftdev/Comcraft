
package net.comcraft.src;

public final class WorldSaveType {

    private String path;
    
    public WorldSaveType(String path) {
        this.path = path;
    }
    
    public String getWorldPath() {
        return path;
    }
    
    public String getWorldName() {
        return FileSystemHelper.getLastPathName(path);
    }
}
