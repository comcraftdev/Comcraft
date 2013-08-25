package net.comcraft.src;

import java.util.Vector;

import net.comcraft.client.Comcraft;

public abstract class GuiScreenSlotHost extends GuiScreen {

    protected GuiSlot guiSlot;
    private int button_index =0 ;

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
    protected void addButton(String text, boolean enabled, int row, int col) {
        row++;
        int bwidth=GuiButtonSmall.getButtonWidth();
        int bheight=GuiButtonSmall.getButtonHeight();
        int xpos;
        int ypos = Comcraft.screenHeight - (5*row) - (bheight*row);
        if (col==0) {
            xpos=5; // 5 px from the left edge
        }
        else {
            xpos=Comcraft.screenWidth - 5 - bwidth; // 5 px from the right edge
        }
        elementsList.addElement(new GuiButtonSmall(cc, button_index, xpos, ypos, text).setEnabled(enabled));
        button_index++;
    }
}
