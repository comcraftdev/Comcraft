package net.comcraft.src;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsException;
import com.google.minijoe.sys.JsFunction;

public class EventHandler {

    private Hashtable events;

    public EventHandler(String[] eventnames) {
        events = new Hashtable();
        for (int i = 0; i < eventnames.length; i++) {
            Vector[] v = new Vector[2];
            v[0] = new Vector();
            v[1] = new Vector();
            events.put(eventnames[i], v);
        }
    }

    public void runEvent(String name, Object[] params) {
        if (params == null) {
            params = new Object[0];
        }
        Vector[] event = (Vector[]) events.get(name);
        if (event[0].isEmpty()) {
            return;
        }
        Enumeration e = event[0].elements();
        while (e.hasMoreElements()) {
            JsFunction fn = (JsFunction) e.nextElement();
            JsArray stack = new JsArray();
            stack.setObject(0, ModGlobals.global); // Global scope
            stack.setObject(1, fn); // Function
            stack.setObject(2, fn); // Local scope
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Integer) {
                    stack.setInt(i + 3, ((Integer) params[i]).intValue());
                } else if (params[i] instanceof Boolean) {
                    stack.setBoolean(i + 3, ((Boolean) params[i]).booleanValue());
                } else {
                    stack.setObject(i + 3, params[i]);
                }
            }
            fn.eval(stack, 1, params.length);
            event[1].setElementAt(stack.getObject(1), event[0].indexOf(fn));
        }
    }

    public void runEvent(String name) {
        runEvent(name, null);
    }

    public void bindEvent(String name, JsFunction function) {
        if (!events.containsKey(name)) {
            throw new JsException("Unknown Event key " + name);
        }
        Vector[] e = (Vector[]) events.get(name);
        e[0].addElement(function);
        e[1].addElement(null);
    }

    public Object getLastReturn(String name) {
        return ((Vector[]) events.get(name))[1].lastElement();
    }

    public Object getLastSuccess(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        Enumeration e = event.elements();
        Object r = null;
        while (e.hasMoreElements()) {
            Object t = e.nextElement();
            if (t != null) {
                r = t;
            }
        }
        return r;
    }

    public Object getFirstSuccess(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        Enumeration e = event.elements();
        while (e.hasMoreElements()) {
            Object r = e.nextElement();
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public Object[] getSucesses(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        Enumeration e = event.elements();
        Vector v = new Vector();
        while (e.hasMoreElements()) {
            Object r = e.nextElement();
            if (r != null) {
                v.addElement(r);
            }
        }
        Object[] arr = new Object[v.size()];
        v.copyInto(arr);
        return arr;
    }
}
