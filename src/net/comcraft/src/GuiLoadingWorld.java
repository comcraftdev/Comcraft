package net.comcraft.src;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class GuiLoadingWorld extends GuiScreen {

    public GuiLoadingWorld() {
        super(null);
    }

    public boolean getSkipRender() {
        return false;
    }
    
    public void handleInput() {
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void customDrawScreen() {
        cc.loadingScreen.setProgress((float) cc.world.chunkProvider.getLoadedChunksNum() / (cc.world.chunkProvider.getChunksQueueNum() + cc.world.chunkProvider.getLoadedChunksNum()));

        if (cc.world.chunkProvider.getChunksQueueNum() == 0 && cc.world.chunkProvider.getLoadedChunksNum() != 0) {
            cc.displayGuiScreen(null);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                //#debug
//#                 ex.printStackTrace();
            }
        }
    }

    protected void initGui() {
        cc.loadingScreen.displayLoadingScreen(cc.langBundle.getText("GuiLoadingWorld.loadingText"));
    }

    protected void handleGuiAction(GuiButton guiButton) {
    }
}