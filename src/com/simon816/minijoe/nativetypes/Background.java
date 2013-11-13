package com.simon816.minijoe.nativetypes;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public class Background extends JsObject {
    private static final int ID_SET_COLOR = 100;
    private static final int ID_GET_COLOR = 101;
    private javax.microedition.m3g.Background bg;

    public Background() {
        super(OBJECT_PROTOTYPE);
        this.bg = new javax.microedition.m3g.Background();
        addNative("setColor", ID_SET_COLOR, 1);
        addNative("getColor", ID_GET_COLOR, 0);
    }

    public void setColor(int c) {
        _getBackground().setColor(c);
    }

    public int getColor() {
        return _getBackground().getColor();
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_SET_COLOR:
            setColor(stack.getInt(sp + 2));
            break;
        case ID_GET_COLOR:
            stack.setInt(sp, getColor());
            break;
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    public javax.microedition.m3g.Background _getBackground() {
        return bg;
    }
}
