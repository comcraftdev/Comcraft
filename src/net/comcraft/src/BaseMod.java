//CCMLGen: 11.1000,16,23
package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsSystem;

class BaseMod extends JsObject {
    static final JsObject PROTOTYPE = new JsObject(OBJECT_PROTOTYPE);
    private static final int ID_BLOCK = 1000;
    JsArray stack = new JsArray();

    public BaseMod() {
        super(PROTOTYPE);
        scopeChain = JsSystem.createGlobal();
        addVar("Block", new JsFunction(ID_BLOCK, 1));
        stack.setObject(0, this);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_BLOCK:
            stack.setObject(sp, new Block(stack.getInt(sp+2)));
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
}
