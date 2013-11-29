package net.comcraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWorldInfo extends Packet {

    private DataInputStream dis;

    public PacketWorldInfo() {
    }

    public void writeData(DataOutputStream dos) throws IOException {
    }

    /*
     * A bad way to pull all data from response and store in a temporary stream for processing later.
     */
    public void readData(DataInputStream dis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        float ver = dis.readFloat();
        dos.writeFloat(ver);
        byte[] data;
        data = new byte[36];
        dis.read(data);
        dos.write(data);
        int n = dis.readInt();
        dos.writeInt(n);
        data = new byte[n * 4];
        dis.read(data);
        dos.write(data);
        if (ver > 3) {
            dos.writeBoolean(dis.readBoolean());
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        this.dis = new DataInputStream(bais);
    }

    public void process(ServerGame handler) {
        handler.handleWorldInfo(dis);
    }

}
