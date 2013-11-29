package net.comcraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.comcraft.client.Comcraft;

public class ServerLoader {

    private final Comcraft cc;
    private Vector serverList;
    public ServerGame game = null;

    public ServerLoader(Comcraft cc) {
        this.cc = cc;
        serverList = new Vector();
    }

    private FileConnection open(int mode) throws IOException {
        return (FileConnection) Connector.open(cc.settings.getComcraftFileSystem().getPathToFolder("servers.info"), mode);
    }

    public void updateServerList() {
        try {
            serverList.removeAllElements();

            if (!cc.settings.getComcraftFileSystem().isAvailable()) {
                return;
            }

            FileConnection file = open(Connector.READ_WRITE);
            if (!file.exists()) {
                file.create();
                update(file);
                return;
            }
            DataInputStream dis = file.openDataInputStream();
            int total = dis.readShort();
            String name, ip;
            for (int i = 0; i < total; i++) {
                name = dis.readUTF();
                ip = dis.readUTF();
                serverList.addElement(new String[] { name, ip });
            }
            dis.close();
            file.close();
        } catch (IOException ex) {
            throw new ComcraftException(ex);
        }
    }

    public Vector getServerList() {
        return serverList;
    }

    public void deleteServer(String[] selectedServer) {
        serverList.removeElement(selectedServer);
        update();
    }

    public void editServer(String[] existing, String name, String ip) {
        if (serverList.contains(existing)) {
            serverList.removeElement(existing);
        }
        addServer(name, ip);
    }

    public void addServer(String name, String ip) {
        serverList.insertElementAt(new String[] { name, ip }, 0);
        update();
    }

    private void update() {
        try {
            update(open(Connector.WRITE));
        } catch (IOException ex) {
            throw new ComcraftException(ex);
        }
    }

    private void update(FileConnection file) {
        try {
            DataOutputStream dos = file.openDataOutputStream();
            dos.writeShort(serverList.size());
            for (int i = 0; i < serverList.size(); i++) {
                String[] serverinf = (String[]) serverList.elementAt(i);
                dos.writeUTF(serverinf[0]);
                dos.writeUTF(serverinf[1]);
            }
            dos.close();
            file.close();
        } catch (IOException ex) {
            throw new ComcraftException(ex);
        }
    }

    public void newGame(String ip) {
        ServerGame g = new ServerGame(cc, ip);
        if (g.hasConnected()) {
            game = g;
            game.begin();
        }
    }
}
