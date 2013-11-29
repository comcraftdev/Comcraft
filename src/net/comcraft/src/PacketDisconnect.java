package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketDisconnect extends Packet {

    private String reason;

    public void writeData(DataOutputStream dos) throws IOException {
    }

    public void readData(DataInputStream dis) throws IOException {
        this.reason = dis.readUTF();
    }

    public void process(ServerGame handler) {
        handler.handleDisconnect(reason, this);
    }

}
