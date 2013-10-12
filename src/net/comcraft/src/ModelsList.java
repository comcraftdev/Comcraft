package net.comcraft.src;

import java.util.Hashtable;
import javax.microedition.m3g.Loader;
import javax.microedition.m3g.Node;

public class ModelsList {

    private static Hashtable modelsList;

    private ModelsList() {
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
//        ModelSmallCube.initModel();
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
