package net.comcraft.src;
import javax.microedition.lcdui.Graphics;
import net.comcraft.client.Comcraft;
public class GuiModInfo extends GuiScreen {
    private String text;
    public GuiModInfo(GuiScreen parentScreen, String text) {
        super(parentScreen);
        this.text = text;
    }
    protected void customDrawScreen() {
        cc.g.setColor(255, 255, 255);
        drawStringInLines(cc.g, text, 10, 10, Comcraft.screenWidth - 10 * 2,
                Graphics.TOP | Graphics.LEFT);
    }
    protected void initGui() {
        elementsList.addElement(new GuiButtonSmall(cc, 0, 3,
                Comcraft.screenHeight - 3 - GuiButtonSmall.getButtonHeight(),
                "Back"));
    }
    protected void handleGuiAction(GuiButton guiButton) {
        backToParentScreen();
    }
}
