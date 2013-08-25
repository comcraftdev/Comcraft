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

// ModLoader start
import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;
// ModLoader end
import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexBuffer;

public class Block extends JsObject { // ModLoader

    public static final Block[] blocksList;
    public static final Block stone;
    public static final Block grass;
    public static final Block dirt;
    public static final Block glass;
    public static final Block leaves;
    public static final Block sand;
    public static final Block planks;
    public static final Block wood;
    public static final Block brick;
    public static final Block woolWhite;
    public static final Block woolBlack;
    public static final Block woolRed;
    public static final Block woolBlue;
    public static final Block woolYellow;
    public static final Block woolGreen;
    public static final Block woolLightGray;
    public static final Block woolGray;
    public static final Block woolOrange;
    public static final Block woolLime;
    public static final Block woolCyan;
    public static final Block woolLightBlue;
    public static final Block woolPurple;
    public static final Block woolMagenta;
    public static final Block woolPink;
    public static final Block woolBrown;
    public static final Block cobblestone;
    public static final Block bookshelve;
    public static final Block obsidian;
    public static final Block pumpkin;
    public static final Block ice;
    public static final Block stoneBrick;
    public static final Block mossStone;
    public static final Block iron;
    public static final Block gold;
    public static final Block diamond;
    public static final Block bedrock;
    public static final Block stoneSlab;
    public static final Block plankSlab;
    public static final Block doubleStoneSlab;
    public static final Block cobblestoneSlab;
    public static final Block cactus;
    public static final Block water;
    public static final Block redFlower;
    public static final Block yellowFlower;
    public static final Block treePlant;
    public static final Block toadstool;
    public static final Block mushroom;
    public static final Block lava;
    public static final Block snow;
    public static final Block snowBlock;
    public static final Block sandStone;
    public static final Block lapisLazuli;
    public static final Block craftingTable;
    public static final Block furnace;
    public static final Block tnt;
    public static final Block tntWeak;
    public static final Block tntStrong;
    public static final Block netherrack;
    public static final Block netherBrick;
    public static final Block soulSand;
    public static final Block plankTitle;
    public static final Block planks1;
    public static final Block planks2;
    public static final Block planks3;
    public static final Block plankSlab1;
    public static final Block plankSlab2;
    public static final Block plankSlab3;
    public static final Block netherBrickSlab;
    public static final Block brickSlab;
    public static final Block whiteWoolSlab;
    public static final Block blackWoolSlab;
    public static final Block redWoolSlab;
    public static final Block blueWoolSlab;
    public static final Block yellowWoolSlab;
    public static final Block greenWoolSlab;
    public static final Block lightGreenWoolSlab;
    public static final Block orangeWoolSlab;
    public static final Block pinkWoolSlab;
    public static final Block wood1;
    public static final Block wood2;
    public static final Block plankTitle1;
    public static final Block plankTitle2;
    public static final Block plankTitle3;
    public static final Block brickTitle;
    public static final Block netherBrickTitle;
    public static final Block stoneTitle;
    public static final Block cobblestoneTitle;
    public static final Block torch;
    public static final Block woodenDoor;
    public static final Block ironDoor;
//    public static final Block gus;
    public static final Block plankStairs;
    public static final Block plankStairs1;
    public static final Block plankStairs2;
    public static final Block plankStairs3;
    public static final Block brickStairs;
    public static final Block netherBrickStairs;
    public static final Block stoneStairs;
    public static final Block cobblestoneStairs;
    public static final Block whiteWoolStairs;
    public static final Block blackWoolStairs;
    public static final Block redWoolStairs;
    public static final Block blueWoolStairs;
    public static final Block yellowWoolStairs;
    public static final Block greenWoolStairs;
    public static final Block lightGreenWoolStairs;
    public static final Block orangeWoolStairs;
    public static final Block pinkWoolStairs;
    public static final Block chest;
    public static final Block emoticon;
    public static final Block alphabet1;
    public static final Block alphabet2;
    public static final Block numbers;
    public static final Block fencePlank;
    public static final Block fencePlank2;
    public static final Block fencePlank3;
    public static final Block fencePlank4;
    public static final Block fenceStone;
    public static final Block fenceBrick;
    public static final Block fenceNetherbrick;
    public static final Block wheat;
    public static final Block animalSheep;
    public static final Block animalChicken;
    public static final Block animalCow;
    public static final Block animalPig;
    public static final Block bed;
    /*
     * Block custom
     */
    protected int blockIndexInTexture;
    public final int blockID;
    private String blockName;
    private String blockPlacingSound = "/sound/block_placed.wav";
    private String blockDestroyingSound = "/sound/block_placed.wav";
    // ModLoader start
    private static final int ID_COLLIDES_WITH_PLAYER = 100;
    private static final int ID_DOES_BLOCK_DESTROY_GRASS = 101;
    private static final int ID_IS_REPLACEABLE_BLOCK = 102;
    private static final int ID_IS_UPDATABLE_BLOCK = 103;
    private static final int ID_CAN_BE_PIECED = 104;
    private static final int ID_CAN_BE_PIECED_VERTICALLY = 105;
    private static final int ID_GET_BLOCK_NAME = 106;
    private static final int ID_SET_BLOCK_NAME = 107;
    private static final int ID_GET_ID_DROPPED = 108;
    private static final int ID_GET_BLOCK_TEXTURE = 109;
    private static final int ID_IS_NORMAL = 110;
    private static final int ID_GET_RENDER_TYPE = 111;
    private static final int ID_SHOULD_SIDE_BE_RENDERED = 112;
    private static final int ID_CAN_PLACE_BLOCK_ON_SIDE = 113;
    private static final int ID_CAN_PLACE_BLOCK_AT = 114;
    private static final int ID_ON_BLOCK_PLACED = 115;
    private static final int ID_ON_BLOCK_DESTROYED_BY_PLAYER = 116;
    private static final int ID_ON_NEIGHBOR_BLOCK_CHANGE = 117;
    private static final int ID_ON_BLOCK_REMOVAL = 118;
    private static final int ID_ON_BLOCK_ADDED = 119;
    private static final int ID_GET_BLOCK_DESTROYING_SOUND = 120;
    private static final int ID_GET_BLOCK_PLACING_SOUND = 121;
    private static final int ID_SET_BLOCK_PLACING_SOUND = 122;
    private static final int ID_SET_BLOCK_DESTROYING_SOUND = 123;
    private static final int ID_TICK_BLOCK = 124;
    // ModLoader end

    protected Block(int id) {
        super(JsObject.OBJECT_PROTOTYPE); // ModLoader
        if (blocksList[id] != null) {
            throw new ComcraftException("Block ID is already in use! Id: " + id, null);
        } else {
            blocksList[id] = this;
            blockID = id;
        }
        // ModLoader start
        // Methods
        addNative("collidesWithPlayer", ID_COLLIDES_WITH_PLAYER, 0);
        addNative("doesBlockDestroyGrass", ID_DOES_BLOCK_DESTROY_GRASS, 0);
        addNative("isReplaceableBlock", ID_IS_REPLACEABLE_BLOCK, 0);
        addNative("isUpdatableBlock", ID_IS_UPDATABLE_BLOCK, 0);
        addNative("canBePieced", ID_CAN_BE_PIECED, 0);
        addNative("canBePiecedVertically", ID_CAN_BE_PIECED_VERTICALLY, 0);
        addNative("getBlockName", ID_GET_BLOCK_NAME, 0);
        addNative("setBlockName", ID_SET_BLOCK_NAME, 1);
        addNative("getIdDropped", ID_GET_ID_DROPPED, 0);
        addNative("getBlockTexture", ID_GET_BLOCK_TEXTURE, 5);
        addNative("isNormal", ID_IS_NORMAL, 0);
        addNative("getRenderType", ID_GET_RENDER_TYPE, 0);
        addNative("shouldSideBeRendered", ID_SHOULD_SIDE_BE_RENDERED, 5);
        addNative("canPlaceBlockOnSide", ID_CAN_PLACE_BLOCK_ON_SIDE, 5);
        addNative("canPlaceBlockAt", ID_CAN_PLACE_BLOCK_AT, 4);
        addNative("onBlockPlaced", ID_ON_BLOCK_PLACED, 5);
        addNative("onBlockDestroyedByPlayer", ID_ON_BLOCK_DESTROYED_BY_PLAYER, 5);
        addNative("onNeighborBlockChange", ID_ON_NEIGHBOR_BLOCK_CHANGE, 5);
        addNative("onBlockRemoval", ID_ON_BLOCK_REMOVAL, 4);
        addNative("onBlockAdded", ID_ON_BLOCK_ADDED, 4);
        addNative("getBlockDestroyingSound", ID_GET_BLOCK_DESTROYING_SOUND, 0);
        addNative("getBlockPlacingSound", ID_GET_BLOCK_PLACING_SOUND, 0);
        addNative("setBlockPlacingSound", ID_SET_BLOCK_PLACING_SOUND, 1);
        addNative("setBlockDestroyingSound", ID_SET_BLOCK_DESTROYING_SOUND, 1);
        addNative("tickBlock", ID_TICK_BLOCK, 4);
        // Properties
        JsArray arr_blocksList= new JsArray();for(int i=0;i<blocksList.length;i++){arr_blocksList.setObject(i,blocksList[i]);}
        addVar("blocksList", arr_blocksList);
        addVar("stone", stone);
        addVar("grass", grass);
        addVar("dirt", dirt);
        addVar("glass", glass);
        addVar("leaves", leaves);
        addVar("sand", sand);
        addVar("planks", planks);
        addVar("wood", wood);
        addVar("brick", brick);
        addVar("woolWhite", woolWhite);
        addVar("woolBlack", woolBlack);
        addVar("woolRed", woolRed);
        addVar("woolBlue", woolBlue);
        addVar("woolYellow", woolYellow);
        addVar("woolGreen", woolGreen);
        addVar("woolLightGray", woolLightGray);
        addVar("woolGray", woolGray);
        addVar("woolOrange", woolOrange);
        addVar("woolLime", woolLime);
        addVar("woolCyan", woolCyan);
        addVar("woolLightBlue", woolLightBlue);
        addVar("woolPurple", woolPurple);
        addVar("woolMagenta", woolMagenta);
        addVar("woolPink", woolPink);
        addVar("woolBrown", woolBrown);
        addVar("cobblestone", cobblestone);
        addVar("bookshelve", bookshelve);
        addVar("obsidian", obsidian);
        addVar("pumpkin", pumpkin);
        addVar("ice", ice);
        addVar("stoneBrick", stoneBrick);
        addVar("mossStone", mossStone);
        addVar("iron", iron);
        addVar("gold", gold);
        addVar("diamond", diamond);
        addVar("bedrock", bedrock);
        addVar("stoneSlab", stoneSlab);
        addVar("plankSlab", plankSlab);
        addVar("doubleStoneSlab", doubleStoneSlab);
        addVar("cobblestoneSlab", cobblestoneSlab);
        addVar("cactus", cactus);
        addVar("water", water);
        addVar("redFlower", redFlower);
        addVar("yellowFlower", yellowFlower);
        addVar("treePlant", treePlant);
        addVar("toadstool", toadstool);
        addVar("mushroom", mushroom);
        addVar("lava", lava);
        addVar("snow", snow);
        addVar("snowBlock", snowBlock);
        addVar("sandStone", sandStone);
        addVar("lapisLazuli", lapisLazuli);
        addVar("craftingTable", craftingTable);
        addVar("furnace", furnace);
        addVar("tnt", tnt);
        addVar("tntWeak", tntWeak);
        addVar("tntStrong", tntStrong);
        addVar("netherrack", netherrack);
        addVar("netherBrick", netherBrick);
        addVar("soulSand", soulSand);
        addVar("plankTitle", plankTitle);
        addVar("planks1", planks1);
        addVar("planks2", planks2);
        addVar("planks3", planks3);
        addVar("plankSlab1", plankSlab1);
        addVar("plankSlab2", plankSlab2);
        addVar("plankSlab3", plankSlab3);
        addVar("netherBrickSlab", netherBrickSlab);
        addVar("brickSlab", brickSlab);
        addVar("whiteWoolSlab", whiteWoolSlab);
        addVar("blackWoolSlab", blackWoolSlab);
        addVar("redWoolSlab", redWoolSlab);
        addVar("blueWoolSlab", blueWoolSlab);
        addVar("yellowWoolSlab", yellowWoolSlab);
        addVar("greenWoolSlab", greenWoolSlab);
        addVar("lightGreenWoolSlab", lightGreenWoolSlab);
        addVar("orangeWoolSlab", orangeWoolSlab);
        addVar("pinkWoolSlab", pinkWoolSlab);
        addVar("wood1", wood1);
        addVar("wood2", wood2);
        addVar("plankTitle1", plankTitle1);
        addVar("plankTitle2", plankTitle2);
        addVar("plankTitle3", plankTitle3);
        addVar("brickTitle", brickTitle);
        addVar("netherBrickTitle", netherBrickTitle);
        addVar("stoneTitle", stoneTitle);
        addVar("cobblestoneTitle", cobblestoneTitle);
        addVar("torch", torch);
        addVar("woodenDoor", woodenDoor);
        addVar("ironDoor", ironDoor);
        addVar("plankStairs", plankStairs);
        addVar("plankStairs1", plankStairs1);
        addVar("plankStairs2", plankStairs2);
        addVar("plankStairs3", plankStairs3);
        addVar("brickStairs", brickStairs);
        addVar("netherBrickStairs", netherBrickStairs);
        addVar("stoneStairs", stoneStairs);
        addVar("cobblestoneStairs", cobblestoneStairs);
        addVar("whiteWoolStairs", whiteWoolStairs);
        addVar("blackWoolStairs", blackWoolStairs);
        addVar("redWoolStairs", redWoolStairs);
        addVar("blueWoolStairs", blueWoolStairs);
        addVar("yellowWoolStairs", yellowWoolStairs);
        addVar("greenWoolStairs", greenWoolStairs);
        addVar("lightGreenWoolStairs", lightGreenWoolStairs);
        addVar("orangeWoolStairs", orangeWoolStairs);
        addVar("pinkWoolStairs", pinkWoolStairs);
        addVar("chest", chest);
        addVar("emoticon", emoticon);
        addVar("alphabet1", alphabet1);
        addVar("alphabet2", alphabet2);
        addVar("numbers", numbers);
        addVar("fencePlank", fencePlank);
        addVar("fencePlank2", fencePlank2);
        addVar("fencePlank3", fencePlank3);
        addVar("fencePlank4", fencePlank4);
        addVar("fenceStone", fenceStone);
        addVar("fenceBrick", fenceBrick);
        addVar("fenceNetherbrick", fenceNetherbrick);
        addVar("wheat", wheat);
        addVar("animalSheep", animalSheep);
        addVar("animalChicken", animalChicken);
        addVar("animalCow", animalCow);
        addVar("animalPig", animalPig);
        addVar("bed", bed);
        addVar("blockID", new Integer(blockID));
        // ModLoader end
    }

    protected Block(int id, int index) {
        this(id);
        blockIndexInTexture = index;
    }

    public Block() {
    	super(JsObject.OBJECT_PROTOTYPE);
    	blockID=0;
	}



    public boolean collidesWithPlayer() {
        return true;
    }
    
    public boolean doesBlockDestroyGrass() {
        return true;
    }

    public boolean isReplaceableBlock() {
        return false;
    }

    public boolean isUpdatableBlock() {
        return false;
    }

    public boolean canBePieced() {
        return true;
    }

    public boolean canBePiecedVertically() {
        return true;
    }

    public Transform getBlockTransform(World world, int x, int y, int z, Transform transform, int side) {
        return transform;
    }

    public VertexBuffer[][][][] getBlockVertexBufferPieced(World world, int x, int y, int z) {
        return ModelPieceBlock.vertexBuffer;
    }

    public VertexBuffer[] getBlockVertexBufferSided(World world, int x, int y, int z) {
        return getBlockVertexBufferPieced(world, x, y, z)[0][0][0];
    }

    public IndexBuffer getBlockIndexBuffer() {
        return ModelPieceBlock.indexBuffer;
    }

    protected void initializeBlock() {
    }

    public String getBlockName() {
        return blockName;
    }

    public Block setBlockName(String name) {
        blockName = name;
        return this;
    }

    public int getIdDropped() {
        return blockID;
    }

    public int getBlockTexture(World world, int x, int y, int z, int side) {
        return blockIndexInTexture;
    }

    public boolean isNormal() {
        return true;
    }

    public int getRenderType() {
        return 1;
    }

    public boolean shouldSideBeRendered(World world, int x, int y, int z, int side) {
        return !world.isBlockNormal(x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1f, y + 1f, z + 1f);
    }

    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        return canPlaceBlockAt(world, x, y, z);
    }

    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int i = world.getBlockID(x, y, z);
        return i == 0 || Block.blocksList[i].isReplaceableBlock();
    }

    public void onBlockPlaced(World world, int x, int y, int z, int side) {
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityPlayer entityPlayer) {
    }

    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata) {
    }

    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, InvItemStack itemStack) {
        return blockActivated(world, x, y, z, entityplayer);
    }
    
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer entityplayer) {
        return false;
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID) {
    }

    public void onBlockRemoval(World world, int x, int y, int z) {
    }

    public void onBlockAdded(World world, int x, int y, int z) {
    }

    public String getBlockDestroyingSound() {
        return blockDestroyingSound;
    }

    public String getBlockPlacingSound() {
        return blockPlacingSound;
    }

    public void setBlockPlacingSound(String path) {
        blockPlacingSound = path;
    }

    public void setBlockDestroyingSound(String path) {
        blockDestroyingSound = path;
    }

    public void tickBlock(World world, int x, int y, int z) {
    }

    public int[] getUsedTexturesList() {
        return new int[]{blockIndexInTexture};
    }

    public Node getBlockModel(World world, int x, int y, int z) {
        return null;
    }

    static {
        blocksList = new Block[256];

        stone = new Block(1, 1).setBlockName("stone");
        grass = new BlockGrass(2, 3, 0, 2).setBlockName("grass");
        dirt = new BlockDirt(3, 2).setBlockName("dirt");
        glass = new BlockGlass(4, 49).setBlockName("glass");
        leaves = new Block(5, 53).setBlockName("leaves");
        sand = new Block(6, 18).setBlockName("sand");
        planks = new Block(7, 4).setBlockName("planks");
        wood = new BlockSidesTopBottom(8, 20, 21, 21).setBlockName("wood");
        brick = new Block(9, 7).setBlockName("brick");
        woolWhite = new Block(10, 64).setBlockName("wool");
        woolBlack = new Block(11, 113).setBlockName("wool");
        woolRed = new Block(12, 129).setBlockName("wool");
        woolBlue = new Block(13, 177).setBlockName("wool");
        woolYellow = new Block(14, 162).setBlockName("wool");
        woolGreen = new Block(15, 145).setBlockName("wool");
        cobblestone = new Block(16, 16).setBlockName("cobblestone");
        obsidian = new Block(17, 37).setBlockName("obsidian");
        bookshelve = new BlockSidesTop(18, 35, 4).setBlockName("bookshelve");
        pumpkin = new BlockFurnance(19, 119, 118, 102, 102, 120).setBlockName("pumpkin");
        ice = new BlockIce(20, 67).setBlockName("ice");
        stoneBrick = new Block(21, 54).setBlockName("stone brick");
        mossStone = new Block(22, 36).setBlockName("moss stone");
        iron = new Block(23, 22).setBlockName("block of iron");
        gold = new Block(24, 23).setBlockName("block of gold");
        diamond = new Block(25, 24).setBlockName("block of diamond");
        bedrock = new Block(26, 17).setBlockName("bedrock");
        stoneSlab = new BlockSlab(27, 5, 6).setBlockName("stone slab");
        plankSlab = new BlockSlab(28, 4, 4).setBlockName("plank slab");
        doubleStoneSlab = new BlockSidesTop(29, 5, 6).setBlockName("stone slab");
        cobblestoneSlab = new BlockSlab(30, 16, 16).setBlockName("cobblestone slab");
        cactus = new BlockSidesTop(31, 70, 69).setBlockName("cactus");
        woolLightGray = new Block(32, 225).setBlockName("wool");
        woolGray = new Block(33, 114).setBlockName("wool");
        woolOrange = new Block(34, 210).setBlockName("wool");
        woolLime = new Block(35, 146).setBlockName("wool");
        woolCyan = new Block(36, 209).setBlockName("wool");
        woolLightBlue = new Block(37, 178).setBlockName("wool");
        woolPurple = new Block(38, 193).setBlockName("wool");
        woolMagenta = new Block(39, 194).setBlockName("wool");
        woolPink = new Block(40, 130).setBlockName("wool");
        woolBrown = new Block(41, 161).setBlockName("wool");
        water = new BlockLiquid(42, 205).setWaterFall(1).setBlockName("water");
        redFlower = new BlockFlower(43, 12);
        yellowFlower = new BlockFlower(44, 13);
        treePlant = new BlockTreePlant(45, 15);
        toadstool = new BlockFlower(46, 28);
        mushroom = new BlockFlower(47, 29);
        lava = new BlockLiquid(48, 237).setWaterFall(2);
        snow = new BlockSidesTopBottom(49, 68, 66, 2);
        snowBlock = new Block(50, 66);
        sandStone = new Block(51, 192);
        lapisLazuli = new Block(52, 144);
        craftingTable = new BlockCraftingTable(53, 59, 60, 43, 43);
        furnace = new BlockFurnance(54, 44, 45, 62, 62, 61);
        tnt = new BlockTNT(55, 8, 9, 10, 256, 257, 258, 3, 3);
        netherrack = new Block(56, 103);
        netherBrick = new Block(57, 224);
        soulSand = new Block(58, 104);
        plankTitle = new BlockTitle(60, 4);
        planks1 = new Block(61, 198).setBlockName("planks");
        planks2 = new Block(62, 199).setBlockName("planks");
        planks3 = new Block(63, 214).setBlockName("planks");
        plankSlab1 = new BlockSlab(64, 198, 198);
        plankSlab2 = new BlockSlab(65, 199, 199);
        plankSlab3 = new BlockSlab(66, 214, 214);
        netherBrickSlab = new BlockSlab(67, 224, 224);
        brickSlab = new BlockSlab(68, 7, 7);
        wood1 = new BlockSidesTopBottom(69, 116, 21, 21);
        wood2 = new BlockSidesTopBottom(70, 117, 21, 21);
        plankTitle1 = new BlockTitle(72, 198);
        plankTitle2 = new BlockTitle(73, 199);
        plankTitle3 = new BlockTitle(74, 214);
        brickTitle = new BlockTitle(75, 7);
        netherBrickTitle = new BlockTitle(76, 224);
        stoneTitle = new BlockTitle(77, 6);
        torch = new BlockTorch(78, 80);
        woodenDoor = new BlockDoor(79, 81, 97);
        ironDoor = new BlockDoor(80, 82, 98);
        cobblestoneTitle = new BlockTitle(81, 16);
//        gus = new BlockModel(82, "/models/gus.m3g");
        plankStairs = new BlockStairs(82, 4);
        plankStairs1 = new BlockStairs(83, 198);
        plankStairs2 = new BlockStairs(84, 199);
        plankStairs3 = new BlockStairs(85, 214);
        brickStairs = new BlockStairs(86, 7);
        netherBrickStairs = new BlockStairs(87, 224);
        stoneStairs = new BlockStairs(88, 6);
        cobblestoneStairs = new BlockStairs(89, 16);
        whiteWoolStairs = new BlockStairs(90, 64);
        blackWoolStairs = new BlockStairs(91, 113);
        redWoolStairs = new BlockStairs(92, 129);
        blueWoolStairs = new BlockStairs(93, 177);
        yellowWoolStairs = new BlockStairs(94, 162);
        greenWoolStairs = new BlockStairs(95, 145);
        lightGreenWoolStairs = new BlockStairs(96, 146);
        orangeWoolStairs = new BlockStairs(97, 210);
        pinkWoolStairs = new BlockStairs(98, 130);
        whiteWoolSlab = new BlockSlab(99, 64);
        blackWoolSlab = new BlockSlab(100, 113);
        redWoolSlab = new BlockSlab(101, 129);
        blueWoolSlab = new BlockSlab(102, 177);
        yellowWoolSlab = new BlockSlab(103, 162);
        greenWoolSlab = new BlockSlab(104, 145);
        lightGreenWoolSlab = new BlockSlab(105, 146);
        orangeWoolSlab = new BlockSlab(106, 210);
        pinkWoolSlab = new BlockSlab(107, 130);
        tntWeak = new BlockTNT(108, 265, 266, 267, 281, 282, 283, 1, 1);
        tntStrong = new BlockTNT(109, 268, 269, 270, 284, 285, 286, 6, 5);
        chest = new BlockFurnance(110, 274, 273, 272, 272, 275);
        emoticon = new BlockEmoticon(111, 280, 272, 272, 272, new int[]{279, 275, 276, 277, 278, 271, 287});
        alphabet1 = new BlockAlphabet(112, 288, 272, 272, 272, new int[]{288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300});
        alphabet2 = new BlockAlphabet(113, 301, 272, 272, 272, new int[]{301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313});
        numbers = new BlockAlphabet(114, 314, 272, 272, 272, new int[]{314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327});
        fencePlank = new BlockFence(115, 328);
        fencePlank2 = new BlockFence(116, 329);
        fencePlank3 = new BlockFence(117, 330);
        fencePlank4 = new BlockFence(118, 331);
        wheat = new BlockWheat(119, new int[]{88, 89, 90, 91, 92, 93, 94, 95});
        fenceNetherbrick = new BlockFence(120, 332);
        fenceStone = new BlockFence(121, 333);
        fenceBrick = new BlockFence(122, 334);
        animalSheep = new BlockAnimal(123, 337, 336, 338, 336, 336);
        bed = new BlockBed(124, 135, 134, 149, 152, 150, 151, 335, 351);
        animalChicken = new BlockAnimal(125, 339, 341, 340, 357, 357);
        animalCow = new BlockAnimal(126, 342, 344, 343, 344, 344);
        animalPig = new BlockAnimal(127, 345, 348, 346, 347, 347);
        
        InvItem.itemsList[stoneSlab.blockID] = new InvItemSlab(stoneSlab.blockID - 256, doubleStoneSlab);
        InvItem.itemsList[plankSlab.blockID] = new InvItemSlab(plankSlab.blockID - 256, planks);
        InvItem.itemsList[cobblestoneSlab.blockID] = new InvItemSlab(cobblestoneSlab.blockID - 256, cobblestone);
        InvItem.itemsList[plankSlab1.blockID] = new InvItemSlab(plankSlab1.blockID - 256, planks1);
        InvItem.itemsList[plankSlab2.blockID] = new InvItemSlab(plankSlab2.blockID - 256, planks2);
        InvItem.itemsList[plankSlab3.blockID] = new InvItemSlab(plankSlab3.blockID - 256, planks3);
        InvItem.itemsList[netherBrickSlab.blockID] = new InvItemSlab(netherBrickSlab.blockID - 256, netherBrick);
        InvItem.itemsList[brickSlab.blockID] = new InvItemSlab(brickSlab.blockID - 256, brick);

        InvItem.itemsList[woodenDoor.blockID] = new InvItemBlock(woodenDoor.blockID - 256, 43);
        InvItem.itemsList[ironDoor.blockID] = new InvItemBlock(ironDoor.blockID - 256, 44);

        InvItem.itemsList[water.blockID] = new InvItemBlock(water.blockID - 256, 75);
        InvItem.itemsList[lava.blockID] = new InvItemBlock(lava.blockID - 256, 76);
        
        InvItem.itemsList[bed.blockID] = new InvItemBlock(bed.blockID - 256, 45);

        for (int i = 0; i < 256; i++) {
            if (blocksList[i] == null) {
                continue;
            }

            if (InvItem.itemsList[i] == null) {
                InvItem.itemsList[i] = new InvItemBlock(i - 256);
                blocksList[i].initializeBlock();
            }
        }
    }
    // ModLoader start
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch(id) {
            case ID_COLLIDES_WITH_PLAYER:
                stack.setBoolean(sp,collidesWithPlayer());
                break;
            case ID_DOES_BLOCK_DESTROY_GRASS:
                stack.setBoolean(sp,doesBlockDestroyGrass());
                break;
            case ID_IS_REPLACEABLE_BLOCK:
                stack.setBoolean(sp,isReplaceableBlock());
                break;
            case ID_IS_UPDATABLE_BLOCK:
                stack.setBoolean(sp,isUpdatableBlock());
                break;
            case ID_CAN_BE_PIECED:
                stack.setBoolean(sp,canBePieced());
                break;
            case ID_CAN_BE_PIECED_VERTICALLY:
                stack.setBoolean(sp,canBePiecedVertically());
                break;
            case ID_GET_BLOCK_NAME:
                stack.setObject(sp,getBlockName());
                break;
            case ID_SET_BLOCK_NAME:
                stack.setObject(sp,setBlockName(stack.getString(sp+2)));
                break;
            case ID_GET_ID_DROPPED:
                stack.setInt(sp,getIdDropped());
                break;
            case ID_GET_BLOCK_TEXTURE:
                stack.setInt(sp,getBlockTexture((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6)));
                break;
            case ID_IS_NORMAL:
                stack.setBoolean(sp,isNormal());
                break;
            case ID_GET_RENDER_TYPE:
                stack.setInt(sp,getRenderType());
                break;
            case ID_SHOULD_SIDE_BE_RENDERED:
                stack.setBoolean(sp,shouldSideBeRendered((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6)));
                break;
            case ID_CAN_PLACE_BLOCK_ON_SIDE:
                stack.setBoolean(sp,canPlaceBlockOnSide((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6)));
                break;
            case ID_CAN_PLACE_BLOCK_AT:
                stack.setBoolean(sp,canPlaceBlockAt((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5)));
                break;
            case ID_ON_BLOCK_PLACED:
                onBlockPlaced((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6));
                break;
            case ID_ON_BLOCK_DESTROYED_BY_PLAYER:
                onBlockDestroyedByPlayer((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6));
                break;
            case ID_ON_NEIGHBOR_BLOCK_CHANGE:
                onNeighborBlockChange((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5), stack.getInt(sp+6));
                break;
            case ID_ON_BLOCK_REMOVAL:
                onBlockRemoval((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5));
                break;
            case ID_ON_BLOCK_ADDED:
                onBlockAdded((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5));
                break;
            case ID_GET_BLOCK_DESTROYING_SOUND:
                stack.setObject(sp,getBlockDestroyingSound());
                break;
            case ID_GET_BLOCK_PLACING_SOUND:
                stack.setObject(sp,getBlockPlacingSound());
                break;
            case ID_SET_BLOCK_PLACING_SOUND:
                setBlockPlacingSound(stack.getString(sp+2));
                break;
            case ID_SET_BLOCK_DESTROYING_SOUND:
                setBlockDestroyingSound(stack.getString(sp+2));
                break;
            case ID_TICK_BLOCK:
                tickBlock((World) stack.getObject(sp+2), stack.getInt(sp+3), stack.getInt(sp+4), stack.getInt(sp+5));
                break;

            default:
                super.evalNative(id, stack, sp, parCount);
        }
    }
    // ModLoader end
}
