package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public class DebugConsole extends JsObject {

    private static final int ID_LOG = 100;

    public DebugConsole() {
        super(JsObject.OBJECT_PROTOTYPE);
        addNative("log", ID_LOG, 1);
    }
    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch(id) {
        case ID_LOG:
            System.out.println(stack.getObject(sp+2));
            break;
        default:

            super.evalNative(id, stack, sp, parCount);
        }
    }

}
