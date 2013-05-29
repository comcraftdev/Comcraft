package net.comcraft.src;

import javax.microedition.lcdui.Graphics;

public class GuiElement {

    public static void drawStringWithShadow(Graphics g, String string, int x, int y, int anchor) {
        int rColor = g.getRedComponent();
        int gColor = g.getGreenComponent();
        int bColor = g.getBlueComponent();

        g.setColor(0, 0, 0);
        g.drawString(string, x + 1, y + 1, anchor);

        g.setColor(rColor, gColor, bColor);
        g.drawString(string, x, y, anchor);
    }

    public static void drawStringInLines(Graphics g, String string, int x, int y, int width, int anchor) {
        for (int i = 0; string.length() > 0; ++i) {
            String tempString = "";
            
            while (g.getFont().stringWidth(tempString) < width && string.length() > 0) {
                tempString += string.charAt(0);
                string = string.substring(1);
            }
            
            drawStringWithShadow(g, tempString, x, y + (g.getFont().getHeight() + 3) * i, anchor);
        }
    }
    
}
