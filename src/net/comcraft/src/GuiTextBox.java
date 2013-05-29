/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;

/**
 *
 * @author Piotr Wójcik
 */
public class GuiTextBox extends GuiScreen implements CommandListener {

    private GuiTextBoxHost guiTextBoxHost;
    private Command exitCommand;
    private Command okCommand;
    private String textBoxString;
    private TextBox textBox;
    private int textFieldType;
    private int maxLength;
    
    public GuiTextBox(GuiTextBoxHost parentScreen, String string, int textFieldType, int maxLength) {
        super((GuiScreen) parentScreen);

        this.guiTextBoxHost = parentScreen;
        this.textBoxString = string;
        this.textFieldType = textFieldType;
        this.maxLength = maxLength;
    }

    protected void customDrawScreen() {
    }

    protected void initGui() {
        exitCommand = new Command("Cancel", Command.EXIT, 0);
        okCommand = new Command("OK", Command.OK, 1);

        textBox = new TextBox("Comcraft", textBoxString, maxLength, textFieldType);
        
        textBox.addCommand(okCommand);
        textBox.addCommand(exitCommand);
        
        textBox.setCommandListener(this);
        
        cc.comcraftMIDlet.display.setCurrent(textBox);
    }

    protected void handleGuiAction(GuiButton guiButton) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == okCommand) {
            cc.comcraftMIDlet.display.setCurrent(cc.ccCanvas);
            
            backToParentScreen();
            
            guiTextBoxHost.guiTextBoxAction(textBox.getString());
        } else if (c == exitCommand) {
            cc.comcraftMIDlet.display.setCurrent(cc.ccCanvas);
            
            backToParentScreen();
            
            guiTextBoxHost.guiTextBoxAction(null);
        }
    }
}