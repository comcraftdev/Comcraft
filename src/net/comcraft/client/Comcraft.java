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
package net.comcraft.client;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.comcraft.src.*;

public final class Comcraft implements Runnable {

    public final CanvasComcraft ccCanvas;
    public final ComcraftMIDlet comcraftMIDlet;
    private long lastTime;
    public Render render;
    public GuiScreen currentScreen;
    public GuiIngame guiIngame;
    public LoadingScreen loadingScreen;
    public volatile boolean running;
    public volatile boolean isGamePaused;
    public static int screenWidth = 240;
    public static int screenHeight = 320;
    public Graphics g;
    public TexturePackList texturePackList;
    public ModLoader modLoader;
    public TextureManager textureProvider;
    public Settings settings;
    public WorldLoader worldLoader;
    public World world;
    public EntityPlayer player;
    public volatile float dt;
    public volatile int tickTime;
    public PlayerManager playerManager;
    public long currentTime;
    private InputManager movementInput;
    public HelloWords helloWords;
    public LangBundle langBundle;
    public MusicPlayer musicPlayer;

    public Comcraft(ComcraftMIDlet comcraftMIDlet, CanvasComcraft ccCanvas) {
        this.comcraftMIDlet = comcraftMIDlet;
        this.ccCanvas = ccCanvas;
        ccCanvas.setComcraft(this);
        running = true;
        isGamePaused = true;
        texturePackList = null;
        settings = null;
        g = null;
        currentScreen = null;
        textureProvider = null;
        modLoader=new ModLoader(this);
        textureProvider = new TextureManager(this);
        texturePackList = new TexturePackList(this);
        world = null;
        guiIngame = null;
        render = null;
        loadingScreen = new LoadingScreen(this);
        langBundle = new LangBundle();
        render = new Render(this);
        musicPlayer = new MusicPlayer(this);
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    private void initScreenSize() {
        int width = ccCanvas.getWidth();
        int height = ccCanvas.getHeight();

        int[][] tab = {{480, 800}, {480, 640}, {360, 640}, {360, 480}, {320, 480}, {320, 240}, {240, 400}, {240, 320}, {176, 220}};

        for (int n = 0; n < tab.length; ++n) {
            if (width >= tab[n][0] && height >= tab[n][1]) {
                screenWidth = tab[n][0];
                screenHeight = tab[n][1];

                break;
            }
        }

        System.out.println("Screen width: " + screenWidth);
        System.out.println("Screen height: " + screenHeight);
    }

    public void openUrl(String url) {
        comcraftMIDlet.openUrl(url);
    }

    private void updateDt() {
        currentTime = System.currentTimeMillis();

        tickTime = (int) (currentTime - lastTime);

        dt = tickTime / 1000f;

        lastTime = currentTime;
    }

    private void setDesiredFont() {
        Font font = Font.getFont(0, 0, 0);
        g.setFont(font);
    }

    public void startGame() {
        g = ccCanvas.getGraphics();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        initScreenSize();

        g.setClip(0, 0, screenWidth, screenHeight);

        g.setColor(0x000000);
        g.fillRect(0, 0, screenWidth, screenHeight);

        setDesiredFont();
        settings = new Settings(this);

        loadScreen();

        langBundle.setDefaultMap("/lang/en.lng");

        ModelsList.initModelList();

        settings.loadOptions();

        modLoader.initMods();

        langBundle.loadBundle(settings.language);

        if (!settings.getComcraftFileSystem().isAvailable()) {
            displayGuiScreen(new GuiSelectPath(currentScreen));
        }

        render.initRender();

        worldLoader = new WorldLoader(this);

        texturePackList.updateAvailableTexturePacks();

        textureProvider.reloadTextures();
        render.reloadRender();

        helloWords = new HelloWords(this);
        musicPlayer.loadMusicList();
        movementInput = new InputManager(this);

        if (currentScreen == null) {
            displayGuiScreen(new GuiMainMenu());
        }

        

        showScreenVisit();
    }

    public void loadScreen() {
        GuiScreen guiScreen = new GuiLoadingScreen();

        guiScreen.initGuiScreen(this);
        guiScreen.drawScreen();

        flushGraphics();
    }

    private void showScreenVisit() {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, screenWidth, screenHeight);

        g.setColor(0, 0, 0);
        g.drawString("Visit:", screenWidth / 2, screenHeight / 2 - 10, Graphics.HCENTER | Graphics.TOP);//visit:
        g.drawString("comcraft-game.blogspot.com", screenWidth / 2, screenHeight / 2 + 10, Graphics.HCENTER | Graphics.TOP);//com-craft.blogspot.com

        flushGraphics();

        try {
            Thread.sleep(3500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            throw new ComcraftException(ex);
        }
    }

    public void flushGraphics() {
        ccCanvas.flushGraphics();
    }

    public void shutdown() {
        settings.saveOptions();
        running = false;
        shutdownComcraftMIDlet();
    }

    private void shutdownComcraftMIDlet() {
        comcraftMIDlet.notifyDestroyed();
    }

    public String getMIDletSize() {
        if (comcraftMIDlet.getAppProperty("MIDlet-Jar-Size") == null) {
            throw new ComcraftException("MIDlet-Jar-Size error", null);
        }

        return this.comcraftMIDlet.getAppProperty("MIDlet-Jar-Size");
    }

    public void run() {
        try {
            startGame();
        } catch (SecurityException ex) {
            //#debug debug
//#             ex.printStackTrace();
            displayGuiScreen(new GuiDialog(new GuiDialogHost() {
                public void dialogAction() {
                    shutdown();
                }
            }, langBundle.getText("GuiDialog.readWriteFilesError")));
        } catch (ComcraftException ex) {
            //#debug debug
//#             ex.printStackTrace();
            displayGuiScreen(new GuiError(ex));
        } catch (OutOfMemoryError error) {
            //#debug debug
//#             error.printStackTrace();
            outOfMemoryErrorAction(error);
        } catch (Exception ex) {
            //#debug debug
//#             ex.printStackTrace();
            displayGuiScreen(new GuiError(ex));
        }

        while (running) {
            try {
                if (ccCanvas.visibile) {
                    runTick();
                }
            } catch (SecurityException ex) {
                //#debug debug
//#                 ex.printStackTrace();
                displayGuiScreen(new GuiDialog(new GuiDialogHost() {
                    public void dialogAction() {
                        shutdown();
                    }
                }, langBundle.getText("GuiDialog.readWriteFilesError")));
            } catch (ComcraftException ex) {
                //#debug debug
//#                 ex.printStackTrace();
                displayGuiScreen(new GuiError(ex));
            } catch (OutOfMemoryError error) {
                //#debug debug
//#                 error.printStackTrace();
                outOfMemoryErrorAction(error);
            } catch (Exception ex) {
                //#debug debug
//#                 ex.printStackTrace();

                if (world != null) {
                    world.saveWorld(loadingScreen);
                }

                displayGuiScreen(new GuiError(ex));
            }
        }
    }

    private void outOfMemoryErrorAction(OutOfMemoryError error) {
        if (settings.ignoreOutOfMemory) {
            return;
        }

        textureProvider.releaseTextures();
        render.releaseRender();

        System.gc();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            //#debug
//#             ex.printStackTrace();
        }

        if (world != null && !isGamePaused) {
            world.saveWorld(loadingScreen);
            world.onWorldEnd();

            world = null;
        }

        displayGuiScreen(new GuiOutOfMemory(error));
    }

    private void processInput() {
        Touch.tickTouch();
        Keyboard.tickKeyboard();

        boolean checked = false;

        while (!checked || !Keyboard.isQueueEmpty() || !Touch.isQueueEmpty()) {
            checked = true;

            if (currentScreen != null) {
                currentScreen.handleInput();
            }

            if (!isGamePaused) {
                guiIngame.handleInput();
                movementInput.handleInput();
            }

            Keyboard.tickKeyboard();
            Touch.tickTouch();
        }
    }

    private void runTick() {
        updateDt();

        musicPlayer.tickMusicPlayer();

        if (!isGamePaused) {
            player.onLivingUpdate(dt);

            if (settings.memorySaveMode) {
                world.chunkProvider.releaseUnusedChunks();
            }

            if (settings.autosaveTime != -1) {
                world.checkAutosave();
            }
        }

        processInput();

        if (!isGamePaused) {
            world.getWorldUpdater().updateWorld((int) (dt * 1000));

            render.renderEffects.tickEffects();
        }

        render.renderAll();
    }

    /*
     * 0 - left click, 1 - right click
     */
    public void onClick(int mode, RayObjectPosition rayObject) {
        if (rayObject == null) {
            return;
        }

        if (rayObject.typeOfHit == RayObjectPosition.TILE) {
            if (mode == 0) {
                playerManager.clickBlock(rayObject.blockX, rayObject.blockY, rayObject.blockZ, rayObject.sideHit);
            } else {
                playerManager.onPlayerRightClick(player, world, player.inventory.getSelectedItemStack(), rayObject.blockX, rayObject.blockY, rayObject.blockZ, rayObject.sideHit);
            }
        }
    }

    public void displayGuiScreen(GuiScreen newGuiScreen) {
        if (currentScreen != null) {
            currentScreen.onScreenClosed();
        }

        currentScreen = newGuiScreen;

        if (currentScreen != null) {
            if (!currentScreen.isScreenInitialized()) {
                currentScreen.initGuiScreen(this);
            }

            currentScreen.onScreenDisplay();

            if (currentScreen.doesGuiPauseGame()) {
                isGamePaused = true;
            }
        }
    }

    public void vibrate(int time) {
        if (settings.vibrations) {
            comcraftMIDlet.display.vibrate(time);
        }
    }

    public void startWorld(WorldSaveType worldInfo) {
        guiIngame = new GuiIngame(this);
        guiIngame.initIngameGui();

        isGamePaused = false;

        SaveInfo saveHandler = worldLoader.getSaveLoader(worldInfo.getWorldName());

        player = playerManager.createPlayer();

        world = new World(this, saveHandler, worldInfo);

        displayGuiScreen(new GuiLoadingWorld());
    }

    public void endWorld() {
        isGamePaused = true;

        displayGuiScreen(new GuiMainMenu());

        if (world != null) {
            world.saveWorld(loadingScreen);
            world.onWorldEnd();

            world = null;
        }
    }

    public static String getPlatform() {
        return System.getProperty("microedition.platform");
    }

    public static String getImei() {
        String imei = null;

        try {
            if (imei == null) {
                imei = System.getProperty("imei");
            }
            if (imei == null) {
                imei = System.getProperty("phone.imei");
            }
            if (imei == null) {
                imei = System.getProperty("com.nokia.IMEI");
            }
            if (imei == null) {
                imei = System.getProperty("com.nokia.mid.imei");
            }
            if (imei == null) {
                imei = System.getProperty("com.sonyericsson.imei");
            }
            if (imei == null) {
                imei = System.getProperty("IMEI");
            }
            if (imei == null) {
                imei = System.getProperty("com.motorola.IMEI");
            }
            if (imei == null) {
                imei = System.getProperty("com.samsung.imei");
            }
            if (imei == null) {
                imei = System.getProperty("com.siemens.imei");
            }
            if (imei == null) {
                imei = System.getProperty("com.lge.imei");
            }
            if (imei == null) {
                imei = System.getProperty("sim.imei");
            }
            if (imei == null) {
                imei = System.getProperty("microedition.imei");
            }
        } catch (SecurityException ex) {
            //#debug
//#             ex.printStackTrace();
        }

        if (imei == null) {
            imei = "Unknown";
        }

        return imei;
    }
}