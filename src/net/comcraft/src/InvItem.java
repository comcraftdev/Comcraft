package net.comcraft.src;

public class InvItem {

    public static final InvItem itemsList[] = new InvItem[512];
    public static final InvItem detonator;
    public static final InvItem seeds;
    public static final InvItem hammer;
    /*
     * Item custom
     */
    public final int shiftedIndex;
    protected int maxStackSize;
    protected int iconIndex = -1;
    private String itemName;

    protected InvItem(int id) {
        maxStackSize = 64;
        shiftedIndex = 256 + id;

        itemsList[256 + id] = this;
    }

    protected InvItem(int id, int index) {
        maxStackSize = 64;
        shiftedIndex = 256 + id;
        iconIndex = index;

        itemsList[256 + id] = this;
    }

    public String getItemUsingSound() {
        return null;
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public InvItem setMaxStackSize(int maxSize) {
        maxStackSize = maxSize;
        return this;
    }

    public InvItem setIconCoord(int x, int y) {
        iconIndex = x + y * 16;
        return this;
    }

    public boolean onItemUse(InvItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side) {
        return false;
    }

    public String getItemName() {
        return itemName;
    }
    
    static {
        detonator = new InvItem(1, 256);
        seeds = new InvItem(2, 257);
        hammer = new InvItemHammer(3, 258);
    }
}
