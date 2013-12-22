package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexBuffer;

public final class ModelPieceSlab {

    /*
     * Block sides ----------- public static final byte WALL_FRONT = 0; public
     * static final byte WALL_BACK = 1; public static final byte WALL_RIGHT = 2;
     * public static final byte WALL_LEFT = 3; public static final byte WALL_TOP
     * = 4; public static final byte WALL_BOTTOM = 5;
     */
    public static IndexBuffer indexBuffer;
    public static VertexBuffer[][][][] vertexBuffer;

    private ModelPieceSlab() {
    }

    public static void initModelPiece() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});
        vertexBuffer = new VertexBuffer[4][1][4][6];

        for (short z = 1; z <= 4; ++z) {
            for (short y = 1; y <= 1; ++y) {
                for (short x = 1; x <= 4; ++x) {
                    short[][] vertexTable = {
                        {0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z), 0, (short) (5 * y), (short) (10 * z), (short) (10 * x), (short) (5 * y), (short) (10 * z)},
                        {0, (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), 0, 0, 0, 0, (short) (10 * x), 0, 0},
                        {0, (short) (5 * y), (short) (10 * z), 0, (short) (5 * y), 0, 0, 0, (short) (10 * z), 0, 0, 0},
                        {(short) (10 * x), (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), (short) (10 * z), (short) (10 * x), 0, 0, (short) (10 * x), 0, (short) (10 * z)},
                        {0, (short) (5 * y), (short) (10 * z), (short) (10 * x), (short) (5 * y), (short) (10 * z), 0, (short) (5 * y), 0, (short) (10 * x), (short) (5 * y), 0},
                        {0, 0, 0, (short) (10 * x), 0, 0, 0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z)}
                    };
                    short[][] textureTable = {
                        {(short) (2 * x), (short) (2 * y), 0, (short) (2 * y), (short) (2 * x), 1, 0, 1}, // front
                        {0, 1, (short) (2 * x), 1, 0, (short) (2 * y), (short) (2 * x), (short) (2 * y)}, // back
                        {0, 1, (short) (2 * z), 1, 0, (short) (2 * y), (short) (2 * z), (short) (2 * y)}, // right
                        {0, 1, (short) (2 * z), 1, 0, (short) (2 * y), (short) (2 * z), (short) (2 * y)}, // left
                        {0, 0, (short) (2 * x), 0, 0, (short) (2 * z), (short) (2 * x), (short) (2 * z)}, // top
                        {0, 0, (short) (2 * x), 0, 0, (short) (2 * z), (short) (2 * x), (short) (2 * z)} // bottom
                    };

                    vertexBuffer[x - 1][y - 1][z - 1] = Model.getVertexBuffer(vertexTable, textureTable, 0.5f);
                }
            }
        }
    }
}