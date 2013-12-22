package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

public abstract class GuiSlot extends GuiElement {

    protected GuiScreenSlotHost guiScreenSlotHost;
    protected int slotScreenWidth;
    protected int slotScreenHeight;
    protected int slotWidth;
    protected int slotHeight;
    protected int xPos;
    protected int yPos;
    protected Comcraft cc;
    protected int amountScrolled;
    protected int selectedElement;
    private boolean handleInput;

    public GuiSlot(GuiScreenSlotHost guiScreenSlotHost) {
        this.guiScreenSlotHost = guiScreenSlotHost;
        cc = null;
        handleInput = true;
    }

    public final void resetSlot() {
        if (Touch.isTouchSupported()) {
            selectedElement = -1;
        } else {
            selectedElement = 0;
        }

        guiScreenSlotHost.elementClicked(selectedElement);

        if (getSize() * slotHeight >= slotScreenHeight) {
            amountScrolled = -5;
        } else {
            amountScrolled = -(slotScreenHeight / 2 - getSize() * slotHeight / 2);
        }
    }

    public int getSlotEndPosY() {
        return yPos + slotScreenHeight;
    }

    public void initSlotScreen(Comcraft cc) {
        this.cc = cc;
        initSlotSize();
        initSlot();
    }

    protected abstract void initSlot();

    protected abstract void initSlotSize();

    public void setHandleInput(boolean flag) {
        handleInput = flag;
    }

    public boolean isInputHandle() {
        return handleInput;
    }

    private void drawBackground() {
        guiScreenSlotHost.drawDarkBackground();
    }

    protected void drawForeground() {
        Image backgroundImage = cc.textureProvider.getImage("gui/background.png");

        int cols = Comcraft.screenWidth / backgroundImage.getWidth() + 1;

        for (int i = 0; i < cols; ++i) {
            cc.g.drawImage(backgroundImage, i * backgroundImage.getWidth(), yPos, Graphics.BOTTOM | Graphics.LEFT);
        }

        int yStart = slotScreenHeight + yPos;
        int rows = (Comcraft.screenHeight - yStart) / backgroundImage.getHeight() + 1;

        for (int y = 0; y < rows; ++y) {
            for (int x = 0; x < cols; ++x) {
                cc.g.drawImage(backgroundImage, x * backgroundImage.getWidth(), yStart + y * backgroundImage.getHeight(), Graphics.TOP | Graphics.LEFT);
            }
        }
    }

    protected abstract void drawSlot(int i);

    private void elementClicked(int i) {
        selectedElement = i;
        guiScreenSlotHost.elementClicked(i);
    }

    public void drawScreen() {
        drawBackground();

        for (int i = 0; i < getSize(); ++i) {
            drawSlot(i);
        }

        if (getSize() * slotHeight > slotScreenHeight) {
            drawScrollBar();
        }
        drawForeground();
    }

    private void drawScrollBar() {
        int scrollBarHeight = slotScreenHeight / getSize();
        float floatAmountScrolled = (float) (amountScrolled) / (getSize() * slotHeight - slotScreenHeight);
        int y = (int) (yPos + slotScreenHeight * floatAmountScrolled);

        if (y < yPos) {
            y = yPos;
        } else if (y > yPos + slotScreenHeight - scrollBarHeight) {
            y = yPos + slotScreenHeight - scrollBarHeight;
        }

        cc.g.setColor(215, 215, 215);
        cc.g.fillRect(slotScreenWidth - 10, y, 5, scrollBarHeight);
    }

    protected int getClickedElementId(int y) {
        int yFx = y - yPos + amountScrolled;

        float id = (float) yFx / slotHeight;

        if (id < 0) {
            return -1;
        }

        return (int) id;
    }

    private void centerScreenAtSlot(int id) {
        if (id < 0 || id >= getSize()) {
            return;
        }

        int y = id * slotHeight - amountScrolled + yPos;

        if (y <= yPos) {
            amountScrolled -= slotHeight + 15;
        } else if (y + slotHeight >= yPos + slotScreenHeight) {
            amountScrolled += slotHeight + 15;
        }
    }

    protected void handleTouchInput() {
        if (isMousePressedAtScreen(Touch.getX(), Touch.getY())) {
            if (Touch.isPressed() && Touch.wasUnpressed()) {
                elementClicked(getClickedElementId(Touch.getY()));
            }
            if (Touch.isDragged()) {
                scrollSlot(Touch.getTouchYDifference());
            }
        }
    }

    protected void handleKeyboardInput() {
        if (Keyboard.wasButtonDown(Keyboard.KEY_DOWN)) {
            selectedElement = getCorrectSelectedElementId(selectedElement + 1);
            centerScreenAtSlot(selectedElement);
            elementClicked(selectedElement);
        }
        if (Keyboard.wasButtonDown(Keyboard.KEY_UP)) {
            selectedElement = getCorrectSelectedElementId(selectedElement - 1);
            centerScreenAtSlot(selectedElement);
            elementClicked(selectedElement);
        }
    }

    private int getCorrectSelectedElementId(int id) {
        if (id >= getSize()) {
            return getSize() - 1;
        } else if (id < 0) {
            return 0;
        } else {
            return id;
        }
    }

    public void handleInput() {
        if (handleInput) {
            handleKeyboardInput();
        }
        handleTouchInput();
    }

    protected int getSize() {
        return guiScreenSlotHost.getElementsList().size();
    }

    protected void drawSelectedSlotFrame(int y) {
        cc.g.setColor(0, 0, 0);
        cc.g.fillRect((Comcraft.screenWidth - slotWidth) / 2, y, slotWidth, slotHeight);

        cc.g.setColor(128, 128, 128);

        for (int i = 0; i < 3; ++i) {
            cc.g.drawRect((Comcraft.screenWidth - slotWidth) / 2 - i, y - i, slotWidth + i * 2, slotHeight + i * 2);
        }
    }

    private void scrollSlot(int i) {
        if (getSize() * slotHeight <= slotScreenHeight) {
            return;
        }

        amountScrolled += i;

        if (amountScrolled < -15) {
            amountScrolled = -15;
        } else if (amountScrolled > getSize() * slotHeight + 15 - slotScreenHeight) {
            amountScrolled = getSize() * slotHeight + 15 - slotScreenHeight;
        }
    }

    protected boolean isMousePressedAtScreen(int x, int y) {
        return x >= xPos && y >= yPos && x <= xPos + slotScreenWidth && y <= yPos + slotScreenHeight;
    }

    protected boolean isSlotVisibile(int y) {
        return y + slotHeight >= yPos && y <= yPos + slotScreenHeight;
    }

    protected int getSlotY(int i) {
        return i * slotHeight - amountScrolled + yPos;
    }
}
