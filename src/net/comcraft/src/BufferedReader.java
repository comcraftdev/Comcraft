package net.comcraft.src;

import java.io.IOException;
import java.io.Reader;

public class BufferedReader {

    private Reader reader;
    private boolean reachedTheEnd;

    public BufferedReader(Reader reader) {
        this.reader = reader;
        reachedTheEnd = false;
    }

    public boolean getReachedTheEnd() {
        return reachedTheEnd;
    }

    public String readLine() {
        StringBuffer line = new StringBuffer();

        int c = 0;

        try {
            c = reader.read();
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
        }

        while (c != -1 && c != '\n' && c != 10) {
            if (c != 13) {
                line.append((char) c);
            }

            try {
                c = reader.read();
            } catch (IOException ex) {
                //#debug
//#                 ex.printStackTrace();
            }
        }

        if (c == -1) {
            reachedTheEnd = true;
        }

        if (line.length() == 0) {
            return null;
        }

        return line.toString();
    }

    public void close() throws IOException {
        reader.close();
    }
}
