/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Piotr Wójcik
 */
public class HTTPHelper {

    public static void getConnectionInformation(HttpConnection hc) {
        System.out.println("Request Method for this connection is " + hc.getRequestMethod());
        System.out.println("URL in this connection is " + hc.getURL());
        System.out.println("Protocol for this connection is " + hc.getProtocol()); // It better be HTTP:)
        System.out.println("This object is connected to " + hc.getHost() + " host");
        System.out.println("HTTP Port in use is " + hc.getPort());
        System.out.println("Query parameter in this request are  " + hc.getQuery());
    }

    public static String getLineHTTP(String url) throws IOException {
        HttpConnection connection = null;

        try {
            connection = (HttpConnection) Connector.open(url);

            connection.setRequestMethod(HttpConnection.GET);
            connection.setRequestProperty("User-Agent", "Profile/MIDP-X Confirguration/CLDC-X");

            getConnectionInformation(connection);
            
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = connection.openInputStream();

            String line;

            int length = (int) connection.getLength();

            if (length != -1) {
                byte incomingData[] = new byte[length];

                inputStream.read(incomingData);

                line = new String(incomingData);
            } else {
                ByteArrayOutputStream bytestream = new ByteArrayOutputStream();

                int ch;

                while ((ch = inputStream.read()) != -1) {
                    bytestream.write(ch);
                }

                line = new String(bytestream.toByteArray());

                bytestream.close();
            }

            inputStream.close();

            return line;
        } catch (IOException ex) {
            //#debug
//#             ex.printStackTrace();
            throw new ComcraftException(ex);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}