package net.comcraft.src;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsException;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;

public class EventHandlerAPI extends JsObject {

    private EventHandler ehandle;
    private static final int ID_BIND_EVENT = 100;

    public EventHandlerAPI() {
        super(OBJECT_PROTOTYPE);
        addNative("bindEvent", ID_BIND_EVENT, 2);
        ehandle = new EventHandler(new String[] { "World.Generate", "Render.Init", "Game.Command" });
    }

    public void runEvent(String name, Object[] params) {
        ehandle.runEvent(name, params);
    }

    public void runEvent(String name) {
        ehandle.runEvent(name);
    }

    public Object getLastReturn(String name) {
        return ehandle.getLastReturn(name);
    }

    public Object getLastSuccess(String name) {
        return ehandle.getLastSuccess(name);
    }

    public Object getFirstSuccess(String name) {
        return ehandle.getFirstSuccess(name);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_BIND_EVENT:
            if (parCount != 2) {
                throw new JsException("Wrong parameter count");
            }
            String name = stack.getString(sp + 2);
            JsFunction function = (JsFunction) stack.getJsObject(sp + 3);
            ehandle.bindEvent(name, function);
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    public Object[] getSucesses(String name) {
        return ehandle.getSucesses(name);
    }
}
