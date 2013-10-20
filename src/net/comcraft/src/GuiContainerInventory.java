/*
 * Copyright (C) 2013 Piotr Wójcik
 * 
 * This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.comcraft.src;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import net.comcraft.client.Comcraft;

public class GuiContainerInventory {

    private Comcraft cc;
    private GuiInventory guiInventory;
    private int rows;
    private int cols;
    private int width;
    private int height;
    private int xPos;
    private int yPos;
    private int amountScrolled;
    private int selectedIndex;
    private Vector itemStackList;
    private Vector lockedItems;

    public GuiContainerInventory(Comcraft cc, GuiInventory guiInventory, int xPos, int yPos) {
        this.cc = cc;
        this.guiInventory = guiInventory;
        amountScrolled = 0;
        this.xPos = xPos;
        this.yPos = yPos;
        itemStackList = new Vector(256);
        lockedItems = new Vector(256);

        if (Touch.isTouchSupported()) {
            selectedIndex = -1;
        } else {
            selectedIndex = 0;
        }

        initItemStackList();
        initGuiContainer();
    }

    public InvItemStack getCurrentItem() {
        return (InvItemStack) itemStackList.elementAt(selectedIndex);
    }

    private void initGuiContainer() {
        if (Touch.isTouchSupported()) {
            if (Comcraft.getScreenWidth() == 240) {
                width = 150;
            } else if (Comcraft.getScreenWidth() == 320) {
                width = 250;
            } else if (Comcraft.getScreenWidth() == 360) {
                width = 250;
            } else if (Comcraft.getScreenWidth() == 176) {
                width = 150;
            } else if (Comcraft.getScreenWidth() == 480) {
                width = 400;
            }

            if (Comcraft.getScreenHeight() == 320) {
                height = 300;
            } else if (Comcraft.getScreenHeight() == 220) {
                height = 150;
            } else if (Comcraft.getScreenHeight() == 240) {
                height = 200;
            } else if (Comcraft.getScreenHeight() == 400) {
                height = 350;
            } else if (Comcraft.getScreenHeight() == 640) {
                height = 600;
            } else if (Comcraft.getScreenHeight() == 480) {
                height = 450;
            } else if (Comcraft.getScreenHeight() == 800) {
                height = 600;
            }
        } else {
            if (Comcraft.getScreenWidth() == 240) {
                width = 200;
            } else if (Comcraft.getScreenWidth() == 176) {
                width = 150;
            } else if (Comcraft.getScreenWidth() == 360) {
                width = 350;
            } else if (Comcraft.getScreenWidth() == 320) {
                width = 300;
            } else if (Comcraft.getScreenWidth() == 480) {
                width = 450;
            }

            if (Comcraft.getScreenHeight() == 320) {
                height = 250;
            } else if (Comcraft.getScreenHeight() == 220) {
                height = 150;
            } else if (Comcraft.getScreenHeight() == 240) {
                height = 150;
            } else if (Comcraft.getScreenHeight() == 400) {
                height = 300;
            } else if (Comcraft.getScreenHeight() == 640) {
                height = 550;
            } else if (Comcraft.getScreenHeight() == 480) {
                height = 400;
            } else if (Comcraft.getScreenHeight() == 800) {
                height = 550;
            }
        }

        if (Touch.isTouchSupported()) {
            cols = height / 50;
        } else {
            cols = width / 50;
        }

        rows = (itemStackList.size() / cols) + 1;

        if (!Touch.isTouchSupported()) {
            checkSelectedElement();
        }
    }

     private void addItem(InvItem item) {
        itemStackList.addElement(new InvItemStack(item.shiftedIndex, 1));
    }
    
    private void addBlock(Block block) {
        itemStackList.addElement(new InvItemStack(block.blockID, 1));
    }

    private void initItemStackList() {
        /*
         * The blocks are added to the inventory here.
         */

        addBlock(Block.getBlock("stone"));
        addBlock(Block.getBlock("grass"));
        addItem(InvItem.hammer);
        addBlock(Block.getBlock("dirt"));
        addBlock(Block.getBlock("sand"));
        addBlock(Block.getBlock("cobblestone"));
        addBlock(Block.getBlock("leaves"));
        addBlock(Block.getBlock("glass"));
        addBlock(Block.getBlock("wood"));
        addBlock(Block.getBlock("wood1"));
        addBlock(Block.getBlock("wood2"));
        addBlock(Block.getBlock("planks"));
        addBlock(Block.getBlock("planks1"));
        addBlock(Block.getBlock("planks2"));
        addBlock(Block.getBlock("planks3"));
        addItem(InvItem.detonator);
        addItem(InvItem.seeds);
        addBlock(Block.getBlock("tnt"));
        addBlock(Block.getBlock("tntWeak"));
        addBlock(Block.getBlock("tntStrong"));
        addBlock(Block.getBlock("torch"));
        addBlock(Block.getBlock("woodenDoor"));
        addBlock(Block.getBlock("ironDoor"));
        addBlock(Block.getBlock("bedrock"));
        addBlock(Block.getBlock("lapisLazuli"));
        addBlock(Block.getBlock("sandStone"));
        addBlock(Block.getBlock("woolWhite"));
        addBlock(Block.getBlock("woolRed"));
        addBlock(Block.getBlock("woolOrange"));
        addBlock(Block.getBlock("woolYellow"));
        addBlock(Block.getBlock("woolLime"));
        addBlock(Block.getBlock("woolGreen"));
        addBlock(Block.getBlock("woolLightBlue"));
        addBlock(Block.getBlock("woolCyan"));
        addBlock(Block.getBlock("woolBlue"));
        addBlock(Block.getBlock("woolMagenta"));
        addBlock(Block.getBlock("woolPink"));
        addBlock(Block.getBlock("woolPurple"));
        addBlock(Block.getBlock("woolBrown"));
        addBlock(Block.getBlock("woolLightGray"));
        addBlock(Block.getBlock("woolGray"));
        addBlock(Block.getBlock("woolBlack"));
        addBlock(Block.getBlock("cactus"));
        addBlock(Block.getBlock("redFlower"));
        addBlock(Block.getBlock("yellowFlower"));
        addBlock(Block.getBlock("treePlant"));
        addBlock(Block.getBlock("mushroom"));
        addBlock(Block.getBlock("toadstool"));
        addBlock(Block.getBlock("wheat"));
        addBlock(Block.getBlock("fencePlank"));
        addBlock(Block.getBlock("fencePlank2"));
        addBlock(Block.getBlock("fencePlank3"));
        addBlock(Block.getBlock("fencePlank4"));
        addBlock(Block.getBlock("fenceStone"));
        addBlock(Block.getBlock("fenceBrick"));
        addBlock(Block.getBlock("fenceNetherbrick"));
        addBlock(Block.getBlock("iron"));
        addBlock(Block.getBlock("gold"));
        addBlock(Block.getBlock("diamond"));
        addBlock(Block.getBlock("doubleStoneSlab"));
        addBlock(Block.getBlock("stoneSlab"));
        addBlock(Block.getBlock("plankSlab"));
        addBlock(Block.getBlock("plankSlab1"));
        addBlock(Block.getBlock("plankSlab2"));
        addBlock(Block.getBlock("plankSlab3"));
        addBlock(Block.getBlock("cobblestoneSlab"));
        addBlock(Block.getBlock("brickSlab"));
        addBlock(Block.getBlock("netherBrickSlab"));
        addBlock(Block.getBlock("whiteWoolSlab"));
        addBlock(Block.getBlock("redWoolSlab"));
        addBlock(Block.getBlock("orangeWoolSlab"));
        addBlock(Block.getBlock("yellowWoolSlab"));
        addBlock(Block.getBlock("lightGreenWoolSlab"));
        addBlock(Block.getBlock("greenWoolSlab"));
        addBlock(Block.getBlock("blueWoolSlab"));
        addBlock(Block.getBlock("pinkWoolSlab"));
        addBlock(Block.getBlock("blackWoolSlab"));
        addBlock(Block.getBlock("craftingTable"));
        addBlock(Block.getBlock("bed"));
        addBlock(Block.getBlock("furnace"));
        addBlock(Block.getBlock("chest"));
        addBlock(Block.getBlock("water"));
        addBlock(Block.getBlock("lava"));
        addBlock(Block.getBlock("stoneTitle"));
        addBlock(Block.getBlock("plankTitle"));
        addBlock(Block.getBlock("plankTitle1"));
        addBlock(Block.getBlock("plankTitle2"));
        addBlock(Block.getBlock("plankTitle3"));
        addBlock(Block.getBlock("cobblestoneTitle"));
        addBlock(Block.getBlock("brickTitle"));
        addBlock(Block.getBlock("netherBrickTitle"));
        addBlock(Block.getBlock("brick"));
        addBlock(Block.getBlock("bookshelve"));
        addBlock(Block.getBlock("mossStone"));
        addBlock(Block.getBlock("obsidian"));
        addBlock(Block.getBlock("ice"));
        addBlock(Block.getBlock("snowBlock"));
        addBlock(Block.getBlock("snow"));
        addBlock(Block.getBlock("pumpkin"));
        addBlock(Block.getBlock("netherrack"));
        addBlock(Block.getBlock("soulSand"));
        addBlock(Block.getBlock("stoneBrick"));
        addBlock(Block.getBlock("netherBrick"));
        addBlock(Block.getBlock("plankStairs"));
        addBlock(Block.getBlock("plankStairs1"));
        addBlock(Block.getBlock("plankStairs2"));
        addBlock(Block.getBlock("plankStairs3"));
        addBlock(Block.getBlock("brickStairs"));
        addBlock(Block.getBlock("netherBrickStairs"));
        addBlock(Block.getBlock("stoneStairs"));
        addBlock(Block.getBlock("cobblestoneStairs"));
        addBlock(Block.getBlock("whiteWoolStairs"));
        addBlock(Block.getBlock("redWoolStairs"));
        addBlock(Block.getBlock("orangeWoolStairs"));
        addBlock(Block.getBlock("yellowWoolStairs"));
        addBlock(Block.getBlock("lightGreenWoolStairs"));
        addBlock(Block.getBlock("greenWoolStairs"));
        addBlock(Block.getBlock("blueWoolStairs"));
        addBlock(Block.getBlock("pinkWoolStairs"));
        addBlock(Block.getBlock("blackWoolStairs"));
        addBlock(Block.getBlock("emoticon"));
        addBlock(Block.getBlock("alphabet1"));
        addBlock(Block.getBlock("alphabet2"));
        addBlock(Block.getBlock("numbers"));
        addBlock(Block.getBlock("animalPig"));
        addBlock(Block.getBlock("animalSheep"));
        addBlock(Block.getBlock("animalCow"));
        addBlock(Block.getBlock("animalChicken"));

        for (int id = 0; id < Block.blocksList.length; ++id) {
            InvItemStack itemStack = new InvItemStack(id, 1);

            if (Block.blocksList[id] != null && !itemStackList.contains(itemStack)) {
                itemStackList.addElement(itemStack);
                //#debug
//#                 System.out.println("(" + this.getClass().getName() + ") unused id: " + id);
            }
        }
    }

    public void drawContainer() {
        cc.g.setClip(xPos, yPos, width, height);

        for (int rowsI = 0; rowsI < rows; ++rowsI) {
            for (int colsI = 0; colsI < cols; ++colsI) {
                int id = colsI + rowsI * cols;

                InvItemStack itemStack = null;

                if (id < itemStackList.size()) {
                    itemStack = (InvItemStack) itemStackList.elementAt(id);
                }

                int x;
                int y;

                if (Touch.isTouchSupported()) {
                    x = xPos + rowsI * 50;
                    y = yPos + colsI * 50;
                } else {
                    x = xPos + colsI * 50;
                    y = yPos + rowsI * 50;
                }

                drawSlot(itemStack, x, y, id);
            }
        }

        cc.g.setClip(0, 0, Comcraft.screenWidth, Comcraft.screenHeight);
    }

    private void drawSlot(InvItemStack itemStack, int x, int y, int id) {
        if (Touch.isTouchSupported()) {
            x += amountScrolled;
        } else {
            y += amountScrolled;
        }

        if (itemStack != null) {
            cc.g.drawImage(cc.textureProvider.getItemTexture(itemStack.getItem().getIconIndex()), x, y, Graphics.TOP | Graphics.LEFT);

            if (lockedItems.contains(new Integer(itemStack.itemID))) {
                cc.g.drawImage(cc.textureProvider.getImage("gui/locked_item.png"), x, y, Graphics.TOP | Graphics.LEFT);
            }
        } else {
            cc.g.setColor(0xffffff);
            cc.g.fillRect(x, y, 50, 50);
        }

        cc.g.drawImage(cc.textureProvider.getImage(("gui/inventory_slot.png")), x, y, Graphics.TOP | Graphics.LEFT);

        if (selectedIndex == id) {
            cc.g.drawImage(cc.textureProvider.getImage(("gui/inventory_slot_selection.png")), x, y, Graphics.TOP | Graphics.LEFT);
        }
    }

    public boolean isPointAtContainer(int x, int y) {
        return x >= xPos && x <= xPos + width && y >= yPos && y <= yPos + height;
    }

    private void elementClicked(int x, int y) {
        x -= xPos;
        y -= yPos;

        x -= amountScrolled;

        int xEl = x / 50;
        int yEl = y / 50;

        int id = yEl + xEl * cols;

        if (id < itemStackList.size()) {
            InvItemStack itemStack = (InvItemStack) itemStackList.elementAt(id);

            if (!lockedItems.contains(new Integer(itemStack.itemID))) {
                guiInventory.clickedItemStack(itemStack);
            }
        }
    }

    private void checkSelectedElement() {
        int sRow = selectedIndex / cols - 1;

        amountScrolled = sRow * -50 + 25;
    }

    private void handleKeyboardInput() {
        if (Keyboard.wasButtonDown(Keyboard.KEY_LEFT) || Keyboard.wasButtonDown(Keyboard.KEY_A)) {
            if (selectedIndex > 0) {
                --selectedIndex;
            } else {
                selectedIndex = itemStackList.size() - 1;
            }
            checkSelectedElement();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_RIGHT) || Keyboard.wasButtonDown(Keyboard.KEY_D)) {
            if (selectedIndex < itemStackList.size() - 1) {
                ++selectedIndex;
            } else {
                selectedIndex = 0;
            }
            checkSelectedElement();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_UP) || Keyboard.wasButtonDown(Keyboard.KEY_W)) {
            selectedIndex -= cols;

            if (selectedIndex < 0) {
                selectedIndex = itemStackList.size() - 1;
            }

            checkSelectedElement();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_DOWN) || Keyboard.wasButtonDown(Keyboard.KEY_S)) {
            selectedIndex += cols;

            if (selectedIndex >= itemStackList.size()) {
                selectedIndex = 0;
            }

            checkSelectedElement();
        } else if (Keyboard.wasButtonDown(Keyboard.KEY_FIRE) || Keyboard.wasButtonDown(Keyboard.KEY_F)) {
            InvItemStack itemStack = (InvItemStack) itemStackList.elementAt(selectedIndex);

            if (!lockedItems.contains(new Integer(itemStack.itemID))) {
                guiInventory.clickedItemStack(itemStack);
            }
        }
    }

    private void scrollContainer(int difference) {
        amountScrolled -= difference;

        if (amountScrolled > 50) {
            amountScrolled = 50;
        }
        if (amountScrolled < ((rows - 1) * -50 + 50)) {
            amountScrolled = ((rows - 1) * -50 + 50);
        }
    }

    private void handleTouchInput() {
        if (isPointAtContainer(Touch.getX(), Touch.getY())) {
            if (!Touch.isPressed() && Touch.wasPressed() && !Touch.wasDragged()) {
                elementClicked(Touch.getX(), Touch.getY());
            } else if (Touch.isDragged()) {
                scrollContainer(Touch.getTouchXDifference());
            }

            Touch.setInputHandled();
        }
    }

    public void handleInput() {
        handleKeyboardInput();
        handleTouchInput();
    }
}
