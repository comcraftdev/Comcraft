package net.comcraft.src;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Texture2D;
import net.comcraft.client.Comcraft;

public final class TextureManager {

    private final Comcraft cc;
    private Hashtable loadedImagesList;
    private Texture2D[] terrainTexturesList;
    private Image[] itemImagesList;

    public TextureManager(Comcraft cc) {
        this.cc = cc;
        loadedImagesList = new Hashtable(50);
        terrainTexturesList = new Texture2D[512];
        itemImagesList = new Image[512];
    }

    public void releaseTextures() {
        loadedImagesList.clear();
        terrainTexturesList = null;
    }

    public void reloadTextures() {
        loadedImagesList.clear();
        loadAllImages();

        loadTerrainTextures();
        loadItemTextures();
    }

    private Vector getUsedItemTexturesList() {
        Vector list = new Vector(256);

        for (int n = 0; n < InvItem.itemsList.length; ++n) {
            InvItem item = InvItem.itemsList[n];

            if (item != null) {
                int tex = item.getIconIndex();

                Integer integer = new Integer(tex);

                list.addElement(integer);
            }
        }

        return list;
    }

    private Vector getUsedTerrainTexturesList() {
        Vector list = new Vector(512);

        for (int n = 0; n < Block.blocksList.length; ++n) {
            Block block = Block.blocksList[n];

            if (block != null) {
                int[] tex = block.getUsedTexturesList();

                for (int i = 0; i < tex.length; ++i) {
                    Integer integer = new Integer(tex[i]);

                    list.addElement(integer);
                }
            }
        }

        return list;
    }

    public static Image getResizedImage(Image image, int resizedWidth, int resizedHeight) {
        int[] in, out;

        int width = image.getWidth(), height = image.getHeight();
        in = new int[width];

        int i = 0;
        int dy, dx;
        out = new int[resizedWidth * resizedHeight];

        for (int y = 0; y < resizedHeight; y++) {
            dy = y * height / resizedHeight;

            image.getRGB(in, 0, width, 0, dy, width, 1);

            for (int x = 0; x < resizedWidth; x++) {
                dx = x * width / resizedWidth;

                out[(resizedWidth * y) + x] = in[dx];
            }
        }

        Image resized = Image.createRGBImage(out, resizedWidth, resizedHeight, true);

        return resized;
    }

    private void loadItemTextures() {
        Image items = null;
        Image items2 = null;

        Vector usedTextures = getUsedItemTexturesList();

        try {
            items = Image.createImage(cc.texturePackList.getResourceAsStream("gui/items.png"));
            items2 = Image.createImage(cc.texturePackList.getResourceAsStream("gui/items_cc.png"));
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("Can't load terrain image!", ex);
        }

        int size = items.getWidth() / 16;

        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                if (!usedTextures.contains(new Integer(y * 16 + x))) {
                    continue;
                }

                Image image = Image.createImage(items, x * size, y * size, size, size, Touch.isTouchSupported() ? Sprite.TRANS_ROT90 : Sprite.TRANS_NONE);

                Image resized = getResizedImage(image, 32, 32);

                Image background = Image.createImage(50, 50);

                background.getGraphics().drawImage(resized, (50 - resized.getWidth()) / 2, (50 - resized.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);

                itemImagesList[y * 16 + x] = background;
            }
        }
        
        size = items2.getWidth() / 16;
        
        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                if (!usedTextures.contains(new Integer(256 + y * 16 + x))) {
                    continue;
                }

                Image image = Image.createImage(items2, x * size, y * size, size, size, Touch.isTouchSupported() ? Sprite.TRANS_ROT90 : Sprite.TRANS_NONE);

                Image resized = getResizedImage(image, 32, 32);

                Image background = Image.createImage(50, 50);

                background.getGraphics().drawImage(resized, (50 - resized.getWidth()) / 2, (50 - resized.getHeight()) / 2, Graphics.TOP | Graphics.LEFT);

                itemImagesList[256 + y * 16 + x] = background;
            }
        }
    }

    private void loadTerrainTextures() {
        Image terrain = null;
        Image terrain1 = null;

        Vector usedTextures = getUsedTerrainTexturesList();

        try {
            terrain = Image.createImage(cc.texturePackList.getResourceAsStream("terrain.png"));
            terrain1 = Image.createImage(cc.texturePackList.getResourceAsStream("terrain_cc.png"));
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException("Can't load terrain image!", ex);
        }

        int size = terrain.getWidth() / 16;

        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                if (!usedTextures.contains(new Integer(y * 16 + x))) {
                    continue;
                }

                Image image = Image.createImage(terrain, x * size, y * size, size, size, Sprite.TRANS_MIRROR);

                Image2D image2D = new Image2D(Image2D.RGBA, image);

                Texture2D texture2D = new Texture2D(image2D);
                texture2D.setFiltering(Texture2D.FILTER_BASE_LEVEL, Texture2D.FILTER_NEAREST);
                texture2D.setWrapping(Texture2D.WRAP_REPEAT, Texture2D.WRAP_REPEAT);
                texture2D.setBlending(Texture2D.FUNC_REPLACE);

                terrainTexturesList[y * 16 + x] = texture2D;
            }
        }

        size = terrain1.getWidth() / 16;

        for (int y = 0; y < 16; ++y) {
            for (int x = 0; x < 16; ++x) {
                if (!usedTextures.contains(new Integer(256 + y * 16 + x))) {
                    continue;
                }

                Image image = Image.createImage(terrain1, x * size, y * size, size, size, Sprite.TRANS_MIRROR);

                Image2D image2D = new Image2D(Image2D.RGBA, image);

                Texture2D texture2D = new Texture2D(image2D);
                texture2D.setFiltering(Texture2D.FILTER_BASE_LEVEL, Texture2D.FILTER_NEAREST);
                texture2D.setWrapping(Texture2D.WRAP_REPEAT, Texture2D.WRAP_REPEAT);
                texture2D.setBlending(Texture2D.FUNC_REPLACE);

                terrainTexturesList[256 + y * 16 + x] = texture2D;
            }
        }
    }

    private void loadAllImages() {
        loadImage("gui/background.png");
        loadImage("gui/black.png");
        loadImage("gui/file_icon.png");
        loadImage("gui/button.png");
        loadImage("gui/button_small.png");
        loadImage("gui/slot.png");
        loadImage("gui/slot_selection.png");
        loadImage("gui/arrow_key.png");
        loadImage("gui/pointer.png");
        loadImageLandscape("gui/slot_more.png");
        loadImage("gui/inventory_landscape.png");
        loadImage("gui/inventory_normal.png");
        loadImage("gui/inventory_slot.png");
        loadImage("gui/inventory_slot_selection.png");
        loadImage("gui/button_exit.png");
        loadImage("gui/comcraft_logo.png");
        loadImageLandscape("gui/loading_sprite.png");
//        loadImage("gui/button_load_all.png");
        loadImage("gui/button_quick_menu.png");
        loadImage("gui/button_screenshot.png");
        loadImageLandscape("gui/locked_item.png");
    }

    private void loadImage(String path) {
        Image image = null;

        try {
            image = Image.createImage(cc.texturePackList.getResourceAsStream(path));
        } catch (IOException ex) {
            throw new ComcraftException("Can't load texture from texturepack: " + path, ex);
        }

        loadedImagesList.put(path, image);
    }

    private void loadImageLandscape(String path) {
        Image image = null;

        int transform = Touch.isTouchSupported() ? Sprite.TRANS_ROT90 : Sprite.TRANS_NONE;

        try {
            Image tempImage = Image.createImage(cc.texturePackList.getResourceAsStream(path));

            image = Image.createImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight(), transform);
        } catch (IOException ex) {
            throw new ComcraftException("Can't load texture from texturepack: " + path, ex);
        }

        loadedImagesList.put(path, image);
    }

    public Texture2D getTerrainTexture(int index) {
        return terrainTexturesList[index];
    }

    public Image getItemTexture(int index) {
        if (index >= 512) {
            return cc.render.renderBlockPreview.getBlockPreviewImage(index - 512);
        } else {
            return itemImagesList[index];
        }
    }

    public Image getImage(String path) {
        Image image = (Image) loadedImagesList.get(path);

        if (image == null) {
            try {
                //#debug
//#                 System.out.println("image not found: " + path);

                image = Image.createImage(cc.texturePackList.getResourceAsStream(path));

                return image;
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
                throw new ComcraftException(ex);
            }
        } else {
            return image;
        }
    }
}
