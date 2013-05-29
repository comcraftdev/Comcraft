/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comcraft.src;

import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class ModelLiquid {

    public static VertexBuffer[][] vertexBuffer;

    private ModelLiquid() {
    }

    public static void initModel() {
        vertexBuffer = new VertexBuffer[8][6];

        for (int h = 1; h <= 8; ++h) {
            int x = 1;
            int y = 1;
            int z = 1;

            short[][] vertexTable = {
                {0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z), 0, (short) ((h / 8f) * 10 * y), (short) (10 * z), (short) (10 * x), (short) ((h / 8f) * 10 * y), (short) (10 * z)},
                {0, (short) ((h / 8f) * 10 * y), 0, (short) (10 * x), (short) ((h / 8f) * 10 * y), 0, 0, 0, 0, (short) (10 * x), 0, 0},
                {0, (short) ((h / 8f) * 10 * y), (short) (10 * z), 0, (short) ((h / 8f) * 10 * y), 0, 0, 0, (short) (10 * z), 0, 0, 0},
                {(short) (10 * x), (short) ((h / 8f) * 10 * y), 0, (short) (10 * x), (short) ((h / 8f) * 10 * y), (short) (10 * z), (short) (10 * x), 0, 0, (short) (10 * x), 0, (short) (10 * z)},
                {0, (short) ((h / 8f) * 10 * y), (short) (10 * z), (short) (10 * x), (short) ((h / 8f) * 10 * y), (short) (10 * z), 0, (short) ((h / 8f) * 10 * y), 0, (short) (10 * x), (short) ((h / 8f) * 10 * y), 0},
                {0, 0, 0, (short) (10 * x), 0, 0, 0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z)}
            };
            short[][] textureTable = {
                {(short) (1 * x), (short) (1 * y), 0, (short) (1 * y), (short) (1 * x), 0, 0, 0}, // front
                {0, 0, (short) (1 * x), 0, 0, (short) (1 * y), (short) (1 * x), (short) (1 * y)}, // back
                {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // right
                {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // left
                {0, 0, (short) (1 * x), 0, 0, (short) (1 * z), (short) (1 * x), (short) (1 * z)}, // top
                {0, 0, (short) (1 * x), 0, 0, (short) (1 * z), (short) (1 * x), (short) (1 * z)} // bottom
            };

            vertexBuffer[h - 1] = Model.getVertexBuffer(vertexTable, textureTable, 1);
        }
    }
}