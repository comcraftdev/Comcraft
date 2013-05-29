/*
 * Copyright (C) 2012 Piotr Wójcik
 *
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class ModelChristmasTree {

    public static IndexBuffer indexBuffer;
    public static VertexBuffer[][][][][] vertexBuffer;

    private ModelChristmasTree() {
    }

    public static void initModelPiece() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});
        vertexBuffer = new VertexBuffer[2][6][1][1][1];

        for (int n = 0; n < 2; ++n) {
            for (short z = 1; z <= 1; ++z) {
                for (short y = 1; y <= 1; ++y) {
                    for (short x = 1; x <= 1; ++x) {
                        float yI;
                        
                        short start;
                        short end;

                        if (n == 1) {
                            yI = 0.75f;
                            start = 3;
                            end = 7;
                        } else {
                            yI = 1;
                            start = 2;
                            end = 8;
                        }

                        short[][] vertexTable = {
                            {start, 0, (short) (end * z), (short) (end * x), 0, (short) (end * z), start, (short) (10 * yI), (short) (end * z), (short) (end * x), (short) (10 * yI), (short) (end * z)},
                            {start, (short) (10 * yI), start, (short) (end * x), (short) (10 * yI), start, start, 0, start, (short) (end * x), 0, start},
                            {start, (short) (10 * yI), (short) (end * z), start, (short) (10 * yI), start, start, 0, (short) (end * z), start, 0, start},
                            {(short) (end * x), (short) (10 * yI), start, (short) (end * x), (short) (10 * yI), (short) (end * z), (short) (end * x), 0, start, (short) (end * x), 0, (short) (end * z)},
                            {start, (short) (10 * yI), (short) (end * z), (short) (end * x), (short) (10 * yI), (short) (end * z), start, (short) (10 * yI), start, (short) (end * x), (short) (10 * yI), start},
                            {start, 0, start, (short) (end * x), 0, start, start, 0, (short) (end * z), (short) (end * x), 0, (short) (end * z)}
                        };

                        short[][] textureTable = {
                            {(short) (1 * x), (short) (1 * y), 0, (short) (1 * y), (short) (1 * x), 0, 0, 0}, // front
                            {0, 0, (short) (1 * x), 0, 0, (short) (1 * y), (short) (1 * x), (short) (1 * y)}, // back
                            {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // right
                            {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // left
                            {0, 0, (short) (1 * x), 0, 0, (short) (1 * z), (short) (1 * x), (short) (1 * z)}, // top
                            {0, 0, (short) (1 * x), 0, 0, (short) (1 * z), (short) (1 * x), (short) (1 * z)} // bottom
                        };

                        for (int side = 0; side < 6; ++side) {
                            VertexArray vertexArray = new VertexArray(vertexTable[side].length / 3, 3, 2);
                            vertexArray.set(0, vertexTable[side].length / 3, vertexTable[side]);

                            VertexArray textureArray = new VertexArray(textureTable[side].length / 2, 2, 2);
                            textureArray.set(0, textureTable[side].length / 2, textureTable[side]);

                            vertexBuffer[n][side][x - 1][y - 1][z - 1] = new VertexBuffer();
                            vertexBuffer[n][side][x - 1][y - 1][z - 1].setPositions(vertexArray, 1.0f, null);
                            vertexBuffer[n][side][x - 1][y - 1][z - 1].setTexCoords(0, textureArray, 1.0f, null);
                        }
                    }
                }
            }
        }
    }
}