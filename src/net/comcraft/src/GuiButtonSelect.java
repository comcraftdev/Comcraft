package net.comcraft.src;

import net.comcraft.client.Comcraft;

public class GuiButtonSelect extends GuiButton {

    private int[] values;
    private String[] names;
    private int selectedValue;

    public GuiButtonSelect(Comcraft cc, int id, int posX, int posY, String displayString, int[] values, String[] names) {
        super(cc, id, posX, posY, displayString);

        this.values = values;
        this.names = names;
    }

    public int getCurrentValue() {
        return values[selectedValue];
    }

    public GuiButtonSelect setCurrentValue(int selectedValue) {
        for (int i = 0; i < values.length; ++i) {
            if (values[i] == selectedValue) {
                this.selectedValue = i;
            }
        }
        
        return this;
    }

    public void nextValue() {
        if (selectedValue < values.length - 1) {
            ++selectedValue;
        } else {
            selectedValue = 0;
        }
    }

    protected String getDisplayString() {
        return displayString + names[selectedValue];
    }
}
