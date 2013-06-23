/*
 * MJVM - Copyright (C) 2009  Igor Maznitsa www.igormaznitsa.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.igormaznitsa.mjvm;

/**
 * This interface describes a processor which gets signals from a MJVMClass object and processes them
 *
 * @author Igor Maznitsa (igor.maznitsa@igormaznitsa.com)
 * @version 1.00
 */
public interface MJVMProcessor
{
    /**
     * Invoke an object method
     * @param _class a MJVMClass object which has called the method, must not be null
     * @param _object an object which method should be invoked, may be null
     * @param _method ID of the method in the format <SUN_class_name>.<method_name>.<method_signature>, must not be null
     * @param _arguments the argument array for the method, may be null if the method doesn't need arguments
     * @return the result as Object or null if the method has a void as the result
     */
    public Object invoke(MJVMClass _class,Object _object, String _method, Object[] _arguments);
    
    /**
     * Make a new instance of a class
     * @param _source a MJVMClass object which has called the method, must not be null
     * @param _class a class name in the SUN class name format, must not be null
     * @return new instance of the class as Object, must not be null
     */
    public Object newInstance(MJVMClass _source,String _class);

    /**
     * Create an object array
     * @param _source a MJVMClass object which has called the method, must not be null
     * @param _class a class name in the SUN class name format, must not be null. We have to make an array supports the class
     * @param _length the length of the array, must not be negative
     * @return new one-dimension array for the length and the class
     */
    public Object [] newObjectArray(MJVMClass _source,String _class,int _length);
    
    /**
     * Create a multidimension array
     * @param _source a MJVMClass object which has called the method, must not be null
     * @param _class a class name in the SUN class name format, must not be null. We have to make an array supports the class
     * @param _dimensions an array contains a length for every dimension of new array, must not be null
     * @return new multidimension array for the class 
     */
    public Object newMultidimensionObjectArray(MJVMClass _source,String _class,int [] _dimensions);

    /**
     * Get a value from an object field
     * @param _class  a MJVMClass object which has called the method, must not be null
     * @param _object an object contains the field
     * @param _fieldidentifier the UID for the field in the format <SUN_class_name>.<method_name>.<method_signature>, must not be null
     * @return an object from the field or null
     */
    public Object getField(MJVMClass _class,Object _object, String _fieldidentifier);

    /**
     * Set a value to a field
     * @param _class a MJVMClass object which has called the method, must not be null
     * @param _object an object contains the field
     * @param _fieldidentifier the UID for the field in the format <SUN_class_name>.<method_name>.<method_signature>, must not be null
     * @param _value the value to be placed in the field, can be null
     */
    public void setField(MJVMClass _class,Object _object, String _fieldidentifier, Object _value);
    
    /**
     * Get a value from a static field
     * @param _class a MJVMClass object which has called the method, must not be null
     * @param _fieldidentifier  the UID for the field in the format <SUN_class_name>.<method_name>.<method_signature>, must not be null
     * @return the field value as an Object or null
     */
    public Object getStatic(MJVMClass _class,String _fieldidentifier);

    /**
     * Set a value to a static field
     * @param _class  a MJVMClass object which has called the method, must not be null
     * @param _fieldidentifier  the UID for the field in the format <SUN_class_name>.<method_name>.<method_signature>, must not be null
     * @param _value the value to be placed in the field, can be null
     */
    public void setStatic(MJVMClass _class,String _fieldidentifier, Object _value);
    
}
