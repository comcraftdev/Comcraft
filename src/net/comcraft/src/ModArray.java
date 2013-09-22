package net.comcraft.src;

import com.google.minijoe.sys.JsArray;

public class ModArray extends JsArray {
	private Object[] source;

	public ModArray(Object[] sourcearray) {
		source=sourcearray;
		for (int i=0; i<sourcearray.length; i++) {
			super.setObject(i, source[i]);
		}
	}
	public void setObject(int i, Object v) {
		source[i]=v;
	}
	public Object getObject(int i) {
		return source[i];
	}
	public void vmSetOperation(JsArray stack, int keyIndex, int valueIndex) {
		super.vmSetOperation(stack, keyIndex, valueIndex);
		setObject(stack.getInt(keyIndex), stack.getObject(valueIndex));
	}
}