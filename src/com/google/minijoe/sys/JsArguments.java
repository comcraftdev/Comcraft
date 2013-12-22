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

/** 
 * Class representing the Javascript arguments object providing access to
 * anonymous parameters.

 * @author Stefan Haustein
 */
public class JsArguments extends JsObject {

  private static final JsObject ARGUMENTS_PROTOTYPE = 
    new JsObject(JsObject.OBJECT_PROTOTYPE);
  
  private JsFunction function;
  private JsObject context;
  
  /**
   * Creates a new arguments object.
   * 
   * @param fn the function this arguments object holds the parameters for
   * @param context the evaluation context 
   */
  public JsArguments(JsFunction fn, JsObject context) {
    super(ARGUMENTS_PROTOTYPE);
    this.function = fn;
    this.context = context;
  }
  
  /** 
   * Set method called from the byte code interpreter, 
   * avoiding temporary stack creation. This method is
   * overwritten in JsArray. 
   */
  public void vmSetOperation(JsArray stack, int keyIndex, int valueIndex){
    String key = stack.getString(keyIndex);
    Object old = getRawInPrototypeChain(key);

    if (old instanceof Integer){
      stack.setObject(keyIndex, 
          function.localNames[((Integer) old).intValue()]);
      context.vmSetOperation(stack, keyIndex, valueIndex);
    } else {
      super.vmSetOperation(stack, keyIndex, valueIndex);
    }
  }

  /** 
   * Get method called from the bytecode interpreter,  avoiding temporary stack 
   * creation. This method is overwritten in JsArray and JsArguments.
   */
  public void vmGetOperation(JsArray stack, int keyIndex, int valueIndex){
    String key = stack.getString(keyIndex);
    Object old = getRawInPrototypeChain(key);

    if (old instanceof Integer){
      stack.setObject(keyIndex, 
          function.localNames[((Integer) old).intValue()]);
      context.vmGetOperation(stack, keyIndex, valueIndex);
    } else {
      super.vmGetOperation(stack, keyIndex, valueIndex);
    }
  }
  
  public String toString() {
    return "[object Arguments]";
  }
}
