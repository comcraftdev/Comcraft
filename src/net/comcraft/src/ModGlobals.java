package net.comcraft.src;

import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsObjectFactory;
import com.google.minijoe.sys.JsSystem;

public class ModGlobals implements JsObjectFactory {
    public static JsObject global;

    static final int FACTORY_ID_BLOCK = 0;
    static ModGlobals instance = new ModGlobals();
    static ChunkGeneratorNormal cgn = new ChunkGeneratorNormal(JsObject.OBJECT_PROTOTYPE);
    static ChunkGeneratorFlat cgf = new ChunkGeneratorFlat(JsObject.OBJECT_PROTOTYPE);

    public static JsObject createGlobal() {
        global = JsSystem.createGlobal();
        global.addVar("Block", new JsFunction(instance, FACTORY_ID_BLOCK,
                Block.BLOCK_PROTOTYPE, Block.ID_CONSTRUCTOR, 1));
        return global;
    }

    public JsObject newInstance(int type) {
        switch (type) {
        case FACTORY_ID_BLOCK:
            return new Block(Block.BLOCK_PROTOTYPE);
        default:
            throw new IllegalArgumentException();
        }
    }

}
