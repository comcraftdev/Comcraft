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

     private void addItem(InvItem item, boolean premiumOnly) {
        itemStackList.addElement(new InvItemStack(item.shiftedIndex, 1));
    }
    
    private void addBlock(Block block, boolean premiumOnly) {
        itemStackList.addElement(new InvItemStack(block.blockID, 1));
    }

    private void initItemStackList() {
        /*
         * The blocks are added to the inventory here.
         */
        
        addBlock(Block.stone, false);
        addBlock(Block.grass, false);
        addItem(Item.hammer, false);
        addBlock(Block.dirt, false);
        addBlock(Block.sand, false);
        addBlock(Block.cobblestone, false);
        addBlock(Block.leaves, false);
        addBlock(Block.glass, false);
        addBlock(Block.wood, false);
        addBlock(Block.wood1, false);
        addBlock(Block.wood2, false);
        addBlock(Block.planks, false);
        addBlock(Block.planks1, true);
        addBlock(Block.planks2, true);
        addBlock(Block.planks3, true);
        addItem(Item.detonator, true);
        addItem(Item.seeds, false);
        addBlock(Block.tnt, true);
        addBlock(Block.tntWeak, true);
        addBlock(Block.tntStrong, true);
        addBlock(Block.torch, false);
        addBlock(Block.woodenDoor, false);
        addBlock(Block.ironDoor, true);
        addBlock(Block.bedrock, false);
        addBlock(Block.lapisLazuli, false);
        addBlock(Block.sandStone, false);
        addBlock(Block.woolWhite, false);
        addBlock(Block.woolRed, false);
        addBlock(Block.woolOrange, false);
        addBlock(Block.woolYellow, false);
        addBlock(Block.woolLime, false);
        addBlock(Block.woolGreen, false);
        addBlock(Block.woolLightBlue, false);
        addBlock(Block.woolCyan, false);
        addBlock(Block.woolBlue, false);
        addBlock(Block.woolMagenta, false);
        addBlock(Block.woolPink, false);
        addBlock(Block.woolPurple, false);
        addBlock(Block.woolBrown, false);
        addBlock(Block.woolLightGray, false);
        addBlock(Block.woolGray, false);
        addBlock(Block.woolBlack, false);
        addBlock(Block.cactus, true);
        addBlock(Block.redFlower, false);
        addBlock(Block.yellowFlower, false);
        addBlock(Block.treePlant, false);
        addBlock(Block.mushroom, false);
        addBlock(Block.toadstool, false);
        addBlock(Block.wheat, false);
        addBlock(Block.fencePlank, true);
        addBlock(Block.fencePlank2, true);
        addBlock(Block.fencePlank3, true);
        addBlock(Block.fencePlank4, true);
        addBlock(Block.fenceStone, true);
        addBlock(Block.fenceBrick, true);
        addBlock(Block.fenceNetherbrick, true);
        addBlock(Block.iron, false);
        addBlock(Block.gold, false);
        addBlock(Block.diamond, false);
        addBlock(Block.doubleStoneSlab, false);
        addBlock(Block.stoneSlab, true);
        addBlock(Block.plankSlab, false);
        addBlock(Block.plankSlab1, true);
        addBlock(Block.plankSlab2, true);
        addBlock(Block.plankSlab3, true);
        addBlock(Block.cobblestoneSlab, true);
        addBlock(Block.brickSlab, false);
        addBlock(Block.netherBrickSlab, true);
        addBlock(Block.whiteWoolSlab, false);
        addBlock(Block.redWoolSlab, false);
        addBlock(Block.orangeWoolSlab, true);
        addBlock(Block.yellowWoolSlab, true);
        addBlock(Block.lightGreenWoolSlab, true);
        addBlock(Block.greenWoolSlab, true);
        addBlock(Block.blueWoolSlab, true);
        addBlock(Block.pinkWoolSlab, true);
        addBlock(Block.blackWoolSlab, false);
        addBlock(Block.craftingTable, false);
        addBlock(Block.bed, true);
        addBlock(Block.furnace, false);
        addBlock(Block.chest, false);
        addBlock(Block.water, false);
        addBlock(Block.lava, true);
        addBlock(Block.stoneTitle, false);
        addBlock(Block.plankTitle, false);
        addBlock(Block.plankTitle1, true);
        addBlock(Block.plankTitle2, true);
        addBlock(Block.plankTitle3, true);
        addBlock(Block.cobblestoneTitle, true);
        addBlock(Block.brickTitle, true);
        addBlock(Block.netherBrickTitle, true);
        addBlock(Block.brick, false);
        addBlock(Block.bookshelve, false);
        addBlock(Block.mossStone, false);
        addBlock(Block.obsidian, false);
        addBlock(Block.ice, false);
        addBlock(Block.snowBlock, false);
        addBlock(Block.snow, false);
        addBlock(Block.pumpkin, true);
        addBlock(Block.netherrack, false);
        addBlock(Block.soulSand, false);
        addBlock(Block.stoneBrick, true);
        addBlock(Block.netherBrick, true);
        addBlock(Block.plankStairs, false);
        addBlock(Block.plankStairs1, false);
        addBlock(Block.plankStairs2, true);
        addBlock(Block.plankStairs3, true);
        addBlock(Block.brickStairs, false);
        addBlock(Block.netherBrickStairs, true);
        addBlock(Block.stoneStairs, false);
        addBlock(Block.cobblestoneStairs, true);
        addBlock(Block.whiteWoolStairs, false);
        addBlock(Block.redWoolStairs, false);
        addBlock(Block.orangeWoolStairs, true);
        addBlock(Block.yellowWoolStairs, true);
        addBlock(Block.lightGreenWoolStairs, true);
        addBlock(Block.greenWoolStairs, true);
        addBlock(Block.blueWoolStairs, true);
        addBlock(Block.pinkWoolStairs, true);
        addBlock(Block.blackWoolStairs, true);
        addBlock(Block.emoticon, false);
        addBlock(Block.alphabet1, false);
        addBlock(Block.alphabet2, false);
        addBlock(Block.numbers, false);
        addBlock(Block.animalPig, false);
        addBlock(Block.animalSheep, false);
        addBlock(Block.animalCow, false);
        addBlock(Block.animalChicken, false);


        for (int id = 0; id < Block.blocksList.length; ++id) {
            InvItemStack itemStack = new InvItemStack(id, 1);

            if (Block.blocksList[id] != null && !itemStackList.contains(itemStack)) {
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
