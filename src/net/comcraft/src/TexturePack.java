package net.comcraft.src;

import java.io.InputStream;

public interface TexturePack {
    
    public InputStream getResourceAsStream(String resourceDir);
    
    public String getTexturePackName();
    
    public String getTexturePackDescription();
    
    public boolean isTexturePackSupported();
    
    public boolean isThereAnyError();
    
    public String getErrorMessage();
    
}
