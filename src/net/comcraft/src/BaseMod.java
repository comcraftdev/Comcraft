//CCMLGen: 10.999,23,28
package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsSystem;

class BaseMod extends JsObject {
    static final JsObject PROTOTYPE = new JsObject(OBJECT_PROTOTYPE);
    JsArray stack = new JsArray();

	public static JsArray toArray(Object array) {
		JsArray out = new JsArray();
		for (int i=0;i<array.length;i++) {
			out.setObject(i, array[i]);
		}
		return out;
	}

    public BaseMod() {
        super(PROTOTYPE);
        scopeChain = JsSystem.createGlobal();
        stack.setObject(0, this);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }
}
