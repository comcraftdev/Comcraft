package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

import net.comcraft.client.Comcraft;

// For reference, order of method calling when cc.endWorld called:
// saveWorldInfo
// saveChunks
// onChunkLoaderEnd

public class ServerGame implements LevelInfo, ChunkLoader {
    private SocketConnection client;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Comcraft cc;
    private boolean isAlive;
    private boolean ignoreChange = false;
    private boolean closing = false;
    private RecieveThread reciever;
    private SendThread sender;
    private WorldInfo worldInfo;
    private GuiServerMessage guiscr;
    private Chunk chunk = null;
    private World world;

    public ServerGame(Comcraft cc, String ip) {
        this.cc = cc;
        guiscr = new GuiServerMessage(cc.currentScreen, this);
        cc.displayGuiScreen(guiscr);
        if (connect(ip)) {
            guiscr.setText(cc.langBundle.getText("ServerGame.login"));
            sender.sendPacket(new PacketLogin(Comcraft.getImei(), cc.settings.username));
        } else {
            client = null;
        }
    }

    public boolean connect(String ip) {
        guiscr.setText(cc.langBundle.getText("ServerGame.connecting"));
        try {
            client = (SocketConnection) Connector.open("socket://" + ip);
            if (client == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            guiscr.setText(cc.langBundle.getText("ServerGame.error.connect"));
            guiscr.buttonSetBack();
            return false;
        }
        try {
            client.setSocketOption(SocketConnection.KEEPALIVE, 1);
            client.setSocketOption(SocketConnection.DELAY, 0);
            dis = client.openDataInputStream();
            dos = client.openDataOutputStream();
            reciever = new RecieveThread();
            sender = new SendThread();
            new Thread(reciever).start();
            new Thread(sender).start();
        } catch (IOException e) {
            try {
                client.close();
            } catch (IOException e1) {
                // #debug e1.printStackTrace();
            }
            guiscr.setText(cc.langBundle.getText("ServerGame.error.init"));
            return false;
        }
        return true;
    }

    public boolean hasConnected() {
        return client != null;
    }

    protected void disconnect() {
        closing = true;
        try {
            dis.close();
        } catch (IOException e) {
        }
        try {
            dos.close();
        } catch (IOException e) {
        }
        try {
            client.close();
        } catch (IOException e) {
        }
        cc.serverLoader.game = null;
    }

    public void begin() {
        cc.playerManager = new PlayerManagerImp(cc);
        sender.sendPacket(new PacketWorldInfo());
        startWorld();

    }

    private void startWorld() {
        cc.guiIngame = new GuiIngame(cc);
        cc.guiIngame.initIngameGui();
        cc.player = cc.playerManager.createPlayer();
        cc.guiIngame.addCommandButton();
    }

    public WorldInfo loadWorldInfo(EntityPlayer player) {
        return worldInfo;
    }

    public void saveWorldInfo(WorldInfo worldInfo, EntityPlayer player) {
        injectDisconnectPacket(player);
    }

    public ChunkLoader getChunkLoader(World world) {
        isAlive = true;
        this.world = world;
        return this;
    }

    public void onChunkLoaderEnd() {
        isAlive = false;
        disconnect();
    }

    public Chunk loadChunk(int x, int z) {
        if (!isAlive) {
            return null;
        }
        sender.sendPacket(new PacketChunkData(x, z));
        while (chunk == null && isAlive) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // #debug e.printStackTrace();
            }
        }
        Chunk c = chunk;
        chunk = null;
        return c;
    }

    public void saveChunks(Hashtable chunksMap, LoadingScreen loadingScreen) {
    }

    public void blockChanged(Chunk chunk, int x, int y, int z, int id, int metadata) {
        if (ignoreChange) {
            ignoreChange = false;
            return;
        }
        sender.sendPacket(new PacketBlockChange(chunk, x, y, z, id, metadata));

    }

    public void processAnyPackets() {
        reciever.processNext(this);
    }

    class SendThread implements Runnable {
        private Packet[] queue;

        SendThread() {
            resetQueue();
        }

        private void resetQueue() {
            queue = new Packet[0];
        }

        public void run() {
            try {
                int i = 0;
                while (!closing) {
                    if (queue.length > 0) {
                        for (i = 0; i < queue.length; i++) {
                            writeToStream(queue[i]);
                        }
                        resetQueue();
                        dos.flush();
                    } else {
                        Thread.sleep(50);
                    }
                }
            } catch (Exception e) {
                // #debug e.printStackTrace();
                quitServer(cc.langBundle.getText("ServerGame.error.conLost"));
            }
        }

        private void writeToStream(Packet packet) throws IOException {
            if (packet == null) {
                return;
            }
            dos.write(packet.getPacketId());
            packet.writeData(dos);
        }

        public void sendPacket(Packet packet) {
            Packet[] newQueue = new Packet[queue.length + 1];
            System.arraycopy(queue, 0, newQueue, 0, queue.length);
            queue = newQueue;
            queue[queue.length - 1] = packet;
        }
    }

    class RecieveThread implements Runnable {
        private Packet[] stack;

        RecieveThread() {
            resetStack();
        }

        public void processNext(ServerGame handler) {
            if (stack.length == 0) {
                return;
            }
            for (int i = 0; i < stack.length; i++) {
                if (stack[i] != null) {
                    stack[i].process(handler);
                    stack[i] = null;
                    return;
                }
            }
            resetStack();
        }

        private void resetStack() {
            stack = new Packet[0];
        }

        public void run() {
            try {
                while (!closing) {
                    recieveStream();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // #debug e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                // #debug e.printStackTrace();
                quitServer(cc.langBundle.getText("ServerGame.error.conLost"));
            }
        }

        private void recieveStream() throws IOException {
            int size;
            try {
                size = dis.read() & 0xFF;
            } catch (IOException e) {
                if (closing) {
                    return;
                }
                throw e;
            }
            Packet[] newStack = new Packet[stack.length + size];
            System.arraycopy(stack, 0, newStack, 0, stack.length);
            Packet p;
            for (int i = 0; i < size; i++) {
                p = Packet.getNewPacket(new Integer(dis.read() & 0xFF));
                p.readData(dis);
                newStack[stack.length + i] = p;
            }
            stack = newStack;
        }
    }

    public void handleWorldInfo(DataInputStream dis) {
        worldInfo = new WorldInfo();
        try {
            worldInfo.loadWorldInfo(dis, cc.player);
        } catch (IOException e) {
            // #debug e.printStackTrace();
        }
        cc.world = new World(cc, this);
        cc.displayGuiScreen(new GuiLoadingWorld());
        cc.isGamePaused = false;
    }

    private void quitServer(String reason) {
        cc.isGamePaused = true;
        closing = true;
        guiscr.setText(reason);
        guiscr.buttonSetBack();
        cc.displayGuiScreen(guiscr);
        cc.world.onWorldEnd();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        cc.world = null;
    }

    public void handleBlockChange(PacketBlockChange p) {
        Chunk chunk = cc.world.getChunkFromChunkCoords(p.chunkX, p.chunkZ);
        ignoreChange = true;
        chunk.setBlockIDWithMetadata(p.x, p.y, p.z, p.id, p.metadata);
    }

    public void handleChunkData(byte[] data, int x, int z) {
        Chunk chunk = new Chunk(world, x, z);

        ChunkStorage[] blockStorage = new ChunkStorage[8];
        int index = 0;

        for (int i = 0; i < blockStorage.length; ++i) {
            blockStorage[i] = new ChunkStorage();

            byte[] id = new byte[64];
            byte[] metadata = new byte[64];
            System.arraycopy(data, index, id, 0, id.length);
            System.arraycopy(data, index + 64, metadata, 0, metadata.length);
            blockStorage[i].setBlockIDArray(id);
            blockStorage[i].setBlockMetadataArray(metadata);

            blockStorage[i].initBlockStorage();
            index += 128;
        }

        chunk.setBlockStorageArray(blockStorage);
        this.chunk = chunk;
    }

    private void injectDisconnectPacket(EntityPlayer player) {
        try {
            // The game exits too quickly, need to inject these packets directly into the stream.
            sender.writeToStream(new PacketPlayerData(player));
            sender.writeToStream(new PacketDisconnect());
        } catch (IOException e) {
            // #debug e.printStackTrace();
        }
    }

    public void handleDisconnect(String reason, PacketDisconnect p) {
        injectDisconnectPacket(cc.player);
        quitServer(reason);
    }
}
