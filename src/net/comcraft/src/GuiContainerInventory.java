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
        
        addBlock(Block.stone);
        addBlock(Block.grass);
        addItem(InvItem.hammer);
        addBlock(Block.dirt);
        addBlock(Block.sand);
        addBlock(Block.cobblestone);
        addBlock(Block.leaves);
        addBlock(Block.glass);
        addBlock(Block.wood);
        addBlock(Block.wood1);
        addBlock(Block.wood2);
        addBlock(Block.planks);
        addBlock(Block.planks1);
        addBlock(Block.planks2);
        addBlock(Block.planks3);
        addItem(InvItem.detonator);
        addItem(InvItem.seeds);
        addBlock(Block.tnt);
        addBlock(Block.tntWeak);
        addBlock(Block.tntStrong);
        addBlock(Block.torch);
        addBlock(Block.woodenDoor);
        addBlock(Block.ironDoor);
        addBlock(Block.bedrock);
        addBlock(Block.lapisLazuli);
        addBlock(Block.sandStone);
        addBlock(Block.woolWhite);
        addBlock(Block.woolRed);
        addBlock(Block.woolOrange);
        addBlock(Block.woolYellow);
        addBlock(Block.woolLime);
        addBlock(Block.woolGreen);
        addBlock(Block.woolLightBlue);
        addBlock(Block.woolCyan);
        addBlock(Block.woolBlue);
        addBlock(Block.woolMagenta);
        addBlock(Block.woolPink);
        addBlock(Block.woolPurple);
        addBlock(Block.woolBrown);
        addBlock(Block.woolLightGray);
        addBlock(Block.woolGray);
        addBlock(Block.woolBlack);
        addBlock(Block.cactus);
        addBlock(Block.redFlower);
        addBlock(Block.yellowFlower);
        addBlock(Block.treePlant);
        addBlock(Block.mushroom);
        addBlock(Block.toadstool);
        addBlock(Block.wheat);
        addBlock(Block.fencePlank);
        addBlock(Block.fencePlank2);
        addBlock(Block.fencePlank3);
        addBlock(Block.fencePlank4);
        addBlock(Block.fenceStone);
        addBlock(Block.fenceBrick);
        addBlock(Block.fenceNetherbrick);
        addBlock(Block.iron);
        addBlock(Block.gold);
        addBlock(Block.diamond);
        addBlock(Block.doubleStoneSlab);
        addBlock(Block.stoneSlab);
        addBlock(Block.plankSlab);
        addBlock(Block.plankSlab1);
        addBlock(Block.plankSlab2);
        addBlock(Block.plankSlab3);
        addBlock(Block.cobblestoneSlab);
        addBlock(Block.brickSlab);
        addBlock(Block.netherBrickSlab);
        addBlock(Block.whiteWoolSlab);
        addBlock(Block.redWoolSlab);
        addBlock(Block.orangeWoolSlab);
        addBlock(Block.yellowWoolSlab);
        addBlock(Block.lightGreenWoolSlab);
        addBlock(Block.greenWoolSlab);
        addBlock(Block.blueWoolSlab);
        addBlock(Block.pinkWoolSlab);
        addBlock(Block.blackWoolSlab);
        addBlock(Block.craftingTable);
        addBlock(Block.bed);
        addBlock(Block.furnace);
        addBlock(Block.chest);
        addBlock(Block.water);
        addBlock(Block.lava);
        addBlock(Block.stoneTitle);
        addBlock(Block.plankTitle);
        addBlock(Block.plankTitle1);
        addBlock(Block.plankTitle2);
        addBlock(Block.plankTitle3);
        addBlock(Block.cobblestoneTitle);
        addBlock(Block.brickTitle);
        addBlock(Block.netherBrickTitle);
        addBlock(Block.brick);
        addBlock(Block.bookshelve);
        addBlock(Block.mossStone);
        addBlock(Block.obsidian);
        addBlock(Block.ice);
        addBlock(Block.snowBlock);
        addBlock(Block.snow);
        addBlock(Block.pumpkin);
        addBlock(Block.netherrack);
        addBlock(Block.soulSand);
        addBlock(Block.stoneBrick);
        addBlock(Block.netherBrick);
        addBlock(Block.plankStairs);
        addBlock(Block.plankStairs1);
        addBlock(Block.plankStairs2);
        addBlock(Block.plankStairs3);
        addBlock(Block.brickStairs);
        addBlock(Block.netherBrickStairs);
        addBlock(Block.stoneStairs);
        addBlock(Block.cobblestoneStairs);
        addBlock(Block.whiteWoolStairs);
        addBlock(Block.redWoolStairs);
        addBlock(Block.orangeWoolStairs);
        addBlock(Block.yellowWoolStairs);
        addBlock(Block.lightGreenWoolStairs);
        addBlock(Block.greenWoolStairs);
        addBlock(Block.blueWoolStairs);
        addBlock(Block.pinkWoolStairs);
        addBlock(Block.blackWoolStairs);
        addBlock(Block.emoticon);
        addBlock(Block.alphabet1);
        addBlock(Block.alphabet2);
        addBlock(Block.numbers);
        addBlock(Block.animalPig);
        addBlock(Block.animalSheep);
        addBlock(Block.animalCow);
        addBlock(Block.animalChicken);


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
