package net.comcraft.src;

import java.util.Hashtable;

import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Node;

import com.google.minijoe.sys.JsObject;

public class ModelsList extends JsObject {

    private static Hashtable modelsList;

    public ModelsList() {
        super(OBJECT_PROTOTYPE);
        addVar("PieceBlock", new ModArray(new Object[] { ModelPieceBlock.indexBuffer, ModelPieceBlock.vertexBuffer }));
        addVar("PieceSlab", new ModArray(new Object[] { ModelPieceSlab.indexBuffer, ModelPieceSlab.vertexBuffer }));
        addVar("Liquid", new ModArray(new Object[] { null, ModelLiquid.vertexBuffer }));
        addVar("Flower", new ModArray(new Object[] { ModelFlower.indexBuffer, ModelFlower.vertexBuffer }));
        addVar("PieceCarpet", new ModArray(new Object[] { ModelPieceCarpet.indexBuffer, ModelPieceCarpet.vertexBuffer }));
        addVar("Torch", new ModArray(new Object[] { ModelTorch.indexBuffer, ModelTorch.vertexBuffer }));
        addVar("Door", new ModArray(new Object[] { ModelDoor.indexBuffer, ModelDoor.vertexBuffer }));
        addVar("Stairs", new ModArray(new Object[] { ModelStairs.indexBuffer, ModelStairs.vertexBuffer }));
        addVar("SmallCube", new ModArray(new Object[] { ModelSmallCube.indexBuffer, ModelSmallCube.vertexBuffer }));
        addVar("Fence", new ModArray(new Object[] { ModelFence.indexBuffer, ModelFence.vertexBufferFull, ModelFence.vertexBufferSmall }));
    }

    public static void initModelList() {
        modelsList = new Hashtable(20);

        ModelPieceBlock.initModelPiece();
        ModelPieceSlab.initModelPiece();
        ModelLiquid.initModel();
        ModelFlower.initModel();
        ModelPieceCarpet.initModelPiece();
        ModelTorch.initModel();
        ModelDoor.initModel();
        ModelStairs.initModel();
        ModelSmallCube.initModel();
        ModelFence.initModel();
    }

    public static Node getModel(String path) {
        Node node = (Node) modelsList.get(path);

        if (node != null) {
            return node;
        } else {
            loadModel(path);

            return (Node) modelsList.get(path);
        }
    }

    private static void loadModel(String path) {
        try {
            Node node = (Node) Loader.load(path)[0];

            if (node != null) {
                modelsList.put(path, node);
            }
        } catch (Exception e) {
            //#debug
//#             e.printStackTrace();
        }
    }
}
