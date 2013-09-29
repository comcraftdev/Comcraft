package net.comcraft.src;

import java.util.Random;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;

public abstract class ChunkGenerator extends JsObject {

    private static final int ID_ADD_GENERATE_RULE = 100;
    private static final int ID_RANDOM = 101;
    protected long seed;
    private EventHandler ruleEvents = new EventHandler(new String[] { "Generator.genChunk" });
    protected Random random;

    public ChunkGenerator(long seed) {
        super(OBJECT_PROTOTYPE);
        random = new Random(seed);
        this.seed = seed;
        addNative("addGenerateRule", ID_ADD_GENERATE_RULE, 1);
        addNative("randNext", ID_RANDOM, 1);
    }
    public abstract ChunkStorage[] generateChunk(int x, int z);

    public void evalNative(int index, JsArray stack, int sp, int parCount) {
        switch (index) {
        case ID_ADD_GENERATE_RULE:
            JsFunction fn = (JsFunction) stack.getObject(sp + 2);
            ruleEvents.bindEvent("Generator.genChunk", fn);
            break;
        case ID_RANDOM:
            stack.setInt(sp, random.nextInt(stack.getInt(sp + 2)));
            break;
        default:
            super.evalNative(index, stack, sp, parCount);
        }
    }

    private Object[] int2obj(int[] a) {
        Object[] b = new Object[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i]=new Integer(a[i]);
        }
        return b;
    }

    public int runModGens(int id, int biom, int level, int x, int y, int z, int chunkX, int chunkZ) {
        Object[] params;
        if (biom == -1) {
            params = int2obj(new int[]{level,x,y,z,chunkX,chunkZ});
        } else {
            params = int2obj(new int[]{biom,level,x,y,z,chunkX,chunkZ});
        }
        ruleEvents.runEvent("Generator.genChunk", params);
        Object o = ruleEvents.getLastSuccess("Generator.genChunk");
        if (o == null) {
            return id;
        }
        try {
            return ((Double) o).intValue();
        } catch (ClassCastException e) {
            System.out.println("Cast fail for value "+o);
        }
        return id;
    }

}
