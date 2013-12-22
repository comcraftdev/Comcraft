package net.comcraft.src;

import com.google.minijoe.sys.JsArray;

public class ModArray extends JsArray {
    private Object[] source;

    public ModArray(Object[] sourcearray) {
        source = sourcearray;
        for (int i = 0; i < sourcearray.length; i++) {
            super.setObject(i, source[i]);
        }
    }

    public Object getArrayObject(int i) {
        return source[i];
    }

    public void setJsArrObject(int i, Object v) {
        super.setObject(i, v);
    }

    public void vmGetOperation(JsArray stack, int keyIndex, int valueIndex) {
        super.vmGetOperation(stack, keyIndex, valueIndex);
        int i = stack.getInt(keyIndex);
        stack.setObject(valueIndex, source[i]);
    }

    public void vmSetOperation(JsArray stack, int keyIndex, int valueIndex) {
        super.vmSetOperation(stack, keyIndex, valueIndex);
        source[stack.getInt(keyIndex)] = stack.getObject(valueIndex);
    }

    public static Object[] toArray(JsArray arr) {
        Object[] obj = new Object[arr.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = arr.getObject(i);
        }
        return obj;
    }

    public static int[] toIntArray(JsArray arr) {
        int[] obj = new int[arr.size()];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = arr.getInt(i);
        }
        return obj;
    }
}
