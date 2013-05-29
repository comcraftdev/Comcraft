/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexBuffer;

/**
 *
 * @author Piotr Wójcik (Queader)
 */
public class ModelFlower {

    public static IndexBuffer indexBuffer;
    public static VertexBuffer[] vertexBuffer;

    private ModelFlower() {
    }

    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});

        short[][] vertexTable = {
            {0, 10, 0, 10, 10, 10, 0, 0, 0, 10, 0, 10},
            {0, 10, 10, 10, 10, 0, 0, 0, 10, 10, 0, 0},};
        short[][] textureTable = {
            {0, 0, 1, 0, 0, 1, 1, 1}, // back
            {0, 0, 1, 0, 0, 1, 1, 1}, // back
        };

        vertexBuffer = Model.getVertexBuffer(vertexTable, textureTable, 1);
    }
}