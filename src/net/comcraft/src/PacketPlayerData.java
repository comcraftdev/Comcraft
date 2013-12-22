package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketPlayerData extends Packet {

    private EntityPlayer player;

    public PacketPlayerData() {
    }

    public PacketPlayerData(EntityPlayer player) {
        this.player = player;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        player.writeToDataOutputStream(dos);
    }

    public void readData(DataInputStream dis) throws IOException {

    }

    public void process(ServerGame handler) {
    }

}
