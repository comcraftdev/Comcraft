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

/*
 * Note!
 * I decided to change Block source code, so now it's more similar Minecraft's source code.
 * I hope it will help you create your mods in an easier way.
 */

package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;

public class Block extends JsObject {

    public static final Block[] blocksList = new Block[256];

    public final int blockID;
    private String blockName;
    private String blockPlacingSound = "/sound/block_placed.wav";
    private String blockDestroyingSound = "/sound/block_placed.wav";
    private boolean collidesWithPlayer = true;
    private boolean doesBlockDestroyGrass = true;
    private boolean isReplaceableBlock = false;
    private boolean isUpdatableBlock = false;
    private boolean canBePieced = true;
    private boolean canBePiecedVertically = true;
    private boolean isNormal = true;
    private int RenderType = 1;
    private int IdDropped;
    private EventHandler eh = new EventHandler(new String[] { "getBlockTransform", "getBlockTexture", "shouldSideBeRendered",
            "getCollisionBoundingBoxFromPool", "canPlaceBlockAt", "canPlaceBlockOnSide", "blockActivated", "onBlockPlaced", "onBlockPlacedBy",
            "onBlockDestroyedByPlayer", "onNeighborBlockChange", "onBlockRemoval", "onBlockAdded", "tickBlock", "getBlockModel" });
    private VertexBuffer[][][][] BlockVertexBufferPieced = ModelPieceBlock.vertexBuffer;
    private IndexBuffer BlockIndexBuffer = ModelPieceBlock.indexBuffer;
    private int[] UsedTexturesList;
    private VertexBuffer[] BlockVertexBufferSided = null;

    // ModLoader start
    private static final int ID_CREATE = 100;
    private static final int ID_GET_BLOCK_NAME = 101;
    private static final int ID_IS_NORMAL = 102;
    private static final int ID_GET_BLOCK_DESTROYING_SOUND = 103;
    private static final int ID_GET_BLOCK_PLACING_SOUND = 104;
    // ModLoader end

    protected Block(int id) {
        super(OBJECT_PROTOTYPE); // ModLoader
        if (blocksList[id] != null) {
            throw new ComcraftException("Block ID is already in use! Id: " + id, null);
        }
        blocksList[id] = this;
        IdDropped = blockID = id;
        addNative("create", ID_CREATE, 2);
    }

    protected Block(int id, int index) {
        this(id);
        UsedTexturesList = new int[] { index };
    }

    public boolean collidesWithPlayer() {
        return collidesWithPlayer;
    }

    public boolean doesBlockDestroyGrass() {
        return doesBlockDestroyGrass;
    }

    public boolean isReplaceableBlock() {
        return isReplaceableBlock;
    }

    public boolean isUpdatableBlock() {
        return isUpdatableBlock;
    }

    public boolean canBePieced() {
        return canBePieced;
    }

    public boolean canBePiecedVertically() {
        return canBePiecedVertically;
    }

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int side) {
        eh.runEvent("getBlockTransform", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), transform, new Integer(side) });
        Object success = eh.getLastSuccess("getBlockTransform");
        if (success == null) {
            return transform;
        }
        return (Transform) success;

    }

    public VertexBuffer[][][][] getBlockVertexBufferPieced(World world, int x, int y, int z) {
        return BlockVertexBufferPieced;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        if (BlockVertexBufferSided == null) {
            return getBlockVertexBufferPieced(world, x, y, z)[0][0][0];
        }
        return BlockVertexBufferSided;
    }

    public IndexBuffer getBlockIndexBuffer() {
        return BlockIndexBuffer;
    }

    public String getBlockName() {
        return blockName;
    }

    protected void setBlockName(String name) {
        blockName = name;
    }

    public int getIdDropped() {
        return IdDropped;
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        eh.runEvent("getBlockTexture", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(side) });
        Object success = eh.getLastSuccess("getBlockTexture");
        if (success == null) {
            return UsedTexturesList[0];
        }
        return ((Double) success).intValue();
    }

    public boolean isNormal() {
        return isNormal;
    }

    public int getRenderType() {
        return RenderType;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        eh.runEvent("shouldSideBeRendered", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(side) });
        Object success = eh.getLastSuccess("shouldSideBeRendered");
        if (success == null) {
            return !world.isBlockNormal(x, y, z);
        }
        return ((Boolean) success).booleanValue();
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        eh.runEvent("getCollisionBoundingBoxFromPool", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
        Object success = eh.getLastSuccess("getCollisionBoundingBoxFromPool");
        if (success == null) {
            return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f);
        }
        return (AxisAlignedBB) success;
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        eh.runEvent("canPlaceBlockOnSide", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(side) });
        Object success = eh.getLastSuccess("canPlaceBlockOnSide");
        if (success == null) {
            return canPlaceBlockAt(world, x, y, z);
        }
        return ((Boolean) success).booleanValue();
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        eh.runEvent("canPlaceBlockAt", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
        Object success = eh.getLastSuccess("canPlaceBlockAt");
        if (success == null) {
            int i = world.getBlockID(x, y, z);
            return i == 0 || Block.blocksList[i].isReplaceableBlock();
        }
        return ((Boolean) success).booleanValue();
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
        eh.runEvent("onBlockPlaced", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(side) });
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityPlayer entityPlayer) {
        eh.runEvent("onBlockPlacedBy", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), entityPlayer });
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
        eh.runEvent("onBlockDestroyedByPlayer", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(metadata) });
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        eh.runEvent("blockActivated", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), entityplayer, itemStack });
        Object success = eh.getLastSuccess("blockActivated");
        if (success == null) {
            return false;
        }
        return ((Boolean) success).booleanValue();
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
        eh.runEvent("onNeighborBlockChange", new Object[] { world, new Integer(x), new Integer(y), new Integer(z), new Integer(blockID) });
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
        eh.runEvent("onBlockRemoval", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        eh.runEvent("onBlockAdded", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
    }

    public String getBlockDestroyingSound() {
        return blockDestroyingSound;
    }

    public String getBlockPlacingSound() {
        return blockPlacingSound;
    }

    public void tickBlock(World world, int x, int y, int z) {
        eh.runEvent("tickBlock", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
    }

    public int[] getUsedTexturesList() {
        return UsedTexturesList;
    }

    public Node getBlockModel(World world, int x, int y, int z) {
        eh.runEvent("getBlockModel", new Object[] { world, new Integer(x), new Integer(y), new Integer(z) });
        Object success = eh.getLastSuccess("getBlockModel");
        if (success == null) {
            return null;
        }
        return (Node) success;
    }

    public static Block getBlock(String name) {
        for (int i = 0; i < blocksList.length; i++) {
            if (blocksList[i] == null) {
                continue;
            }
            if (blocksList[i].getBlockName().equals(name)) {
                return blocksList[i];
            }
        }
        return null;
    }

    static {
        new Block(1, 1).setBlockName("stone");
        new BlockGrass(2, 3, 0, 2).setBlockName("grass");
        new BlockDirt(3, 2).setBlockName("dirt");
        new BlockGlass(4, 49).setBlockName("glass");
        new Block(5, 53).setBlockName("leaves");
        new Block(6, 18).setBlockName("sand");
        new Block(7, 4).setBlockName("planks");
        new BlockSidesTopBottom(8, 20, 21, 21).setBlockName("wood");
        new Block(9, 7).setBlockName("brick");
        new Block(10, 64).setBlockName("woolWhite");
        new Block(11, 113).setBlockName("woolBlack");
        new Block(12, 129).setBlockName("woolRed");
        new Block(13, 177).setBlockName("woolBlue");
        new Block(14, 162).setBlockName("woolYellow");
        new Block(15, 145).setBlockName("woolGreen");
        new Block(16, 16).setBlockName("cobblestone");
        new Block(17, 37).setBlockName("obsidian");
        new BlockSidesTop(18, 35, 4).setBlockName("bookshelve");
        new BlockFurnance(19, 119, 118, 102, 102, 120).setBlockName("pumpkin");
        new BlockIce(20, 67).setBlockName("ice");
        new Block(21, 54).setBlockName("stoneBrick");
        new Block(22, 36).setBlockName("mossStone");
        new Block(23, 22).setBlockName("iron");
        new Block(24, 23).setBlockName("gold");
        new Block(25, 24).setBlockName("diamond");
        new Block(26, 17).setBlockName("bedrock");
        new BlockSlab(27, 5, 6).setBlockName("stoneSlab");
        new BlockSlab(28, 4, 4).setBlockName("plankSlab");
        new BlockSidesTop(29, 5, 6).setBlockName("doubleStoneSlab");
        new BlockSlab(30, 16, 16).setBlockName("cobblestoneSlab");
        new BlockSidesTop(31, 70, 69).setBlockName("cactus");
        new Block(32, 225).setBlockName("woolLightGray");
        new Block(33, 114).setBlockName("woolGray");
        new Block(34, 210).setBlockName("woolOrange");
        new Block(35, 146).setBlockName("woolLime");
        new Block(36, 209).setBlockName("woolCyan");
        new Block(37, 178).setBlockName("woolLightBlue");
        new Block(38, 193).setBlockName("woolPurple");
        new Block(39, 194).setBlockName("woolMagenta");
        new Block(40, 130).setBlockName("woolPink");
        new Block(41, 161).setBlockName("woolBrown");
        new BlockLiquid(42, 205).setBlockName("water");
        new BlockFlower(43, 12).setBlockName("redFlower");
        new BlockFlower(44, 13).setBlockName("yellowFlower");
        new BlockTreePlant(45, 15).setBlockName("treePlant");
        new BlockFlower(46, 28).setBlockName("toadstool");
        new BlockFlower(47, 29).setBlockName("mushroom");
        new BlockLiquid(48, 237).setBlockName("lava");
        new BlockSidesTopBottom(49, 68, 66, 2).setBlockName("snow");
        new Block(50, 66).setBlockName("snowBlock");
        new Block(51, 192).setBlockName("sandStone");
        new Block(52, 144).setBlockName("lapisLazuli");
        new BlockCraftingTable(53, 59, 60, 43, 43).setBlockName("craftingTable");
        new BlockFurnance(54, 44, 45, 62, 62, 61).setBlockName("furnace");
        new BlockTNT(55, 8, 9, 10, 256, 257, 258, 3, 3).setBlockName("tnt");
        new Block(56, 103).setBlockName("netherrack");
        new Block(57, 224).setBlockName("netherBrick");
        new Block(58, 104).setBlockName("soulSand");
        new BlockTitle(60, 4).setBlockName("plankTitle");
        new Block(61, 198).setBlockName("planks1");
        new Block(62, 199).setBlockName("planks2");
        new Block(63, 214).setBlockName("planks3");
        new BlockSlab(64, 198, 198).setBlockName("plankSlab1");
        new BlockSlab(65, 199, 199).setBlockName("plankSlab2");
        new BlockSlab(66, 214, 214).setBlockName("plankSlab3");
        new BlockSlab(67, 224, 224).setBlockName("netherBrickSlab");
        new BlockSlab(68, 7, 7).setBlockName("brickSlab");
        new BlockSidesTopBottom(69, 116, 21, 21).setBlockName("wood1");
        new BlockSidesTopBottom(70, 117, 21, 21).setBlockName("wood2");
        new BlockTitle(72, 198).setBlockName("plankTitle1");
        new BlockTitle(73, 199).setBlockName("plankTitle2");
        new BlockTitle(74, 214).setBlockName("plankTitle3");
        new BlockTitle(75, 7).setBlockName("brickTitle");
        new BlockTitle(76, 224).setBlockName("netherBrickTitle");
        new BlockTitle(77, 6).setBlockName("stoneTitle");
        new BlockTorch(78, 80).setBlockName("torch");
        new BlockDoor(79, 81, 97).setBlockName("woodenDoor");
        new BlockDoor(80, 82, 98).setBlockName("ironDoor");
        new BlockTitle(81, 16).setBlockName("cobblestoneTitle");
        // new BlockModel(82, "/models/gus.m3g").setBlockName("gus");
        new BlockStairs(82, 4).setBlockName("plankStairs");
        new BlockStairs(83, 198).setBlockName("plankStairs1");
        new BlockStairs(84, 199).setBlockName("plankStairs2");
        new BlockStairs(85, 214).setBlockName("plankStairs3");
        new BlockStairs(86, 7).setBlockName("brickStairs");
        new BlockStairs(87, 224).setBlockName("netherBrickStairs");
        new BlockStairs(88, 6).setBlockName("stoneStairs");
        new BlockStairs(89, 16).setBlockName("cobblestoneStairs");
        new BlockStairs(90, 64).setBlockName("whiteWoolStairs");
        new BlockStairs(91, 113).setBlockName("blackWoolStairs");
        new BlockStairs(92, 129).setBlockName("redWoolStairs");
        new BlockStairs(93, 177).setBlockName("blueWoolStairs");
        new BlockStairs(94, 162).setBlockName("yellowWoolStairs");
        new BlockStairs(95, 145).setBlockName("greenWoolStairs");
        new BlockStairs(96, 146).setBlockName("lightGreenWoolStairs");
        new BlockStairs(97, 210).setBlockName("orangeWoolStairs");
        new BlockStairs(98, 130).setBlockName("pinkWoolStairs");
        new BlockSlab(99, 64).setBlockName("whiteWoolSlab");
        new BlockSlab(100, 113).setBlockName("blackWoolSlab");
        new BlockSlab(101, 129).setBlockName("redWoolSlab");
        new BlockSlab(102, 177).setBlockName("blueWoolSlab");
        new BlockSlab(103, 162).setBlockName("yellowWoolSlab");
        new BlockSlab(104, 145).setBlockName("greenWoolSlab");
        new BlockSlab(105, 146).setBlockName("lightGreenWoolSlab");
        new BlockSlab(106, 210).setBlockName("orangeWoolSlab");
        new BlockSlab(107, 130).setBlockName("pinkWoolSlab");
        new BlockTNT(108, 265, 266, 267, 281, 282, 283, 1, 1).setBlockName("tntWeak");
        new BlockTNT(109, 268, 269, 270, 284, 285, 286, 6, 5).setBlockName("tntStrong");
        new BlockFurnance(110, 274, 273, 272, 272, 275).setBlockName("chest");
        new BlockEmoticon(111, 280, 272, 272, 272, new int[] { 279, 275, 276, 277, 278, 271, 287 }).setBlockName("emoticon");
        new BlockAlphabet(112, 288, 272, 272, 272, new int[] { 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300 }).setBlockName("alphabet1");
        new BlockAlphabet(113, 301, 272, 272, 272, new int[] { 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313 }).setBlockName("alphabet2");
        new BlockAlphabet(114, 314, 272, 272, 272, new int[] { 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327 }).setBlockName("numbers");
        new BlockFence(115, 328).setBlockName("fencePlank");
        new BlockFence(116, 329).setBlockName("fencePlank2");
        new BlockFence(117, 330).setBlockName("fencePlank3");
        new BlockFence(118, 331).setBlockName("fencePlank4");
        new BlockWheat(119, new int[] { 88, 89, 90, 91, 92, 93, 94, 95 }).setBlockName("wheat");
        new BlockFence(120, 332).setBlockName("fenceNetherbrick");
        new BlockFence(121, 333).setBlockName("fenceStone");
        new BlockFence(122, 334).setBlockName("fenceBrick");
        new BlockAnimal(123, 337, 336, 338, 336, 336).setBlockName("animalSheep");
        new BlockBed(124, 135, 134, 149, 152, 150, 151, 335, 351).setBlockName("bed");
        new BlockAnimal(125, 339, 341, 340, 357, 357).setBlockName("animalChicken");
        new BlockAnimal(126, 342, 344, 343, 344, 344).setBlockName("animalCow");
        new BlockAnimal(127, 345, 348, 346, 347, 347).setBlockName("animalPig");

        InvItem.itemsList[getBlock("stoneSlab").blockID] = new InvItemSlab(getBlock("stoneSlab").blockID - 256, getBlock("doubleStoneSlab"));
        InvItem.itemsList[getBlock("plankSlab").blockID] = new InvItemSlab(getBlock("plankSlab").blockID - 256, getBlock("planks"));
        InvItem.itemsList[getBlock("cobblestoneSlab").blockID] = new InvItemSlab(getBlock("cobblestoneSlab").blockID - 256, getBlock("cobblestone"));
        InvItem.itemsList[getBlock("plankSlab1").blockID] = new InvItemSlab(getBlock("plankSlab1").blockID - 256, getBlock("planks1"));
        InvItem.itemsList[getBlock("plankSlab2").blockID] = new InvItemSlab(getBlock("plankSlab2").blockID - 256, getBlock("planks2"));
        InvItem.itemsList[getBlock("plankSlab3").blockID] = new InvItemSlab(getBlock("plankSlab3").blockID - 256, getBlock("planks3"));
        InvItem.itemsList[getBlock("netherBrickSlab").blockID] = new InvItemSlab(getBlock("netherBrickSlab").blockID - 256, getBlock("netherBrick"));
        InvItem.itemsList[getBlock("brickSlab").blockID] = new InvItemSlab(getBlock("brickSlab").blockID - 256, getBlock("brick"));

        InvItem.itemsList[getBlock("woodenDoor").blockID] = new InvItemBlock(getBlock("woodenDoor").blockID - 256, 43);
        InvItem.itemsList[getBlock("ironDoor").blockID] = new InvItemBlock(getBlock("ironDoor").blockID - 256, 44);

        InvItem.itemsList[getBlock("water").blockID] = new InvItemBlock(getBlock("water").blockID - 256, 75);
        InvItem.itemsList[getBlock("lava").blockID] = new InvItemBlock(getBlock("lava").blockID - 256, 76);

        InvItem.itemsList[getBlock("bed").blockID] = new InvItemBlock(getBlock("bed").blockID - 256, 45);

        for (int i = 0; i < 256; i++) {
            if (blocksList[i] == null) {
                continue;
            }

            if (InvItem.itemsList[i] == null) {
                InvItem.itemsList[i] = new InvItemBlock(i - 256);
            }
        }
    }

    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_GET_BLOCK_NAME:
            stack.setObject(sp, getBlockName());
            break;
        case ID_IS_NORMAL:
            stack.setBoolean(sp, isNormal());
            break;
        case ID_GET_BLOCK_DESTROYING_SOUND:
            stack.setObject(sp, getBlockDestroyingSound());
            break;
        case ID_GET_BLOCK_PLACING_SOUND:
            stack.setObject(sp, getBlockPlacingSound());
            break;
        case ID_CREATE:
            InvItem.itemsList[blockID] = new InvItemBlock(blockID - 256);
            String name = stack.getString(sp + 2);
            // if (getBlock(name) != null) {
            // blocksList[blockID] = null;
            // throw new
            // ComcraftException("Block name is already in use! name: " + name,
            // null);
            // }
            setBlockName(name);
            JsObject properties = stack.getJsObject(sp + 3);
            Object v;

            v = properties.getObject("collidesWithPlayer");
            if (v != null)
                collidesWithPlayer = ((Boolean) v).booleanValue();
            v = properties.getObject("doesBlockDestroyGrass");
            if (v != null)
                doesBlockDestroyGrass = ((Boolean) v).booleanValue();
            v = properties.getObject("isReplaceableBlock");
            if (v != null)
                isReplaceableBlock = ((Boolean) v).booleanValue();
            v = properties.getObject("isUpdatableBlock");
            if (v != null)
                isUpdatableBlock = ((Boolean) v).booleanValue();
            v = properties.getObject("canBePieced");
            if (v != null)
                canBePieced = ((Boolean) v).booleanValue();
            v = properties.getObject("canBePiecedVertically");
            if (v != null)
                canBePiecedVertically = ((Boolean) v).booleanValue();
            v = properties.getObject("isNormal");
            if (v != null)
                isNormal = ((Boolean) v).booleanValue();

            v = properties.getObject("RenderType");
            if (v != null)
                RenderType = ((Double) v).intValue();
            v = properties.getObject("IdDropped");
            if (v != null)
                IdDropped = ((Double) v).intValue();

            v = properties.getObject("blockPlacingSound");
            if (v != null)
                blockPlacingSound = (String) v;
            v = properties.getObject("blockDestroyingSound");
            if (v != null)
                blockDestroyingSound = (String) v;

            v = properties.getObject("getBlockTransform");
            if (v != null)
                eh.bindEvent("getBlockTransform", (JsFunction) v);
            v = properties.getObject("getBlockVertexBufferPieced");
            if (v != null)
                BlockVertexBufferPieced = (VertexBuffer[][][][]) v;
            v = properties.getObject("getBlockVertexBufferSided");
            if (v != null)
                BlockVertexBufferSided = (VertexBuffer[]) v;
            v = properties.getObject("getBlockIndexBuffer");
            if (v != null)
                BlockIndexBuffer = (IndexBuffer) v;
            v = properties.getObject("getBlockTexture");
            if (v != null)
                eh.bindEvent("getBlockTexture", (JsFunction) v);
            v = properties.getObject("shouldSideBeRendered");
            if (v != null)
                eh.bindEvent("shouldSideBeRendered", (JsFunction) v);
            v = properties.getObject("getCollisionBoundingBoxFromPool");
            if (v != null)
                eh.bindEvent("getCollisionBoundingBoxFromPool", (JsFunction) v);
            v = properties.getObject("canPlaceBlockAt");
            if (v != null)
                eh.bindEvent("canPlaceBlockAt", (JsFunction) v);
            v = properties.getObject("canPlaceBlockOnSide");
            if (v != null)
                eh.bindEvent("canPlaceBlockOnSide", (JsFunction) v);
            v = properties.getObject("blockActivated");
            if (v != null)
                eh.bindEvent("blockActivated", (JsFunction) v);
            v = properties.getObject("onBlockPlaced");
            if (v != null)
                eh.bindEvent("onBlockPlaced", (JsFunction) v);
            v = properties.getObject("onBlockPlacedBy");
            if (v != null)
                eh.bindEvent("onBlockPlacedBy", (JsFunction) v);
            v = properties.getObject("onBlockDestroyedByPlayer");
            if (v != null)
                eh.bindEvent("onBlockDestroyedByPlayer", (JsFunction) v);
            v = properties.getObject("onNeighborBlockChange");
            if (v != null)
                eh.bindEvent("onNeighborBlockChange", (JsFunction) v);
            v = properties.getObject("onBlockRemoval");
            if (v != null)
                eh.bindEvent("onBlockRemoval", (JsFunction) v);
            v = properties.getObject("onBlockAdded");
            if (v != null)
                eh.bindEvent("onBlockAdded", (JsFunction) v);
            v = properties.getObject("tickBlock");
            if (v != null)
                eh.bindEvent("tickBlock", (JsFunction) v);
            v = properties.getObject("getBlockModel");
            if (v != null)
                eh.bindEvent("getBlockModel", (JsFunction) v);

            v = properties.getObject("textures");
            if (v != null)
                UsedTexturesList = ModArray.toIntArray((JsArray) v);
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    public String toString() {
        return "[object Block[" + blockID + "]]";
    }
    // ModLoader end
}
