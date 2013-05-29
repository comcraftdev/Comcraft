package net.comcraft.src;

import java.util.Vector;
import net.comcraft.client.Comcraft;

public final class GuiSelectTexturepack extends GuiScreenSlotHost {

    private TexturePack selectedTexturePack;

    public GuiSelectTexturepack(GuiScreen parentScreen) {
        super(parentScreen);
        guiSlot = new GuiSlotSelectTexturepack(this);
    }

    protected void initGuiSlotCustom() {
        cc.texturePackList.updateAvailableTexturePacks();
        
        elementsList.addElement(new GuiButtonSmall(cc, 0, 5, cc.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), cc.langBundle.getText("GuiScreen.buttonSelect")).setEnabled(false));
        elementsList.addElement(new GuiButtonSmall(cc, 1, cc.screenWidth - 5 - GuiButtonSmall.getButtonWidth(), cc.screenHeight - 5 - GuiButtonSmall.getButtonHeight(), cc.langBundle.getText("GuiScreen.buttonClose")).setEnabled(parentScreen != null));
    
        elementClicked(0);
    }

    public void onScreenClosed() {
    }

    public void elementClicked(int id) {
        if (id >= getElementsList().size() || id < 0) {
            getButton(0).setEnabled(false);
            selectedTexturePack = null;
            return;
        }

        TexturePack texturePack = (TexturePack) getElementsList().elementAt(id);

        if (checkTexturepack(texturePack)) {
            getButton(0).setEnabled(true);
            selectedTexturePack = texturePack;
        } else {
            getButton(0).setEnabled(false);
            selectedTexturePack = null;
        }
    }

    private boolean checkTexturepack(TexturePack texturePack) {
        return texturePack.isTexturePackSupported();
    }

    public Vector getElementsList() {
        return cc.texturePackList.getTexturepacksList();
    }

    protected void customDrawScreen() {
        guiSlot.drawScreen();
        
        drawTitle(cc.langBundle.getText("GuiSelectTexturepack.title") + " (" + Comcraft.getScreenWidth() + "x" + Comcraft.getScreenHeight() + ")");
    }

    private void loadTexturePack(TexturePack selectedTexturePack) {
        GuiScreen guiScreen = new GuiLoadingScreen();
        guiScreen.initGuiScreen(cc);
        guiScreen.drawScreen();
        
        cc.flushGraphics();
        
        cc.texturePackList.setTexturePack(selectedTexturePack);
    }
    
    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.id == 0) {
            loadTexturePack(selectedTexturePack);
            backToParentScreen();
        } else if (guiButton.id == 1) {
            backToParentScreen();
        }
    }
}
