
package net.comcraft.src;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.comcraft.client.Comcraft;

public class LoadingScreen {

    private Comcraft cc;
    private String displayString;
    private float progress;
    
    public LoadingScreen(Comcraft cc) {
        this.cc = cc;
        displayString = "";
        progress = 0f;
    }
    
    public void displayLoadingScreen(String text) {
        displayString = text;
        progress = 0f;
        drawScreen();
    }
    
    public void setProgress(float progress) {
        this.progress = progress;
        drawScreen();
    }
    
    private void drawScreen() {
        clearScreen();
        drawBackground();
        
        cc.g.setColor(0xffffff);
        GuiElement.drawStringWithShadow(cc.g, displayString + "..." + (int) (progress * 100) + "%", Comcraft.screenWidth / 2, (int) (Comcraft.screenHeight * 0.25f), Graphics.TOP | Graphics.HCENTER);
        drawProgressBar();
        
        cc.flushGraphics();
    }
    
    private void drawProgressBar() {
        int barWidth = Comcraft.screenWidth - 100;
        int barHeight = barWidth / 10;
        
        cc.g.setColor(0x696969);
        cc.g.drawRect((Comcraft.screenWidth - barWidth) / 2 - 1, (int) (Comcraft.screenHeight * 0.75f) - 1, barWidth + 2, barHeight + 2);
        cc.g.drawRect((Comcraft.screenWidth - barWidth) / 2 - 2, (int) (Comcraft.screenHeight * 0.75f) - 2, barWidth + 4, barHeight + 4);
        
        cc.g.setColor(0xffffff);
        cc.g.fillRect((Comcraft.screenWidth - barWidth) / 2 + 1, (int) (Comcraft.screenHeight * 0.75f) + 1, (int) ((barWidth - 1) * progress), barHeight - 1);
    }
    
    private void clearScreen() {
        cc.g.setColor(0x000000);
        cc.g.fillRect(0, 0, Comcraft.screenWidth, Comcraft.screenHeight);
    }
    
    private void drawBackground() {
        Image backgroundImage = cc.textureProvider.getImage("gui/background.png");

        int rows = Comcraft.screenWidth / backgroundImage.getWidth() + 1;
        int cols = Comcraft.screenHeight / backgroundImage.getHeight() + 1;

        for (int y = 0; y < cols; ++y) {
            for (int x = 0; x < rows; ++x) {
                cc.g.drawImage(backgroundImage, x * backgroundImage.getWidth(), y * backgroundImage.getHeight(), Graphics.TOP | Graphics.LEFT);
            }
        }
    }
    
}
