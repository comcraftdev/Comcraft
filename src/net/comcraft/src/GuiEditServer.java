package net.comcraft.src;

import javax.microedition.lcdui.TextField;

import net.comcraft.client.Comcraft;

public class GuiEditServer extends GuiScreen implements GuiTextBoxHost {

    private String name;
    private String ip;
    private GuiButton textbutton = null;
    private boolean isNew;

    public GuiEditServer(GuiSelectServer parentScreen, String name, String ip, boolean isNew) {
        super(parentScreen);
        this.name = name;
        this.ip = ip;
        this.isNew = isNew;
    }

    protected void customDrawScreen() {
    }

    protected void initGui() {
        name = (name != null) ? name : cc.langBundle.getText("GuiEditServer.defaultName");
        int centerX = (Comcraft.screenWidth - GuiButton.getButtonWidth()) / 2;
        int startY = 30;
        int mul = (GuiButton.getButtonHeight() + GuiButton.getButtonHeight() / 6);

        elementsList.addElement(new GuiButton(cc, 0, centerX, startY, cc.langBundle.getText("GuiEditServer.buttonSave")));
        elementsList.addElement(new GuiButton(cc, 1, centerX, startY + mul * 2, name));
        elementsList.addElement(new GuiButton(cc, 2, centerX, startY + mul * 3, ip != null ? ip : ("<" + cc.langBundle.getText("GuiEditServer.ipaddr") + ">")));
        elementsList.addElement(new GuiButton(cc, 3, centerX, startY + mul * 5, cc.langBundle.getText("GuiNewWorld.buttonClose")));
    }

    protected void handleGuiAction(GuiButton guiButton) {
        if (!guiButton.enabled) {
            return;
        }

        if (guiButton.getId() == 0) {
            if (!name.equals("") && ip != null) {
                if (isNew) {
                    // Add Server
                } else {
                    // Edit Server
                }
                backToParentScreen();
            }
        } else if (guiButton.getId() == 1) {
            textbutton = guiButton;
            cc.displayGuiScreen(new GuiTextBox(this, name, TextField.ANY, 64));
        } else if (guiButton.getId() == 2) {
            textbutton = guiButton;
            cc.displayGuiScreen(new GuiTextBox(this, ip, TextField.ANY, 256));
        } else if (guiButton.getId() == 3) {
            backToParentScreen();
        }
    }

    public void guiTextBoxAction(String textBoxString) {
        if (textBoxString == null) {
            return;
        }
        textbutton.displayString = textBoxString;
        if (textbutton.getId() == 1) {
            name = textBoxString;
        } else if (textbutton.getId() == 2) {
            ip = textBoxString;
        }
    }

}
