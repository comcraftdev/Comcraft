/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class Model {
    
    public static VertexBuffer[] getVertexBuffer(short[][] verticesTable, short[][] texturesTable, float textureCoords) {
        VertexBuffer[] vertexBuffer = new VertexBuffer[verticesTable.length];

        for (int n = 0; n < verticesTable.length; ++n) {
            VertexArray vertexArray = new VertexArray(verticesTable[n].length / 3, 3, 2);
            vertexArray.set(0, verticesTable[n].length / 3, verticesTable[n]);

            VertexArray textureArray = new VertexArray(texturesTable[n].length / 2, 2, 2);
            textureArray.set(0, texturesTable[n].length / 2, texturesTable[n]);

            vertexBuffer[n] = new VertexBuffer();
            vertexBuffer[n].setPositions(vertexArray, 1.0f, null);
            vertexBuffer[n].setTexCoords(0, textureArray, textureCoords, null);
        }
        
        return vertexBuffer;
    }

    public static VertexBuffer getVertexBuffer(short[] verticesTable, short[] texturesTable, float textureCoords) {
        VertexArray vertexArray = new VertexArray(verticesTable.length / 3, 3, 2);
        vertexArray.set(0, verticesTable.length / 3, verticesTable);

        VertexArray textureArray = new VertexArray(texturesTable.length / 2, 2, 2);
        textureArray.set(0, texturesTable.length / 2, texturesTable);

        VertexBuffer vertexBuffer = new VertexBuffer();
        vertexBuffer.setPositions(vertexArray, 1.0f, null);
        vertexBuffer.setTexCoords(0, textureArray, textureCoords, null);

        return vertexBuffer;
    }
}