/*
 * Copyright (C) 2012 Piotr Wójcik
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
public class ModelDoor {

    public static IndexBuffer indexBuffer;
    public static VertexBuffer[] vertexBuffer;

    private ModelDoor() {
    }

    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});

        short[][] vertexTable = {
            {0, 0, 5, 10, 0, 5, 0, 10, 5, 10, 10, 5},
            {0, 10, 4, 10, 10, 4, 0, 0, 4, 10, 0, 4},
            {0, 10, 5, 0, 10, 4, 0, 0, 5, 0, 0, 4},
            {10, 10, 4, 10, 10, 5, 10, 0, 4, 10, 0, 5},
            {0, 10, 5, 10, 10, 5, 0, 10, 4, 10, 10, 4},
            {0, 0, 4, 10, 0, 4, 0, 0, 5, 10, 0, 5}
        };
        short[][] textureTable = {
            {16, 16, 0, 16, 16, 0, 0, 0}, // front
            {0, 0, 16, 0, 0, 16, 16, 16}, // back
            {0, 0, 2, 0, 0, 16, 2, 16}, // right
            {0, 0, 2, 0, 0, 16, 2, 16}, // left
            {0, 0, 2, 0, 0, 2, 2, 2}, // top
            {0, 0, 2, 0, 0, 2, 2, 2} // bottom
        };

        vertexBuffer = Model.getVertexBuffer(vertexTable, textureTable, 0.0625f);
    }
}