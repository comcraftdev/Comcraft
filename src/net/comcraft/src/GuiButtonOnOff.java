
package net.comcraft.src;

import net.comcraft.client.Comcraft;

public class GuiButtonOnOff extends GuiButton {
    
    private boolean value;
    
    public GuiButtonOnOff(Comcraft cc, int id, int posX, int posY, String displayString) {
        super(cc, id, posX, posY, displayString);
        value = false;
    }

    protected String getDisplayString() {
        return displayString + (value ? cc.langBundle.getText("ButtonOnOff.on") : cc.langBundle.getText("ButtonOnOff.off"));
    }
    
    public GuiButtonOnOff setValue(boolean value) {
        this.value = value;
        return this;
    }
    
    public boolean getValue() {
        return value;
    }
    
}
