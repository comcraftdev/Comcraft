package net.comcraft.src;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

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
	    String function = _method.substring(_method.indexOf(".")+1,_method.lastIndexOf('.'));
	    String classname = _method.substring(0,_method.indexOf(function)-1).replace('/', '.');
        // temp location
	    if (classname.equals("net.comcraft.client.Block")) {
            if (function.equals("<init>")) {
                return new Block(((Integer) _arguments[0]).intValue());
            }
        }
        String fnargs= _method.substring(_method.indexOf("(")+1,_method.lastIndexOf(')'));
	    String returntype= _method.substring(_method.indexOf("("+fnargs+")")+fnargs.length()+2);
	    String a="[";
	    for (int i=0;i<_arguments.length;++i) {
	        a+=","+_arguments[i];
	    }
	    System.out.println(
	            "invoke: object='"+_object
	            +"' method='"+_method
	            +"' arguments="+a+"]");
	    if (_object != null) {
	        if(_object.getClass().getName().equals("com.igormaznitsa.mjvm.MJVMObject")) {
	            MJVMObject object=(MJVMObject) _object;
	            System.out.println(object);
	        }
	    }
	    Class classvar = null;
	    try {
            classvar = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            Object c= openClass(classname.replace('.', '/'));
            if (c != null) {
                MJVMClass cl= (MJVMClass) c;
                System.out.println("class="+cl);
               try {
                cl.newInstance(fnargs,_arguments);
            } catch (Throwable e1) {
                e1.printStackTrace();
            }
            }
            else {
                e.printStackTrace();
            }
        }
	    if (function.equals("<init>")) {
	        try {
                classvar.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                //class init takes arguments
                //e.printStackTrace();
            }
	    }
	    if (classname.equals("net.comcraft.client.Comcraft")) {
            if (function.equals("getImei")) {
                return net.comcraft.client.Comcraft.getImei();
            }
        }

	    if (classname.equals("java.io.PrintStream")) {
            if (function.equals("println")) {
                if (_object != null) {
                    ((PrintStream) _object).println(_arguments[0]);
                }
            }     
	    }
	    if (returntype.equals("V")) return null;
	    throw new RuntimeException("Unknown method");
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
		    Object c= openClass(_class);
        if (c != null) {
            MJVMClass cl= (MJVMClass) c;
            try {
                return cl.newInstance();
            } catch (Throwable e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
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
           if (_fieldidentifier.equals("java/lang/System.out.Ljava/io/PrintStream;")) {
               return System.out;
           }
           return null;
       }

       public void setStatic(MJVMClass _class, String _fieldidentifier, Object _value) {
           System.out.println("setStatic");
           return;
       }
       
    private Object openClass(String name) {
        try {
            FileConnection classfile = (FileConnection) Connector.open(path
                    + name + ".class", Connector.READ);
            if (!classfile.exists()) {
                return null;
            }
            InputStream file = classfile.openInputStream();
            MJVMClass c = new MJVMClass(file, this);
            file.close();
            classfile.close();
            return c;
        } catch (IOException e1) {
        }
        return null;

    }

   }
