package net.comcraft.src;

public final class InvItemStack {

    public int stackSize;
    public int itemID;
    private int itemDamage;

    public InvItemStack(int itemId, int stackSize) {
        this.itemID = itemId;
        this.stackSize = stackSize;
    }
    
    public InvItemStack(int itemID) {
        this(itemID, 1);
    }

    public InvItemStack(Block block) {
        this(block.blockID, 1);
    }

    public InvItem getItem() {
        return InvItem.itemsList[itemID];
    }

    public int getItemDamage() {
        return itemDamage;
    }

    public void setItemDamage(int value) {
        itemDamage = value;
    }

    public boolean useItem(EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        boolean flag = getItem().onItemUse(this, entityPlayer, world, x, y, z, side);
        return flag;
    }
    
    public boolean equals(Object object) {
        if (object instanceof InvItemStack) {
            InvItemStack itemStack = (InvItemStack) object;
            
            return itemStack.itemID == itemID;
        } else {
            return super.equals(object);
        }
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.itemID;
        return hash;
    }
}
