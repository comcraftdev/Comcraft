
package net.comcraft.src;

public class InventoryPlayer {

    private int selectedElement;
    private InvItemStack[] elementsList;
    
    public InventoryPlayer() {
        selectedElement = 0;
        elementsList = new InvItemStack[3];
        elementsList[0] = new InvItemStack(1, 1);
        elementsList[1] = new InvItemStack(2, 1);
        elementsList[2] = new InvItemStack(3, 1);
    }
    
    public int getSelectedElementNum() {
        return selectedElement;
    }
    
    public InvItemStack getSelectedItemStack() {
        return elementsList[selectedElement];
    }
    
    public void setSelectedElement(int element) {
        selectedElement = element;
    }
    
    public InvItemStack getItemStackAt(int index) {
        if (index < 0 || index >= elementsList.length) {
            return null;
        }
        
        return elementsList[index];
    }
    
    public void setItemStackAt(int index, InvItemStack itemStack) {
        if (index < 0 || index >= elementsList.length) {
            return;
        }
        
        elementsList[index] = itemStack;
    }
    
    public int getFastSlotSize() {
        return 3;
    }
    
}
