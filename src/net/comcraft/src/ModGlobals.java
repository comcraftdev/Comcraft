package net.comcraft.src;

import com.google.minijoe.sys.JsObject;
import com.google.minijoe.sys.JsObjectFactory;
import com.google.minijoe.sys.JsSystem;

public class ModGlobals implements JsObjectFactory {
    public static JsObject global;

    static ModGlobals instance = new ModGlobals();

    public static EventHandlerAPI event = new EventHandlerAPI();

    public static JsObject createGlobal() {
        return JsSystem.createGlobal();
    }

    public JsObject newInstance(int type) {
        switch (type) {
        default:
            throw new IllegalArgumentException();
        }
    }

}
