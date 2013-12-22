//CCMLGen: 10.1001,20,31
package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

class BaseMod extends JsObject {
    static final JsObject PROTOTYPE = new JsObject(OBJECT_PROTOTYPE);
    private static final int ID_AXISALIGNEDBB = 1000;
    private static final int ID_BLOCK = 1001;
    JsArray stack = new JsArray();

    public BaseMod() {
        super(PROTOTYPE);
        scopeChain = ModGlobals.createGlobal();
        addVar("Console", new DebugConsole());
        addVar("EventHandler", ModGlobals.event);
        addVar("Model", new ModelsList());
        addNative("AxisAlignedBB", ID_AXISALIGNEDBB, 6);
        addNative("Block", ID_BLOCK, 1);
        stack.setObject(0, this);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_AXISALIGNEDBB:
            stack.setObject(sp, new AxisAlignedBB((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4), (float) stack.getNumber(sp + 5), (float) stack.getNumber(sp + 6), (float) stack.getNumber(sp + 7)));
            break;
        case ID_BLOCK:
            stack.setObject(sp, new Block(stack.getInt(sp + 2)));
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
}
