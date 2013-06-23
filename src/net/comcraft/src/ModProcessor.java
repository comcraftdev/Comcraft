package net.comcraft.src;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.igormaznitsa.mjvm.MJVMObject;
import com.igormaznitsa.mjvm.MJVMProcessor;
import com.igormaznitsa.mjvm.MJVMClass;
public class ModProcessor implements MJVMProcessor {
	private String path;

	public ModProcessor(String path) {
	    this.path=path;
	}
	public Object invoke(MJVMClass _class, Object _object, String _method, Object[] _arguments) {
	    String a="[";
	    for (int i=0;i<_arguments.length;++i) {
	        a+=","+_arguments[i];
	    }
	    System.out.println(
	            "invoke: object="+_object
	            +" method="+_method
	            +" arguments="+a+"]");
	    if(_object.getClass().getName().equals("com.igormaznitsa.mjvm.MJVMObject")) {
	        MJVMObject object=(MJVMObject) _object;
	        System.out.println(object);
	    }
	    if (_method.equals("java/lang/Object.<init>.()V")) {
	        return new Object();
	    }
	    return null;
	}
	public final Object newInstance(MJVMClass _source,String _class) {
	    String class_name=_class.replace('/', '.');
	    System.out.println("newInstance "+class_name);
	    try {
			return Class.forName(class_name).newInstance();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			try {
			FileConnection classfile = (FileConnection) Connector.open(path + _class+".class", Connector.READ);
            if (!classfile.exists()) {
                return null;
            }
            InputStream file=classfile.openInputStream();
            MJVMClass c=new MJVMClass(file,this);
            try {
                return c.newInstance();
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            file.close();
            classfile.close();
			// TODO Auto-generated catch block
            e.printStackTrace();
			}
			catch (IOException e1) {
			}
		}
		return null;
    }
       public Object[] newObjectArray(MJVMClass _source, String _class, int _length) {
           System.out.println("newObjectArray");
           return null;
       }

       public Object newMultidimensionObjectArray(MJVMClass _source, String _class, int[] _dimensions) {
           System.out.println("newMultidimensionObjectArray");
           return null;
       }

       public Object getField(MJVMClass _class, Object _object, String _fieldidentifier) {
           System.out.println("getField");
           return null;
       }

       public void setField(MJVMClass _class, Object _object, String _fieldidentifier, Object _value) {
           System.out.println("setField");
           return;
       }

       public Object getStatic(MJVMClass _class, String _fieldidentifier) {
           System.out.println("getStatic field "+_fieldidentifier);
           return null;
       }

       public void setStatic(MJVMClass _class, String _fieldidentifier, Object _value) {
           System.out.println("setStatic");
           return;
       }

   }
