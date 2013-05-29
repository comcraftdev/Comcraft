package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

public final class ModelPieceBlock {

    /*
     * Block sides ----------- public static final byte WALL_FRONT = 0; public
     * static final byte WALL_BACK = 1; public static final byte WALL_RIGHT = 2;
     * public static final byte WALL_LEFT = 3; public static final byte WALL_TOP
     * = 4; public static final byte WALL_BOTTOM = 5;
     */
    public static IndexBuffer indexBuffer;
    public static VertexBuffer[][][][] vertexBuffer;

    private ModelPieceBlock() {
    }

    public static void initModelPiece() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});
        vertexBuffer = new VertexBuffer[4][4][4][6];

        for (short z = 1; z <= 4; ++z) {
            for (short y = 1; y <= 4; ++y) {
                for (short x = 1; x <= 4; ++x) {
                    short[][] vertexTable = {
                        {0, 0, (short) (10 * z), (short) (10 * x), 0, (short) (10 * z), 0, (short) (10 * y), (short) (10 * z), (short) (10 * x), (short) (10 * y), (short) (10 * z)},
                        {0, (short) (10 * y), 0, (short) (10 * x), (short) (10 * y), 0, 0, 0, 0, (short) (10 * x), 0, 0},
                        {0, (short) (10 * y), (short) (10 * z), 0, (short) (10 * y), 0, 0, 0, (short) (10 * z), 0, 0, 0},
                        {(short) (10 * x), (short) (10 * y), 0, (short) (10 * x), (short) (10 * y), (short) (10 * z), (short) (10 * x), 0, 0, (short) (10 * x), 0, (short) (10 * z)},
                        {0, (short) (10 * y), (short) (10 * z), (short) (10 * x), (short) (10 * y), (short) (10 * z), 0, (short) (10 * y), 0, (short) (10 * x), (short) (10 * y), 0},
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

                    vertexBuffer[x - 1][y - 1][z - 1] = Model.getVertexBuffer(vertexTable, textureTable, 1);
                }
            }
        }
    }
}
