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
 * @author Piotrek
 */
public class ModelStairs {

    public static VertexBuffer[] vertexBuffer;
    public static IndexBuffer indexBuffer;

    private ModelStairs() {
    }

    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});

        int x = 1;
        int y = 1;
        int z = 1;

        short[][] vertexTable = {
            {0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z), 0, (short) (5 * y), (short) (10 * z), (short) (10 * x), (short) (5 * y), (short) (10 * z)},
            {0, (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), 0, 0, 0, 0, (short) (10 * x), 0, 0},
            {0, (short) (5 * y), (short) (10 * z), 0, (short) (5 * y), 0, 0, 0, (short) (10 * z), 0, 0, 0},
            {(short) (10 * x), (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), (short) (10 * z), (short) (10 * x), 0, 0, (short) (10 * x), 0, (short) (10 * z)},
            {0, (short) (5 * y), (short) (10 * z), (short) (10 * x), (short) (5 * y), (short) (10 * z), 0, (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), 0},
            {0, 0, 0, (short) (10 * x), 0, 0, 0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z)}
        };
        short[][] textureTable = {
            {(short) (2 * x), (short) (1 * y), 0, (short) (1 * y), (short) (2 * x), 0, 0, 0}, // front
            {0, 0, (short) (2 * x), 0, 0, (short) (1 * y), (short) (2 * x), (short) (1 * y)}, // back
            {0, 0, (short) (2 * z), 0, 0, (short) (1 * y), (short) (2 * z), (short) (1 * y)}, // right
            {0, 0, (short) (2 * z), 0, 0, (short) (1 * y), (short) (2 * z), (short) (1 * y)}, // left
            {0, 0, (short) (2 * x), 0, 0, (short) (2 * z), (short) (2 * x), (short) (2 * z)}, // top
            {0, 0, (short) (2 * x), 0, 0, (short) (2 * z), (short) (2 * x), (short) (2 * z)} // bottom
        };

        VertexBuffer[] vb1 = Model.getVertexBuffer(vertexTable, textureTable, 0.5f);
        
        short[][] vertexTable2 = {
            {0, 5, (short) (10 * z), (short) (10 * x), 5, (short) (10 * z), 0, (short) (10 * y), (short) (10 * z), (short) (10 * x), (short) (10 * y), (short) (10 * z)},
            {0, (short) (10 * y), 5, (short) (10 * x), (short) (10 * y), 5, 0, 5, 5, (short) (10 * x), 5, 5},
            {0, (short) (10 * y), (short) (10 * z), 0, (short) (10 * y), 5, 0, 5, (short) (10 * z), 0, 5, 5},
            {(short) (10 * x), (short) (10 * y), 5, (short) (10 * x), (short) (10 * y), (short) (10 * z), (short) (10 * x), 5, 5, (short) (10 * x), 5, (short) (10 * z)},
            {0, (short) (10 * y), (short) (10 * z), (short) (10 * x), (short) (10 * y), (short) (10 * z), 0, (short) (10 * y), 5, (short) (10 * x), (short) (10 * y), 5},
        };
        short[][] textureTable2 = {
            {(short) (2 * x), (short) (1 * y), 0, (short) (1 * y), (short) (2 * x), 0, 0, 0}, // front
            {0, 0, (short) (2 * x), 0, 0, (short) (1 * y), (short) (2 * x), (short) (1 * y)}, // back
            {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // right
            {0, 0, (short) (1 * z), 0, 0, (short) (1 * y), (short) (1 * z), (short) (1 * y)}, // left
            {0, 0, (short) (2 * x), 0, 0, (short) (1 * z), (short) (2 * x), (short) (1 * z)}, // top
        };
        
        VertexBuffer[] vb2 = Model.getVertexBuffer(vertexTable2, textureTable2, 0.5f);
        
        vertexBuffer = new VertexBuffer[vb1.length + vb2.length];
        
        for (int n = 0; n < vertexBuffer.length; ++n) {
            if (n >= vb1.length) {
                vertexBuffer[n] = vb2[n - vb1.length];
            } else {
                vertexBuffer[n] = vb1[n];
            }
        }
    }
}
