package net.comcraft.src;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexBuffer;

public final class ModelTorch {

    public static IndexBuffer indexBuffer;
    public static VertexBuffer[] vertexBuffer;

    private ModelTorch() {
    }

    public static void initModel() {
        indexBuffer = new TriangleStripArray(0, new int[]{4});

        int x = 10;
        int y = 10;
        int z = 10;

        short[][] vertexTable = {
            {(short) (7 / 16f * x), 0, (short) (9 / 16f * z), (short) (9 / 16f * x), 0, (short) (9 / 16f * z), (short) (7 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z), (short) (9 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z)},
            {(short) (7 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z), (short) (9 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z), (short) (7 / 16f * x), 0, (short) (7 / 16f * z), (short) (9 / 16f * x), 0, (short) (7 / 16f * z)},
            {(short) (7 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z), (short) (7 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z), (short) (7 / 16f * x), 0, (short) (9 / 16f * z), (short) (7 / 16f * x), 0, (short) (7 / 16f * z)},
            {(short) (9 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z), (short) (9 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z), (short) (9 / 16f * x), 0, (short) (7 / 16f * z), (short) (9 / 16f * x), 0, (short) (9 / 16f * z)},
            {(short) (7 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z), (short) (9 / 16f * x), (short) (10 / 16f * y), (short) (9 / 16f * z), (short) (7 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z), (short) (9 / 16f * x), (short) (10 / 16f * y), (short) (7 / 16f * z)},
            {(short) (7 / 16f * x), 0, (short) (7 / 16f * z), (short) (9 / 16f * x), 0, (short) (7 / 16f * z), (short) (7 / 16f * x), 0, (short) (9 / 16f * z), (short) (9 / 16f * x), 0, (short) (9 / 16f * z)}
        };
        short[][] textureTable = {
            {9, 16, 7, 16, 9, 6, 7, 6}, // front
            {7, 6, 9, 6, 7, 16, 9, 16}, // back
            {7, 6, 9, 6, 7, 16, 9, 16}, // right
            {7, 6, 9, 6, 7, 16, 9, 16}, // left
            {7, 6, 9, 6, 7, 8, 9, 8}, // top
            {7, 8, 9, 8, 7, 10, 9, 10} // bottom
        };

        vertexBuffer = Model.getVertexBuffer(vertexTable, textureTable, 0.0625f);
    }
}
