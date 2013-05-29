package net.comcraft.src;

import java.util.Vector;

public abstract class GuiScreenSlotHost extends GuiScreen {

    protected GuiSlot guiSlot;

    public GuiScreenSlotHost(GuiScreen parentScreen) {
        super(parentScreen);
    }

    protected abstract void initGuiSlotCustom();
    
    protected final void initGui() {
        guiSlot.initSlotScreen(cc);
        
        initGuiSlotCustom();
        
        guiSlot.resetSlot();
    }

    public abstract void onScreenClosed();

    public abstract void elementClicked(int id);

    protected void initTouchOrKeyboard() {
        selectedButton = null;
    }

    public abstract Vector getElementsList();

    protected void handleKeyboardInput() {
        if (!guiSlot.isInputHandle()) {
            if (Keyboard.wasButtonDown(Keyboard.KEY_SOFT_LEFT) || Keyboard.wasButtonDown(Keyboard.KEY_SOFT_RIGHT)) {
                guiSlot.setHandleInput(true);
                selectedButton = null;
            } else if (Keyboard.wasButtonDown(Keyboard.KEY_DOWN) || Keyboard.wasButtonDown(Keyboard.KEY_S)) {
                selectedButton = getNextAvailableButton();
            } else if (Keyboard.wasButtonDown(Keyboard.KEY_UP) || Keyboard.wasButtonDown(Keyboard.KEY_W)) {
                selectedButton = getPreviousAvailableButton();
            } else if (Keyboard.wasButtonDown(Keyboard.KEY_FIRE) || Keyboard.wasButtonDown(Keyboard.KEY_R)) {
                if (selectedButton != null && selectedButton.enabled) {
                    handleGuiAction(selectedButton);
                }
            }
        } else {
            if (Keyboard.wasButtonDown(Keyboard.KEY_FIRE) || Keyboard.wasButtonDown(Keyboard.KEY_SOFT_LEFT) || Keyboard.wasButtonDown(Keyboard.KEY_SOFT_RIGHT)) {
                guiSlot.setHandleInput(false);
                selectedButton = getFirstAvailableButton();
            }
        }
    }

    protected void customHandleInput() {
        guiSlot.handleInput();
    }
}
