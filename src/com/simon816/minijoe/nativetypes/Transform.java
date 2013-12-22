package com.simon816.minijoe.nativetypes;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsObject;

public class Transform extends JsObject {
    private static final int ID_POST_TRANSLATE = 100;
    private static final int ID_POST_ROTATE = 101;
    private static final int ID_POST_SCALE = 102;
    private static final int ID_SET = 103;
    private javax.microedition.m3g.Transform tr;

    public Transform() {
        super(OBJECT_PROTOTYPE);
        this.tr = new javax.microedition.m3g.Transform();
        addNative("postTranslate", ID_POST_TRANSLATE, 3);
        addNative("postRotate", ID_POST_ROTATE, 4);
        addNative("postScale", ID_POST_SCALE, 3);
        addNative("set", ID_SET, 1);
    }

    public Transform(javax.microedition.m3g.Transform transform) {
        this();
        this.tr = transform;
    }

    public void postTranslate(float arg0, float arg1, float arg2) {
        this.tr.postTranslate(arg0, arg1, arg2);
    }

    public void postRotate(float arg0, float arg1, float arg2, float arg3) {
        this.tr.postRotate(arg0, arg1, arg2, arg3);
    }

    public void set(Transform transform) {
        this.tr.set(transform._getTransform());
    }

    public void postScale(float arg0, float arg1, float arg2) {
        this.tr.postScale(arg0, arg1, arg2);
    }

    public void evalNative(int id, JsArray stack, int sp, int parCount) {
        switch (id) {
        case ID_POST_ROTATE:
            postRotate((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4), (float) stack.getNumber(sp + 5));
            break;
        case ID_POST_TRANSLATE:
            postTranslate((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4));
            break;
        case ID_POST_SCALE:
            postScale((float) stack.getNumber(sp + 2), (float) stack.getNumber(sp + 3), (float) stack.getNumber(sp + 4));
            break;
        case ID_SET:
            set((Transform) stack.getObject(sp + 2));
        default:
            super.evalNative(id, stack, sp, parCount);
        }
    }

    public javax.microedition.m3g.Transform _getTransform() {
        return tr;
    }
}