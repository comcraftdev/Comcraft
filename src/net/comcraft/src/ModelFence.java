/*
 * Copyright (C) 2013 Piotr Wójcik
 *
 */

package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik
 */
public class ModelFence {
    public static IndexBuffer indexBuffer;
    public static VertexBuffer[] vertexBufferFull;
    public static VertexBuffer[] vertexBufferSmall;

    private ModelFence() {
    }

    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});

        short[][] vertexTable = {
            {0, 0, 6, 10, 0, 6, 0, 10, 6, 10, 10, 6},
            {0, 10, 3, 10, 10, 3, 0, 0, 3, 10, 0, 3},
            {0, 10, 6, 0, 10, 3, 0, 0, 6, 0, 0, 3},
            {10, 10, 3, 10, 10, 6, 10, 0, 3, 10, 0, 6},
            {0, 10, 6, 10, 10, 6, 0, 10, 3, 10, 10, 3},
            {0, 0, 3, 10, 0, 3, 0, 0, 6, 10, 0, 6}
        };
        short[][] textureTable = {
            {16, 16, 0, 16, 16, 0, 0, 0}, // front
            {0, 0, 16, 0, 0, 16, 16, 16}, // back
            {6, 0, 10, 0, 6, 16, 10, 16}, // right
            {6, 0, 10, 0, 6, 16, 10, 16}, // left
            {0, 3, 16, 3, 0, 6, 16, 6}, // top
            {0, 3, 16, 3, 0, 6, 16, 6} // bottom
        };
        
        int x = 10;
        int z = 10;

        short[][] vertexTable2 = {
            {(short) (6 / 16f * x), 0, (short) (10 / 16f * z), (short) (10 / 16f * x), 0, (short) (10 / 16f * z), (short) (6 / 16f * x), 10, (short) (10 / 16f * z), (short) (10 / 16f * x), 10, (short) (10 / 16f * z)},
            {(short) (6 / 16f * x), 10, (short) (6 / 16f * z), (short) (10 / 16f * x), 10, (short) (6 / 16f * z), (short) (6 / 16f * x), 0, (short) (6 / 16f * z), (short) (10 / 16f * x), 0, (short) (6 / 16f * z)},
            {(short) (6 / 16f * x), 10, (short) (10 / 16f * z), (short) (6 / 16f * x), 10, (short) (6 / 16f * z), (short) (6 / 16f * x), 0, (short) (10 / 16f * z), (short) (6 / 16f * x), 0, (short) (6 / 16f * z)},
            {(short) (10 / 16f * x), 10, (short) (6 / 16f * z), (short) (10 / 16f * x), 10, (short) (10 / 16f * z), (short) (10 / 16f * x), 0, (short) (6 / 16f * z), (short) (10 / 16f * x), 0, (short) (10 / 16f * z)},
            {(short) (6 / 16f * x), 10, (short) (10 / 16f * z), (short) (10 / 16f * x), 10, (short) (10 / 16f * z), (short) (6 / 16f * x), 10, (short) (6 / 16f * z), (short) (10 / 16f * x), 10, (short) (6 / 16f * z)},
            {(short) (6 / 16f * x), 10, (short) (6 / 16f * z), (short) (10 / 16f * x), 0, (short) (6 / 16f * z), (short) (6 / 16f * x), 0, (short) (10 / 16f * z), (short) (10 / 16f * x), 0, (short) (10 / 16f * z)}
        };
        short[][] textureTable2 = {
            {10, 16, 6, 16, 10, 0, 6, 0}, // front
            {6, 0, 10, 0, 6, 16, 10, 16}, // back
            {6, 0, 10, 0, 6, 16, 10, 16}, // right
            {6, 0, 10, 0, 6, 16, 10, 16}, // left
            {6, 0, 10, 0, 6, 4, 10, 4}, // top
            {6, 0, 10, 0, 6, 4, 10, 4} // bottom
        };

        vertexBufferFull = Model.getVertexBuffer(vertexTable, textureTable, 0.0625f);
        vertexBufferSmall = Model.getVertexBuffer(vertexTable2, textureTable2, 0.0625f);
    }
}