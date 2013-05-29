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
public class ModelSmallCube {

    public static VertexBuffer[] vertexBuffer;
    public static IndexBuffer indexBuffer;
    
    private ModelSmallCube() {
    }
    
    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});
        
        short[][] verticesTable = {
            {10, 10, 10, 0, 10, 10, 10, 0, 10, 0, 0, 10},
            {0, 10, 0, 10, 10, 0, 0, 0, 0, 10, 0, 0},
            {0, 10, 10, 0, 10, 0, 0, 0, 10, 0, 0, 0},
            {10, 10, 0, 10, 10, 10, 10, 0, 0, 10, 0, 10},
            {10, 10, 0, 0, 10, 0, 10, 10, 10, 0, 10, 10},
            {10, 0, 10, 0, 0, 10, 10, 0, 0, 0, 0 ,0 ,0}
        };
        
        short[][] texturesTable = {
            {1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 0, 1}
        };
        
        vertexBuffer = Model.getVertexBuffer(verticesTable, texturesTable, 1);
    }
}