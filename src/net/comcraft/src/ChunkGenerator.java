package net.comcraft.src;

import java.util.Random;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;

public abstract class ChunkGenerator extends JsObject {

    private static final int ID_ADD_GENERATE_RULE = 100;
    private static final int ID_RANDOM = 101;
    protected long seed;
    private JsArray[] rulestacks = new JsArray[16];
    protected Random random;

    public void newChunkGenerator(long seed) {
        this.seed = seed;
        random = new Random(seed);
    }

    public ChunkGenerator(JsObject objectPrototype) {
        super(OBJECT_PROTOTYPE);
        addNative("addGenerateRule", ID_ADD_GENERATE_RULE, 1);
        addNative("randNext", ID_RANDOM, 1);
    }
    public abstract ChunkStorage[] generateChunk(int x, int z);

    public void evalNative(int index, JsArray stack, int sp, int parCount) {
        switch (index) {
        case ID_ADD_GENERATE_RULE:
            if (parCount == 1) {
                JsFunction fn = (JsFunction) stack.getObject(sp + 2);
                JsArray stack1 = new JsArray();
                stack1.setObject(0, ModGlobals.global);
                stack1.setObject(1, ModGlobals.global);
                stack1.setObject(2, fn);
                rulestacks[0] = stack1;
            }
            break;
        case ID_RANDOM:
            stack.setInt(sp, random.nextInt(stack.getInt(sp+2)));
            break;
        default:
            super.evalNative(index, stack, sp, parCount);
        }
    }

    public int runModGens(int id, int biom,int level,  int x, int y, int z, int chunkX,int chunkZ) {
        try{
        for (int i = 0; i < rulestacks.length; i++) {
            if (rulestacks[i]==null){
                continue;
            }
            JsArray stack = rulestacks[i];
            //System.out.println("running, "+stack);
            stack.setInt(3, biom);
            stack.setInt(4, level);
            stack.setInt(5, x);
            stack.setInt(6, y);
            stack.setInt(7, z);
            stack.setInt(8, chunkX);
            stack.setInt(9, chunkZ);
            JsFunction fn = (JsFunction) stack.getObject(2);
            fn.eval(stack, 1, 7);
            //System.out.println(stack);
            Object o = stack.getObject(1);
            try {
                int v = ((Double) o).intValue();
                return v;
            } catch (ClassCastException e) {
                System.out.println("Could not cast "+o+" in "+stack);
            }
            catch (NullPointerException e) {
                //System.out.println("Could not find value in "+o);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        catch (Throwable e1) {
            e1.printStackTrace();
        }
        return id;
    }

}
