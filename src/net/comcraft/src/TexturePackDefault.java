
package net.comcraft.src;

public class TexturePackDefault extends TexturePack2 {

    public TexturePackDefault() {
        super(null);
    }
    
    protected void initTexturePack() {
        setTexturePackName("Default");
        setTexturePackDescription("The default look of Comcraft!");
    }

    public boolean isTexturePackSupported() {
        return true;
    }

    public boolean isThereAnyError() {
        return false;
    }

    public String getErrorMessage() {
        return null;
    }
}
