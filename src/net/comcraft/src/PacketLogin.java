package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.comcraft.client.Comcraft;

public class PacketLogin extends Packet {

    private int uniqueId;
    private String username;

    public PacketLogin() {
    }

    /*
     * Generates a unique ID for the player using the Adler32 of the IMEI. The id is unique enough for identification. Copied from PNGEncoder.GZIP.calcADLER32
     */
    public PacketLogin(String imei, String username) {
        if (imei.equals("Unknown")) {
            // Anything that is fairly unique
            String[] keys = new String[] { "microedition.platform", "microedition.commports", "microedition.encoding", "microedition.profiles" };
            for (int i = 0; i < keys.length; i++) {
                imei += System.getProperty(keys[i]) != null ? System.getProperty(keys[i]) : imei;
            }
            imei += new Integer(Comcraft.screenHeight).toString() + new Integer(Comcraft.screenWidth).toString();
        }
        byte[] b = imei.getBytes();
        int s1 = 1;
        int s2 = 0;
        for (int i = 0; i < b.length; i++) {
            int abs = b[i] >= 0 ? b[i] : (b[i] + 256);
            s1 = ((s1 + abs) % 65521) & 0xFF;
            s2 = ((s2 + s1) % 65521) & 0xFF;
        }
        uniqueId = ((s2 << 16) + s1);
        if (username.toLowerCase().equals("player")) {
            username += "." + uniqueId;
        }
        System.out.println(uniqueId);
        this.username = username;
    }

    public void writeData(DataOutputStream dos) throws IOException {
        dos.writeInt(uniqueId);
        dos.writeUTF(username);
    }

    public void readData(DataInputStream dis) throws IOException {
    }

    public void process(ServerGame handler) {
    }

}
