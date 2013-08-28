//CCMLGen: 11.1000,16,23
package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

class BaseMod extends JsObject {
    static final JsObject PROTOTYPE = new JsObject(OBJECT_PROTOTYPE);
    JsArray stack = new JsArray();

    public BaseMod() {
        super(PROTOTYPE);
        scopeChain = ModGlobals.createGlobal();
        addVar("Console", new DebugConsole());
        stack.setObject(0, this);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
}
