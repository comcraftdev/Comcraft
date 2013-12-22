// Copyright 2008 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.minijoe.sys;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/** 
 * Root class for all objects that are accessible via the JavaScript 
 * interpreter. 
 * 
 * Constants named ID_XXX are the identification codes for corresponding
 * native members, used in set() and evalNative().
 * 
 * This class also implements the container classes for numbers, booleans
 * and strings since the object constructor must be able to construct all
 * those classes and the string methods need to be transferable to other 
 * classes
 * 
 * @author Stefan Haustein
 */
public class JsObject  {

  /**
   * special function id for empty constructors, 
   * ignored in JsObject.evalNative()
   */
  public static final int ID_NOOP = -1;

  // Object methods
  
  protected static final int ID_INIT_OBJECT = 1;
  protected static final int ID_TO_STRING = 2;
  protected static final int ID_VALUE_OF = 3;
  protected static final int ID_TO_LOCALE_STRING = 4;
  protected static final int ID_HAS_OWN_PROPERTY = 5;
  protected static final int ID_IS_PROTOTYPE_OF = 6;
  protected static final int ID_PROPERTY_IS_ENUMERABLE = 7;
  
  // global functions  
  
  static final int ID_PARSE_INT = 10;
  static final int ID_PARSE_FLOAT = 11;
  static final int ID_IS_NAN = 12;
  static final int ID_IS_FINITE = 13;
  static final int ID_DECODE_URI = 14;
  static final int ID_DECODE_URI_COMPONENT = 15;
  static final int ID_ENCODE_URI = 16;
  static final int ID_ENCODE_URI_COMPONENT = 17;
  static final int ID_PRINT = 18;

  // math functions
  
  static final int ID_ABS = 19;
  static final int ID_ACOS = 20;
  static final int ID_ASIN = 21;
  static final int ID_ATAN = 22;
  static final int ID_ATAN2 = 23;
  static final int ID_CEIL = 24;
  static final int ID_COS = 25;
  static final int ID_EXP = 26;
  static final int ID_FLOOR = 27;
  static final int ID_LOG = 28;
  static final int ID_MAX = 29;
  static final int ID_MIN = 30;
  static final int ID_POW = 31;
  static final int ID_RANDOM = 32;
  static final int ID_ROUND = 33;
  static final int ID_SIN = 34;
  static final int ID_SQRT = 35;
  static final int ID_TAN = 36;
  
  // boolean / number
  
  static final int ID_INIT_BOOLEAN = 37;
  static final int ID_INIT_STRING = 38;
  
  // string methods
  
  static final int ID_FROM_CHAR_CODE = 39;
  
  static final int ID_CHAR_AT = 40;
  static final int ID_CHAR_CODE_AT = 41;
  static final int ID_CONCAT = 42;
  static final int ID_INDEX_OF = 43;  
  static final int ID_LAST_INDEX_OF = 44;
  static final int ID_LOCALE_COMPARE = 45;
  static final int ID_MATCH = 46;
  static final int ID_REPLACE = 47;
  static final int ID_SEARCH = 48;
  static final int ID_SLICE = 49;
  static final int ID_SPLIT = 50;
  static final int ID_SUBSTRING = 51;
  static final int ID_TO_LOWER_CASE = 52;
  static final int ID_TO_LOCALE_LOWER_CASE = 53;
  static final int ID_TO_UPPER_CASE = 54;
  static final int ID_TO_LOCALE_UPPER_CASE = 55;
  static final int ID_LENGTH = 56;
  static final int ID_LENGTH_SET = 57;

  static final int ID_INIT_NUMBER = 60;
  static final int ID_TO_FIXED = 61;
  static final int ID_TO_EXPONENTIAL = 62;
  static final int ID_TO_PRECISION = 63;

  static final int ID_PARSE = 64;
  static final int ID_UTC = 65;
  
  // constructors
  
  static final int ID_INIT_ARRAY = 66;
  static final int ID_INIT_ERROR = 67;
  static final int ID_INIT_FUNCTION = 68;
  static final int ID_INIT_DATE = 69;

  // math constants
  
  static final int ID_E = 70;
  static final int ID_E_SET = 71;
  static final int ID_LN10 = 72;
  static final int ID_LN10_SET = 73;
  static final int ID_LN2 = 74;
  static final int ID_LN2_SET = 75;
  static final int ID_LOG2E = 76;
  static final int ID_LOG2E_SET = 77;
  static final int ID_LOG10E = 78;
  static final int ID_LOG10E_SET = 79;
  static final int ID_PI = 80;
  static final int ID_PI_SET = 81;
  static final int ID_SQRT1_2 = 82;
  static final int ID_SQRT1_2_SET = 83;
  static final int ID_SQRT2 = 84;
  static final int ID_SQRT2_SET = 85;
  
  public static final int TYPE_UNDEFINED = 0;
  public static final int TYPE_NULL = 1;
  public static final int TYPE_OBJECT = 2;
  public static final int TYPE_BOOLEAN = 3;
  public static final int TYPE_NUMBER = 4;
  public static final int TYPE_STRING = 5;
  public static final int TYPE_FUNCTION = 6;
  
  /** Javascript type names as returned by the typeof operator. Note
   * that TYPE_NULL and TYPE_OBJECT are mapped to object both. */
  static final String[] TYPE_NAMES = {"undefined", "object", "object",
    "boolean", "number", "string", "function"
  };
  
  /** Placeholder for Javascript undefined (Java null) values in hashtables */
  private static final Object UNDEFINED_PLACEHOLDER = new Object();
  
  /** Prototype of all Javascript objects */
  public static final JsObject OBJECT_PROTOTYPE = 
      new JsObject(null)
      .addVar("toString", new JsFunction(ID_TO_STRING, 0))
      .addVar("valueOf", new JsFunction(ID_VALUE_OF, 0))
      .addVar("toLocaleString", new JsFunction(ID_TO_LOCALE_STRING, 0))
      .addVar("hasOwnProperty", new JsFunction(ID_HAS_OWN_PROPERTY, 1))
      .addVar("isPrototypeOf", new JsFunction(ID_IS_PROTOTYPE_OF, 1))
      .addVar("propertyIsEnumerable", 
          new JsFunction(ID_PROPERTY_IS_ENUMERABLE, 1))
      ;
  
  public static final JsObject BOOLEAN_PROTOTYPE = 
      new JsObject(OBJECT_PROTOTYPE);

  public static final JsObject NUMBER_PROTOTYPE = 
      new JsObject(OBJECT_PROTOTYPE)
      .addVar("toFixed", new JsFunction(ID_TO_FIXED, 1))
      .addVar("toExponential", new JsFunction(ID_TO_EXPONENTIAL, 1))
      .addVar("toPrecision", new JsFunction(ID_TO_PRECISION, 1))
      ;

  public static final JsObject STRING_PROTOTYPE = 
      new JsObject(OBJECT_PROTOTYPE)
      .addVar("charAt", new JsFunction(ID_CHAR_AT, 1))
      .addVar("charCodeAt", new JsFunction(ID_CHAR_CODE_AT, 1))
      .addVar("concat", new JsFunction(ID_CONCAT, 1))
      .addVar("indexOf", new JsFunction(ID_INDEX_OF, 2))
      .addVar("lastIndexOf", new JsFunction(ID_LAST_INDEX_OF, 2))
      .addVar("localeCompare", new JsFunction(ID_LOCALE_COMPARE, 1))
      .addVar("replace", new JsFunction(ID_REPLACE, 2))
      .addVar("search", new JsFunction(ID_SEARCH, 1))
      .addVar("slice", new JsFunction(ID_SLICE, 2))
      .addVar("split", new JsFunction(ID_SPLIT, 2))
      .addVar("substring", new JsFunction(ID_SUBSTRING, 2))
      .addVar("toLowerCase", new JsFunction(ID_TO_LOWER_CASE, 0))
      .addVar("toLocaleLowerCase", new JsFunction(ID_TO_LOCALE_LOWER_CASE, 0))
      .addVar("toUpperCase", new JsFunction(ID_TO_UPPER_CASE, 0))
      .addVar("toLocaleUpperCase", new JsFunction(ID_TO_LOCALE_UPPER_CASE, 0))
      .addVar("length", new JsFunction(ID_LENGTH, -1))
      ;
  
  public static final Object[] NO_PARAM = new Object[0];

  /** Prototype chain */
  protected JsObject __proto__;
  
  /** Hashtable holding the reverse mapping for native methods. */
  private Hashtable natives = new Hashtable(10);
  /** Hashtable holding the properties and values of this object. */
  private Hashtable data;
  /** Parent object in scope chain */
  protected JsObject scopeChain;

  /** Primitive object value, used for Number and String. */
  Object value;

  /** 
   * Constructs a new Javascript object with the given prototype. 
   */
  public JsObject(JsObject __proto__){
    this.__proto__ = __proto__;
  }

  /** 
   * Return the raw value of a property, taking the prototype chain into account, but not the
   * scope chain or native getters or setters.
   */
  public Object getRawInPrototypeChain(String key){
    Object result;
    
    if (data != null) {
      result = data.get(key);
      if (result != null) {
        return result == UNDEFINED_PLACEHOLDER ? null : result;
      }
    }
    
    if (__proto__ != null) {
      result = __proto__.getRawInPrototypeChain(key);
      if (result != null){
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the given property, taking native getters into account.
   * 
   * @param prop Name of the property
   * @return stored value or null
   */
  public Object getObject(String prop){
    Object v = getRawInPrototypeChain(prop);
    if (v instanceof JsFunction){
      JsFunction nat = (JsFunction) v;
      if (nat.getParameterCount() == -1){
        JsArray stack = new JsArray();
        evalNative(nat.index, stack, 0, 0);
        return stack.getObject(0);
      }
    } else if (v == null && scopeChain != null) {
      v = scopeChain.getObject(prop);
    }
    
    return v;
  }
  
  /**
   * Returns the numeric value for the given key. If the actual value is not
   * numeric, it is converted automatically, using the ECMA 262 conversion 
   * rules.
   */
  public final double getNumber(String key) {
    return JsSystem.toNumber(getObject(key));
  }

  /**
   * Returns the string value for the given key. If the actual value is not a 
   * string, it is converted automatically, using the ECMA 262 conversion 
   * rules.
   */
  public final String getString(String key){
    return JsSystem.toString(getObject(key));
  }
  
  /** 
   * Returns the value at array index i, casted to an integer (32 bit). 
   */
  public final int getInt(String key) {
    return (int) getNumber(key);
  }
  
  /** 
   * Set method called from the byte code interpreter, 
   * avoiding temporary stack creation. This method is
   * overwritten in JsArray. 
   */
  public void vmSetOperation(JsArray stack, int keyIndex, int valueIndex){
    String key = stack.getString(keyIndex);

// TODO re-enable optimization 
//    Object old = getRaw(key);
//
//    if (old instanceof JsFunction){
//      JsFunction nat = (JsFunction) old;
//      if (nat.getParameterCount() == -1){
//        evalNative(nat.index + 1, stack, valueIndex, 0);
//        return;
//      }
//    }

    setObject(key, stack.getObject(valueIndex));
  }

  /** 
   * Get method called from the bytecode interpreter,  avoiding temporary stack 
   * creation. This method is overwritten in JsArray and JsArguments.
   */
  public void vmGetOperation(JsArray stack, int keyIndex, int valueIndex){
    String key = stack.getString(keyIndex);

 // TODO re-enable optimization 
//    Object old = getRaw(key);
//
//    if (old instanceof JsFunction){
//      JsFunction f = (JsFunction) old;
//      if (f.getParameterCount() == -1){
//        evalNative(f.index, stack, valueIndex, 0);
//        return;
//      }
//    }
    stack.setObject(valueIndex, getObject(key));
  }

  /**
   * Set the given property to the given value without taking the scope or
   * prototype chain into account.
   * 
   * @param prop property name
   * @param value value to set
   * @return this (for chained calls)
   */
  public JsObject addVar(String prop, Object v){
    if (data == null){
      data = new Hashtable();
    }
    data.put(prop, v == null ? UNDEFINED_PLACEHOLDER : v);
    if (v instanceof JsFunction && ((JsFunction) v).index != ID_NOOP) {
      String key = getNativeKey(((JsFunction) v).factoryTypeId, ((JsFunction) v).index);
      if(key != null) {
        if(natives.containsKey(key)) {
          System.out.println("Duplicate native function ID '" +
            ((JsFunction) v).index + "' detected for method '" + prop + "'.");
        } else {
          natives.put(key, prop);
        }
      }
    }
    return this;
  }

  /** 
   * Convenience method for <tt>addVar(prop, new JsFunction(int nativeCallId, int parCount)</tt>
   */
  public JsObject addNative(String prop, int nativePropertyId, int parCount) {
    return addVar(prop, new JsFunction(nativePropertyId, parCount));
  }

  /**
   * Get the function's name for a particular ID.
   */
  public String getFunctionName(int factoryTypeId, int index) {
    return getFunctionNameImpl(getNativeKey(factoryTypeId, index));
  }

  private String getFunctionNameImpl(String key) {
    String prop = (String) natives.get(key);
    if (prop == null && __proto__ != null) {
      prop = __proto__.getFunctionNameImpl(key);
    }
    return prop;
  }

  private String getNativeKey(int factoryTypeId, int index) {
    return (factoryTypeId <= JsSystem.FACTORY_ID_OBJECT) ?
      JsSystem.FACTORY_ID_OBJECT + ":" + index : factoryTypeId + ":" + index;
  }

  /** 
   * Set the given property to a numeric value.
   */
  public void setNumber(String key, double n) {
    setObject(key, new Double(n));
  }
  
  /**
   * Sets the given property to the given value, taking the prototype chain,
   * scope chain, and setters into account.
   * 
   * @param prop property name
   * @param value value to set
   * @return this (for chained calls)
   */
  public void setObject(String key, Object v){

    Object old = getRawInPrototypeChain(key);
    if (old instanceof JsFunction 
          && ((JsFunction) old).getParameterCount() == -1) {
        JsFunction nat = (JsFunction) old;
        JsArray stack = new JsArray();
        stack.setObject(0, v);
        evalNative(nat.index + 1, stack, 0, 0);
        return;
    } else if (old == null && scopeChain != null) {
      scopeChain.setObject(key, v);
    } else {
      if (data == null) {
        data = new Hashtable();
      }
      data.put(key, v == null ? UNDEFINED_PLACEHOLDER : v);
    }
  }

  /** 
   * Returns a key enumeration for this object only, not including the 
   * prototype or scope chain.
   */
  public Enumeration keys(){
    if(data == null) {
      data = new Hashtable();
    }
    return data.keys();
  }

  /**
   * Returns elements enumeration for this object only, not including the
   * prototype or scope chain.
   */
  public Enumeration elements(){
    if(data == null) {
      data = new Hashtable();
    }
    return data.elements();
  }

  /**
   * Returns a string representation of this.
   */
  public String toString(){
    return value == null ? "[object Object]" : value.toString();
  }

  /**
   * Delete the given property. Returns true if it was actually deleted.
   */
  public boolean delete(String key){
    if (data == null) {
      return true;
    }
    
    //TODO check whether this covers dontdelete sufficiently
  
    Object old = data.get(key);
    boolean isFunc = old instanceof JsFunction;
    if (isFunc && ((JsFunction) old).getParameterCount() == -1){
      return false;
    }

    data.remove(key);
    if(isFunc) {
        natives.remove(getNativeKey(((JsFunction) old).factoryTypeId, ((JsFunction) old).index));
    }
    return true;
  }

  /**
   * Clears all properties.
   */
  public void clear(){
    if(data != null) {
      data.clear();
      data = null;
    }
  }

  /** 
   * Execute java member implementation. Parameters for functions start at 
   * stack[sp+2]. Function and getter results are returned at stack[sp+0].
   * The assignement value for a setter is stored at stack[sp+0]. 
   */
  public void evalNative(int index, JsArray stack, int sp, int parCount) {
    Object obj;
    switch(index) {
      // object methods
      
      case ID_NOOP:
        break;
        
      case ID_INIT_OBJECT:
        obj = stack.getObject(sp + 2);
        if (isConstruction(stack, sp)){
          if (obj instanceof Boolean || obj instanceof Double ||
              obj instanceof String) {
            value = obj;
          } else if (obj instanceof JsObject){
            stack.setObject(sp - 1,  obj);
          }
          // otherwise, don't do anything -- regular constructor call
        } else {
          if (obj == null || obj == JsSystem.JS_NULL) {
            stack.setObject(sp, new JsObject(OBJECT_PROTOTYPE));
          } else {
            stack.setObject(sp, JsSystem.toJsObject(obj));
          }
        }
        break;
        
      case ID_TO_STRING:
      case ID_TO_LOCALE_STRING:
        stack.setObject(sp, JsSystem.toString(stack.getObject(sp)));
        break;

      case ID_HAS_OWN_PROPERTY:
        stack.setBoolean(sp, data != null && 
            data.get(stack.getString(sp + 2)) != null);
        break;
        
      case ID_IS_PROTOTYPE_OF:
        obj = stack.getObject(sp + 2);
        stack.setBoolean(sp, false);
        while (obj instanceof JsObject){
          if (obj == this) {
            stack.setBoolean(sp, true);
            break;
          }
        }
        break;
        
      case ID_PROPERTY_IS_ENUMERABLE:
        obj = getRawInPrototypeChain(stack.getString(sp + 2));
        stack.setBoolean(sp, obj != null && !(obj instanceof JsFunction));
        break;
        
      case ID_VALUE_OF:
        stack.setObject(sp, value == null ? this : value);
        break;

        // Number methods
        
      case ID_INIT_NUMBER:
        if (isConstruction(stack, sp)) {
          value = new Double(stack.getNumber(sp + 2));
        } else {
          stack.setNumber(sp, stack.getNumber(sp + 2));
        }
        break;

        // Boolean methods

      case ID_INIT_BOOLEAN:
        if (isConstruction(stack, sp)) {
          value = stack.getBoolean(sp + 2) ? Boolean.TRUE : Boolean.FALSE;
        } else {
          stack.setObject(sp, stack.getBoolean(sp + 2) 
              ? Boolean.TRUE : Boolean.FALSE);
        }
        break;

      case ID_INIT_STRING:
        if (isConstruction(stack, sp)) {
          value = stack.getString(sp +2);
        } else {
          stack.setObject(sp, parCount == 0 ? "" : stack.getString(sp + 2));
        }
        break;

      // initializers that can be used as functions need to be covered in Object
        
      case ID_INIT_ARRAY:
        JsArray array = (isConstruction(stack, sp)
          ? (JsArray) this : new JsArray());
        if (parCount == 1 && stack.isNumber(sp + 2)) {
          array.setSize(stack.getInt(sp + 2));
        } else {
          for (int i = 0; i < parCount; i++) {
            stack.copy(sp + i + 2, array, i);
          }
        }
        stack.setObject(sp, array);
        break;
        
      case ID_INIT_ERROR:
        if (isConstruction(stack, sp)) {
          setObject("message", stack.getString(sp + 2));
        } else {
          stack.setObject(sp, new JsError(stack.getJsObject(sp), 
              stack.getString(sp + 2)));
        }
        break;
        
        
      case ID_INIT_FUNCTION:
        // Note: this will exchange the "this" object at sp-1 if it is used as constructor
        boolean construction = isConstruction(stack, sp);
        
        StringBuffer buf = new StringBuffer("(function(");
        for(int i = 0; i < parCount-1; i++) {
          if(i != 0) {
            buf.append(',');
          }
          buf.append(stack.getString(sp + 2 + i));
        }
        buf.append("){");
        if(parCount != 0) {
          buf.append(stack.getString(sp + 2 + parCount - 1));
        }
        buf.append("});");
        
        System.out.println("eval: "+buf);
        
        JsObject global = (JsObject) stack.getObject(0);
        JsFunction eval = (JsFunction) global.getObject("eval");
        stack.setObject(sp, global); // global
        stack.setObject(sp + 1, eval);
        stack.setObject(sp + 2, buf.toString());
        eval.eval(stack, sp, 1);
        
        if(construction) {
          stack.copy(sp, stack, sp-1);
        }
        break;
      
        
      case ID_INIT_DATE:
        // reset to defaults
        if (isConstruction(stack, sp)){
          JsDate d = (JsDate) this;
          if (parCount == 1) {
            d.time.setTime(new Date((long) stack.getNumber(sp + 2)));
          } else if (parCount > 1){
            d.time.setTime(new Date(0));
            int year = stack.getInt(sp + 2);
            if (year >= 0 && year <= 99) { 
              year += 1900;
            }
            d.setDate(false, year, 
                           stack.getNumber(sp + 3), 
                           stack.getNumber(sp + 4));
            
            d.setTime(false, stack.getNumber(sp + 5), 
                  stack.getNumber(sp + 6), 
                  stack.getNumber(sp + 7),
                  stack.getNumber(sp + 8));
          }
        } else {
          stack.setObject(sp, 
              new JsDate(JsDate.DATE_PROTOTYPE).toString(true, true, true));
        }
        break;
   
        
      // global properties
        
      case ID_PRINT:
        System.out.println(stack.getString(sp + 2));
        break;
        
      case ID_PARSE_INT:
        String s = stack.getString(sp + 2).trim().toLowerCase();
        try {
          if (stack.isNull(sp + 3)) {
            stack.setInt(sp, s.startsWith("0x") 
                ? Integer.parseInt(s.substring(2), 16)
                    : Integer.parseInt(s));
          } else {
            stack.setInt(sp, Integer.parseInt(s, stack.getInt(sp + 3)));
          }
        } catch (NumberFormatException e) {
          stack.setInt(sp, 0);
        }
        break;
        
      case ID_PARSE_FLOAT:
        try {
          stack.setNumber(sp, Double.parseDouble(stack.getString(sp + 2)));
        } catch (NumberFormatException e) {
          stack.setNumber(sp, Double.NaN);
        }
        break;
        
      case ID_IS_NAN:
        stack.setBoolean(sp, Double.isNaN(stack.getNumber(sp + 2)));
        break;

      case ID_IS_FINITE:
        double d = stack.getNumber(sp + 2);
        stack.setBoolean(sp, !Double.isInfinite(d) && !Double.isNaN(d));
        break;

      case ID_DECODE_URI:
        obj = stack.getObject(sp + 2);
        if(obj instanceof byte[]) {
          stack.setObject(sp, JsSystem.decodeURI((byte[]) obj));
        } else {
          stack.setObject(sp, JsSystem.decodeURI(obj.toString()));
        }
        break;

      case ID_ENCODE_URI:
        obj = stack.getObject(sp + 2);
        if(obj instanceof byte[]) {
          stack.setObject(sp, JsSystem.encodeURI((byte[]) obj));
        } else {
          stack.setObject(sp, JsSystem.encodeURI(obj.toString()));
        }
        break;

      //TODO Implement
      case ID_DECODE_URI_COMPONENT:
      case ID_ENCODE_URI_COMPONENT:
        throw new RuntimeException("NYI");
        
      // math properties
        
      case ID_ABS:
        stack.setNumber(sp, Math.abs(stack.getNumber(sp + 2)));
        break;

      case ID_ACOS:
      case ID_ASIN:
      case ID_ATAN:
      case ID_ATAN2:
        throw new RuntimeException("NYI");
          
      case ID_CEIL:
        stack.setNumber(sp, Math.ceil(stack.getNumber(sp + 2)));
        break;
        
      case ID_COS:
        stack.setNumber(sp, Math.cos(stack.getNumber(sp + 2)));
        break;
        
      case ID_EXP:
        stack.setNumber(sp, JsSystem.exp(stack.getNumber(sp + 2)));
        break;
        
      case ID_FLOOR:
        stack.setNumber(sp, Math.floor(stack.getNumber(sp + 2)));
        break;
        
      case ID_LOG:
        stack.setNumber(sp, JsSystem.ln(stack.getNumber(sp + 2)));
        break;
        
      case ID_MAX:
        d = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < parCount; i++){
          d = Math.max(d, stack.getNumber(sp + 2 + i));
        }
        stack.setNumber(sp, d);
        break;

      case ID_MIN:
        d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < parCount; i++){
          d = Math.min(d, stack.getNumber(sp + 2 + i));
        }
        stack.setNumber(sp, d);
        break;

      case ID_POW:
        stack.setNumber(sp, JsSystem.pow(stack.getNumber(sp + 2), 
            stack.getNumber(sp + 3)));
        break;
        
      case ID_RANDOM:
        stack.setNumber(sp, JsSystem.random.nextDouble());
        break;

      case ID_ROUND:
        stack.setNumber(sp, Math.floor(stack.getNumber(sp + 2) + 0.5));
        break;

      case ID_SIN:
        stack.setNumber(sp, Math.sin(stack.getNumber(sp + 2)));
        break;

      case ID_SQRT:
        stack.setNumber(sp, Math.sqrt(stack.getNumber(sp + 2)));
        break;

      case ID_TAN:
        stack.setNumber(sp, Math.tan(stack.getNumber(sp + 2)));
        break;

      // string methods
        
      case ID_FROM_CHAR_CODE:
        char[] chars = new char[parCount];
        for (int i = 0; i < parCount; i++) {
          chars[i] = (char) stack.getInt(sp + 2 + i);
        }
        stack.setObject(sp, new String(chars));
        break;
        
      // string.prototype methods
        
      case ID_CHAR_AT:
        s = stack.getString(sp);
        int i = stack.getInt(sp + 2);
        stack.setObject(sp, i < 0 || i >= s.length() 
            ? "" : s.substring(i, i + 1));
        break;
        
      case ID_CHAR_CODE_AT:
        s = stack.getString(sp);
        i = stack.getInt(sp + 2);
        stack.setNumber(sp, i < 0 || i >= s.length() 
            ? Double.NaN : s.charAt(i));
        break;
        
      case ID_CONCAT:
        buf = new StringBuffer(stack.getString(sp));
        for (i = 0; i < parCount; i++) {
          buf.append(stack.getString(sp + i + 2));
        }
        stack.setObject(sp, buf.toString());
        break;
        
      case ID_INDEX_OF:
        stack.setNumber(sp, stack.getString(sp).indexOf(stack.getString(sp+2), 
            stack.getInt(sp + 3)));
        break;
        
      case ID_LAST_INDEX_OF:
        s = stack.getString(sp);
        String find = stack.getString(sp + 2);
        d = stack.getNumber(sp + 3);
        int max = (Double.isNaN(d)) ? s.length() : (int) d;
        
        int best = -1;
        while (true) {
          int found = s.indexOf(find, best + 1);
          if (found == -1 || found > max){
            break;
          }
          best = found;
        }
        
        stack.setNumber(sp, best);
        break;
        
      case ID_LOCALE_COMPARE:
        stack.setNumber(sp, 
            stack.getString(sp).compareTo(stack.getString(sp + 2)));
        break;
        
      case ID_REPLACE:
        s = stack.getString(sp);
        find = stack.getString(sp + 2);
        String replace = stack.getString(sp + 3);
        if(!find.equals("")) {
          StringBuffer sb = new StringBuffer(s);
          int length = find.length();

          // Parse nodes into vector
          while ((index = sb.toString().indexOf(find)) >= 0) {
            sb.delete(index, index + length);
            sb.insert(index, replace);
          }
          stack.setObject(sp, sb.toString());
          sb = null;
        }
        break;
      case ID_MATCH:
      case ID_SEARCH:
        throw new RuntimeException("Regexp NYI");
        
      case ID_SLICE:
        s = stack.getString(sp);
        int len = s.length();
        int start = stack.getInt(sp + 2);
        int end = stack.isNull(sp + 3) ? len : stack.getInt(sp + 3);
        if (start < 0) {
          start = Math.max(len + start, 0);
        }
        if (end < 0) {
          end = Math.max(len + start, 0);
        }
        if (start > len){
          start = len;
        }
        if (end > len) {
          end = len;
        }
        if (end < start) {
          end = start;
        }
        stack.setObject(sp, s.substring(start, end));
        break;
        
      case ID_SPLIT:
        s = stack.getString(sp);
        String sep = stack.getString(sp + 2);
        double limit = stack.getNumber(sp + 3);
        if (Double.isNaN(limit) || limit < 1) {
          limit = Double.MAX_VALUE;
        }
        
        JsArray a = new JsArray();
        if (sep.length() == 0) {
          if(s.length() < limit) {
            limit = s.length();
          }
          for (i = 0; i < limit; i++) {
            a.setObject(i, s.substring(i, i+1));
          }
        }
        else {
          int cut0 = 0;
          while(cut0 < s.length() && a.size() < limit) {
            int cut = s.indexOf(sep, cut0);
            if(cut == -1) { 
              cut = s.length();
            }
            a.setObject(a.size(), s.substring(cut0, cut));
            cut0 = cut + sep.length();
          }
        }
        stack.setObject(sp, a);
        break;
        
      case ID_SUBSTRING:
        s = stack.getString(sp);
        len = s.length();
        start = stack.getInt(sp + 2);
        end = stack.isNull(sp + 3) ? len : stack.getInt(sp + 3);
        if (start > end){
          int tmp = end;
          end = start;
          start = tmp;
        }
        start = Math.min(Math.max(0, start), len);
        end = Math.min(Math.max(0, end), len);
        stack.setObject(sp, s.substring(start, end));
        break;

      case ID_TO_LOWER_CASE: //TODO: which locale to use as defautlt? us?
      case ID_TO_LOCALE_LOWER_CASE:
        stack.setObject(sp, stack.getString(sp + 2).toLowerCase());
        break;

      case ID_TO_UPPER_CASE: //TODO: which locale to use as defautlt? us?
      case ID_TO_LOCALE_UPPER_CASE:
        stack.setObject(sp, stack.getString(sp + 2).toUpperCase());
        break;

      case ID_LENGTH:
        stack.setInt(sp, toString().length());
        break;
        
      case ID_LENGTH_SET:
        // cannot be changed!
        break;

      case ID_TO_EXPONENTIAL:
      case ID_TO_FIXED:
      case ID_TO_PRECISION:
        stack.setObject(sp, JsSystem.formatNumber(index, 
            stack.getNumber(sp + 2), stack.getNumber(sp + 3)));
        break;
        
      case ID_UTC:
        JsDate date = new JsDate(JsDate.DATE_PROTOTYPE);
        date.time.setTime(new Date(0));
        int year = stack.getInt(sp + 2);
        if (year >= 0 && year <= 99) { 
          year += 1900;
        }
        date.setDate(true, year, 
                       stack.getNumber(sp + 3), 
                       stack.getNumber(sp + 4));
        
        date.setTime(true, stack.getNumber(sp + 5), 
              stack.getNumber(sp + 6), 
              stack.getNumber(sp + 7),
              stack.getNumber(sp + 8));

        stack.setNumber(sp, date.time.getTime().getTime());
        break;
        
      case ID_PARSE:
        double[] vals = {Double.NaN, Double.NaN, Double.NaN, 
            Double.NaN, Double.NaN, Double.NaN, Double.NaN};
        
        s = stack.getString(sp + 2);
        int curr = -1;
        int pos = 0;
        for (i = 0; i < s.length(); i++){
          char c = s.charAt(i);
          if (c >= '0' && c <= '9'){
            if (curr == -1){
              curr = c - 48; 
            } else {
              curr = curr * 10 + (c - 48);
            }
          } else if (curr != -1){
            if (pos < vals.length) {
              vals[pos++] = curr;
            }
            curr = -1;
          }
        }
        if (curr != -1 && pos < vals.length) {
          vals[pos++] = curr;
        }
        
        boolean utc = s.endsWith("GMT") || s.endsWith("UTC");
        date = new JsDate(JsDate.DATE_PROTOTYPE);
        date.time.setTime(new Date(0));
        date.setDate(utc, vals[0], vals[1], vals[2]);
        date.setTime(utc, vals[3], vals[4], vals[5], vals[6]);
        stack.setNumber(sp, date.time.getTime().getTime());
        break;
        
      // Math constants
        
      case ID_E:
        stack.setNumber(sp, Math.E);
        break;
      case ID_LN10:
        stack.setNumber(sp, 2.302585092994046);
        break;
      case ID_LN2:
        stack.setNumber(sp, JsSystem.LN2);
        break;
      case ID_LOG2E:
        stack.setNumber(sp, 1.4426950408889634);
        break;
      case ID_LOG10E:
        stack.setNumber(sp, 0.4342944819032518);
        break;
      case ID_PI:
        stack.setNumber(sp, Math.PI);
        break;
      case ID_SQRT1_2:
        stack.setNumber(sp, Math.sqrt(0.5));
        break;
      case ID_SQRT2:
        stack.setNumber(sp, Math.sqrt(2.0));
        break;
        
      case ID_E_SET:
      case ID_LN10_SET:
      case ID_LN2_SET:
      case ID_LOG2E_SET:
      case ID_LOG10E_SET:
      case ID_PI_SET:
      case ID_SQRT1_2_SET:
      case ID_SQRT2_SET:
        // dont do anything: cannot overwrite those values!
        break;  
               
      default:
        throw new IllegalArgumentException("Unknown native id: " + index 
            + " this: " + this);
    }
  }

  /** 
   * Determines whether the given call is an actual constructor call 
   * (with new)
   * TODO check whether there may be false positives...
   */
  protected boolean isConstruction(JsArray stack, int sp) {
    return sp > 0 && stack.getObject(sp - 1) == stack.getObject(sp);
  }
}
