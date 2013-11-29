package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.java4ever.apime.io.GZIP;

public class PacketChunkData extends Packet {
    private int x;
    private int z;
    private byte[] data;

    public PacketChunkData() {
    }

    public PacketChunkData(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        dos.write(x);
        dos.write(z);
    }

    public void readData(DataInputStream dis) throws IOException {
        x = dis.read();
        z = dis.read();
        int size = dis.readInt();
        byte[] gzip = new byte[size];
        dis.read(gzip);
        data = GZIP.inflate(gzip);
    }

    public void process(ServerGame handler) {
        handler.handleChunkData(data, x, z);
    }

}
