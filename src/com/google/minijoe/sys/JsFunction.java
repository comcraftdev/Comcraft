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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Javascript function and bytecode interpreter implementation.
 *
 * @author Stefan Haustein
 */
public class JsFunction extends JsObject {

  public static final int OP_NOP = 0x00;
  public static final int OP_ADD = 0x01;
  public static final int OP_AND = 0x02;
  public static final int OP_APPEND = 0X03;
  public static final int OP_ASR = 0x04;
  public static final int OP_ENUM = 0x05;
  public static final int OP_IN = 0x06;
  public static final int OP_DIV = 0x07;
  public static final int OP_DUP = 0x08;
  public static final int OP_EQEQ = 0x09;
  public static final int OP_CTX_GET = 0x0a;
  public static final int OP_GET = 0x0b;
  public static final int OP_CTX = 0x0c;
  public static final int OP_DEL = 0x0d;
  public static final int OP_GT  = 0x0e;
  public static final int OP_THROW = 0x0F;

  public static final int OP_INC = 0x10;
  public static final int OP_DEC = 0x11;
  public static final int OP_LT  = 0x12;
  public static final int OP_MOD = 0x13;
  public static final int OP_MUL = 0x14;
  public static final int OP_NEG  = 0x15;
  public static final int OP_NEW_ARR = 0x16;
  public static final int OP_NEW_OBJ = 0x17;
  public static final int OP_NEW  = 0x18;
  public static final int OP_NOT = 0x19;
  public static final int OP_OR  = 0x1a;
  public static final int OP_DROP = 0x1b;
  public static final int OP_PUSH_TRUE = 0x1c;
  public static final int OP_PUSH_FALSE = 0x1d;
  public static final int OP_RET  = 0x1e;
  public static final int OP_CTX_SET = 0x1f;

  public static final int OP_SET_KC  = 0x020;
  public static final int OP_SET  = 0x021;
  public static final int OP_SHL = 0x022;
  public static final int OP_SHR = 0x023;
  public static final int OP_SUB = 0x024;
  public static final int OP_SWAP =  0x025;
  public static final int OP_PUSH_THIS = 0x026;
  public static final int OP_PUSH_NULL = 0x027;
  public static final int OP_PUSH_UNDEF = 0x028;
  public static final int OP_DDUP = 0x29;
  public static final int OP_ROT = 0x2A;
  public static final int OP_EQEQEQ = 0x2B;    //TODO
  public static final int OP_XOR = 0x2C;
  public static final int OP_INV = 0x2D;
  public static final int OP_WITH_START = 0x2E;
  public static final int OP_WITH_END = 0x2F;

  public static final int OP_ABOVE = 0x30;
  public static final int OP_INSTANCEOF = 0x31;
  public static final int OP_TYPEOF = 0x32;
  public static final int OP_PUSH_GLOBAL = 0x33;

  public static final int XOP_TRY_CALL = 0xE6 >>> 1;
  public static final int XOP_ADD = 0xE8 >>> 1;  // add immediate to stacktop
  public static final int XOP_PUSH_FN = 0xEA >>> 1;
  public static final int XOP_PUSH_NUM = 0xEC >>> 1;
  public static final int XOP_GO = 0xEE >>> 1;
  public static final int XOP_IF = 0xF0 >>> 1;
  public static final int XOP_CALL = 0xF2 >>> 1;
//                           unused: 0x0F4
  public static final int XOP_LCL_GET = 0xF6 >>> 1;
  public static final int XOP_LCL_SET = 0xF8 >>> 1;
  public static final int XOP_NEXT = 0xFA >>> 1;
  public static final int XOP_PUSH_INT = 0xFC >>> 1;
  public static final int XOP_PUSH_STR = 0xFE >>> 1;

  public static final int BLOCK_COMMENT = 0x00;
  public static final int BLOCK_GLOBAL_STRING_TABLE = 0x10;
  public static final int BLOCK_NUMBER_LITERALS = 0x20;
  public static final int BLOCK_STRING_LITERALS = 0x30;
  public static final int BLOCK_FUNCTION_LITERALS = 0x50;
  public static final int BLOCK_LOCAL_VARIABLE_NAMES = 0x60;
  public static final int BLOCK_BYTE_CODE = 0x80;
  public static final int BLOCK_LINE_NUMBERS = 0xe0;
  public static final int BLOCK_DEBUG_DATA = 0xf0;

  static final int END_MARKER = 0x0ff;

  static final int ID_PROTOTYPE = 100;
  static final int ID_PROTOTYPE_SET = 101;
  static final int ID_APPLY = 102;

  public static final JsObject FUNCTION_PROTOTYPE =
    new JsObject(OBJECT_PROTOTYPE)
        .addVar("prototype", new JsFunction(ID_PROTOTYPE, -1))
        .addVar("length", new JsFunction(ID_LENGTH, -1))
        .addVar("apply", new JsFunction(ID_APPLY, 1))
        ;

  /** Number of declared parameters; -1 for native getter/setter */
  int expectedParameterCount;

  /** Number of local variables  */
  private int varCount;

  /** Byte code containing the implementation of this function */
  private byte[] byteCode;

  /** native method index if this function is implemented in Java */
  int index;

  String[] localNames;

  /** String literal table, used when putting strings on the stack. */
  private String[] stringLiterals;

  /** function literal table, used when putting strings on the stack. */
  private JsFunction[] functionLiterals;

  /** number literal table, used when putting strings on the stack. */
  private double[] numberLiterals;

  /**
   * Prototype object if this function is a constructor. Currently not used;
   * required to implement the JS prototype property.
   */
  private JsObject prototype;

  /** Factory to use if this is a native constructor. */
  private JsObjectFactory factory;

  /** Object factory id if this is a native constructor. */
  int factoryTypeId;

  /** Evaluation context for this function. */
  private JsObject context;

  private int[] lineNumbers;
  
  /**
   * Parses the given stream and runs the main function
   * @throws IOException
   */
  public static Object exec(DataInputStream dis, JsObject context)
      throws IOException{

    //TODO check magic
    for (int i = 0; i < 8; i++){
      dis.read();
    }

    JsFunction main = new JsFunction(new JsFunction(dis, null), context);

    JsArray stack = new JsArray();
    stack.setObject(0, context);
    stack.setObject(1, context);
    stack.setObject(2, main);

    main.eval(stack, 1, 0);

    return stack.getObject(3);
  }

  /**
   * Constructor for functions implemented in Java.
   */
  public JsFunction(int index, int parCount) {
    super(FUNCTION_PROTOTYPE);
    this.index = index;
    this.expectedParameterCount = parCount;
  }

  /**
   * Creates a new function from the given function literal and context.
   */
  public JsFunction(JsFunction literal, JsObject context){
    super(literal.__proto__);
    this.byteCode = literal.byteCode;
    this.context = context;
    this.functionLiterals = literal.functionLiterals;
    this.localNames = literal.localNames;
    this.numberLiterals = literal.numberLiterals;
    this.expectedParameterCount = literal.expectedParameterCount;
    this.prototype = literal.prototype;
    this.stringLiterals = literal.stringLiterals;
    this.varCount = literal.varCount;
    this.factory = JsSystem.getInstance();
    this.factoryTypeId = JsSystem.FACTORY_ID_OBJECT;
    this.lineNumbers = literal.lineNumbers;
  }


  /**
   * Constructor for constructors implemented in Java
   *
   * @param factory factory instance that is able to create new objects
   * @param factoryTypeId instance type id, used by the factory
   * @param prototype the prototype object
   * @param index the ID of the function call, used in evalNative
   * @param parCount
   */
  public JsFunction(JsObjectFactory factory, int factoryTypeId,
      JsObject prototype, int nativeId, int parCount) {
    this(nativeId, parCount);
    this.prototype = prototype;
    this.factory = factory;
    this.factoryTypeId = factoryTypeId;
  }

  /**
   * Constructs a function literal from the serialized binary form including the
   * string table. Please note that function literals cannot be invoked
   * directly, a function must be created from this function literal using the
   * corresponding constructor. If the global string table is null, the file
   * header is expected.
   *
   * @throws IOException thrown for underlying stream IO errors
   */
  public JsFunction(DataInputStream dis, String[] globalStringTable)
      throws IOException {
    super(FUNCTION_PROTOTYPE);
    // __proto__ above, prototype below...
    this.prototype = new JsObject(OBJECT_PROTOTYPE);

    byte[] buf = null;
    int flags = 0;

    loop:
    while (true){
      int blockType = dis.read();
      int count;
      switch(blockType){
      case BLOCK_COMMENT:
        count = dis.readUnsignedShort();
        if (buf == null || buf.length < count){
          buf = new byte[count];
        }
        dis.readFully(buf, 0, count);
        break;
      case BLOCK_GLOBAL_STRING_TABLE:
        count = dis.readUnsignedShort();
        globalStringTable = new String[count];
        for (int i = 0; i < count; i++){
          globalStringTable[i] = dis.readUTF();
        }
        break;
      case BLOCK_STRING_LITERALS:
        count = dis.readUnsignedShort();
        stringLiterals = new String[count];
        for (int i = 0; i < count; i++){
          stringLiterals[i] = globalStringTable[dis.readShort()];
        }
        break;
      case BLOCK_NUMBER_LITERALS:
        count = dis.readUnsignedShort();
        numberLiterals = new double[count];
        for (int i = 0; i < count; i++){
          numberLiterals[i] = dis.readDouble();
        }
        break;
      case BLOCK_FUNCTION_LITERALS:
        count = dis.readUnsignedShort();
        functionLiterals = new JsFunction[count];
        for (int i = 0; i < count; i++){
          functionLiterals[i] = new JsFunction(dis, globalStringTable);
        }
        break;
      case BLOCK_LOCAL_VARIABLE_NAMES:
        count = dis.readUnsignedShort();
        localNames = new String[count];
        for (int i = 0; i < count; i++) {
          localNames[i] = globalStringTable[dis.readShort()];
        }
        break;
      case BLOCK_BYTE_CODE:
        varCount = dis.readUnsignedShort();
        expectedParameterCount = dis.readUnsignedShort();
        varCount -= expectedParameterCount;
        flags = dis.read();
        byteCode = new byte[dis.readShort()];
        dis.readFully(byteCode);
        break;
      case BLOCK_LINE_NUMBERS:
        count = dis.readUnsignedShort();
        lineNumbers = new int[count * 2];
        for (int i = 0; i < count; i++) {
          lineNumbers[i << 1] = dis.readUnsignedShort();
          lineNumbers[(i << 1) + 1] = dis.readUnsignedShort();
        }
        break;
      case END_MARKER:
        break loop;
      default:
        throw new IOException("Illegal Block type "
            + Integer.toString(blockType, 16));
      }
    }

    if ((flags & 1) == 0) {
      if (localNames == null) {
        localNames = new String[0];
      }
    } else {
      localNames = null;
    }
  }

  /**
   * Evaluate this function. The this-pointer, function object and parameters
   * must be on stack (sp + 0 = context, sp + 1=function, sp + 2 = first param
   * etc.). The result is expected at sp + 0.
   */
  public void eval(JsArray stack, int sp, int actualParameterCount) {
    for (int i = actualParameterCount; i < expectedParameterCount; i++) {
      stack.setObject(sp + i + 2, null);
    }

    JsObject thisPtr = stack.getJsObject(sp);

    if (byteCode == null) {
      thisPtr.evalNative(index, stack, sp, actualParameterCount);
      return;
    }

    // sp initially points to context
    // bp points to parameter 0. context is at bp-2, lambda at bp-1

    sp += 2;
    int bp = sp;

    JsObject context;

    // note: arguments available here only!
    if (localNames != null){
      context = new JsObject(JsObject.OBJECT_PROTOTYPE);
      context.scopeChain = this.context;
      JsArguments args = new JsArguments(this, context);
      for (int i = 0; i < expectedParameterCount; i++) {
        context.addVar(localNames[i], stack.getObject(sp + i));
        args.addVar("" + i, new Integer(i));
      }
      for (int i = expectedParameterCount; i < this.localNames.length; i++) {
        context.addVar(localNames[i], null);
      }
      for (int i = expectedParameterCount; i < actualParameterCount; i++) {
        args.setObject(""+i, stack.getObject(bp+i));
      }
      args.setNumber("length", actualParameterCount);
      args.setObject("callee", this);
      context.addVar("arguments", args);
    } else {
      context = this.context;
      sp += expectedParameterCount + varCount;
    }

    int initialSp = sp;
    int opcode;
    byte[] byteCode = this.byteCode;
    int pc = 0;
    int end = byteCode.length;

    try {
      while (pc < end) {
        opcode = byteCode[pc++];

        if (opcode < 0){
          int imm;

          if ((opcode & 1) == 0){
            imm = byteCode[pc++];
          } else {
            imm =  (byteCode[pc] << 8) | (byteCode[pc + 1] & 255);
            pc += 2;
          }

          switch ((opcode & 0x0ff) >>> 1) {
            case XOP_ADD:
              stack.setNumber(sp - 1, stack.getNumber(sp - 1) + imm);
              break;

            case XOP_TRY_CALL:
              try {
                sp = sp - imm - 2; // on stack: context, lambda, params
                JsFunction m = (JsFunction) stack.getObject(sp + 1);
                m.eval(stack, sp, imm);
                stack.setBoolean(sp + 1, true);
                sp += 2;
              } catch (JsException e) {
                stack.setObject(sp++, e.getError());
                stack.setBoolean(sp++, false); // not successfull
              } catch (Exception e) {
                stack.setObject(sp++, new JsError(e));
                stack.setBoolean(sp++, false); // not successfull
              }
              break;

            case XOP_CALL:
              sp = sp - imm - 2; // on stack: context, lambda, params
              JsFunction m = (JsFunction) stack.getObject(sp + 1);
              m.eval(stack, sp++, imm);
              // System.out.println("Ret val received: "
              //  + stack.getObject(sp-1)+" sp: "+sp);
              break;

            case XOP_PUSH_FN:
              stack.setObject(sp++,
                  new JsFunction(functionLiterals[imm], context));
              break;

            case XOP_GO:
              pc += imm;
              break;

            case XOP_IF:
              if (!stack.getBoolean(--sp)) {
                pc += imm;
              }
              break;

            case XOP_PUSH_INT:
              stack.setNumber(sp++, imm);
              break;

            case XOP_LCL_GET:
              stack.copy(bp + imm, stack, sp++);
              break;

//          case XOP_LOCAL_INC:
//          stack.setFP(sp - 1, stack.getFP(sp - 1)
//          + stack.getFP(bp + imm));
//          // fall-through!
            case XOP_LCL_SET:
              stack.copy(sp - 1, stack, bp + imm);
              break;

            case XOP_NEXT:
              Enumeration e = (Enumeration) stack.getObject(sp - 1);
              if (e.hasMoreElements()){
                stack.setObject(sp++, e.nextElement());
              } else {
                pc += imm;
              }
              break;
            case XOP_PUSH_NUM:
              stack.setNumber(sp++, numberLiterals[imm]);
              break;

            case XOP_PUSH_STR:
              // System.out.println("String:" + stringList[(int)param]);
              stack.setObject(sp++, stringLiterals[imm]);
              break;

            default:
              throw new RuntimeException("Illegal opcode: "
                  + Integer.toString(opcode&0xff, 16) + " par: " + imm);
          } // switch
        } else {
          switch (opcode) {

            case OP_ADD:
              if (stack.isNumber(sp - 2) && stack.isNumber(sp - 1)) {
                stack.setNumber(sp - 2,
                    stack.getNumber(sp - 2) + stack.getNumber(sp - 1));
              } else {
                stack.setObject(sp - 2,
                    stack.getString(sp - 2)  +  stack.getString(sp - 1));
              }
              sp--;
              break;

            case OP_AND:
              stack.setNumber(sp - 2, stack.getInt(sp - 2) & stack.getInt(sp - 1));
              sp--;
              break;


            case OP_APPEND:
              JsArray arr = (JsArray) stack.getObject(sp - 2);
              stack.copy(sp - 1, arr, arr.size());
              // ((Array)
              // stack.getObject(sp-2)).addElement(stack.getObject(sp-1));
              sp--;
              break;

            case OP_ASR:
              stack.setNumber(sp - 2,
                  (stack.getInt(sp - 2) & 0xffffffffL) >>> (stack.getInt(sp - 1) & 0x1f));
              sp--;
              break;

            case OP_CTX_GET:
              context.vmGetOperation(stack, sp - 1, sp - 1);
              break;

            case OP_CTX_SET:
              context.vmSetOperation(stack, sp - 1, sp - 2);
              sp--; // take away name, not value
              break;

            case OP_CTX:
              stack.setObject(sp++, context);
              break;

            case OP_DEC:
              stack.setNumber(sp - 1, stack.getNumber(sp - 1) - 1);
              break;

            case OP_DEL:
              stack.setBoolean(sp - 2,  stack.getJsObject(sp - 2).delete(stack.getString(sp - 1)));
              sp--;
              break;

            case OP_DIV:
              stack.setNumber(sp - 2, stack.getNumber(sp - 2) / stack.getNumber(sp - 1) );
              sp--;
              break;

            case OP_DROP:
              sp--;
              break;

            case OP_DUP:
              stack.copy(sp - 1, stack, sp);
              sp++;
              break;

            case OP_DDUP:
              stack.copy(sp - 2, stack, sp, 2);
              sp += 2;
              break;

            case OP_ENUM:
              stack.setObject(sp-1, ((JsObject) stack.getObject(sp-1)).keys());
              break;

            case OP_EQEQEQ:
              if (stack.getType(sp - 2) != stack.getType(sp - 1)){
                sp--;
                stack.setObject(sp - 1, Boolean.FALSE);
                break;
              }
              // fall-trough

            case OP_EQEQ:
              // System.out.println(""+stack.getObject(sp-2)+ " = "+
              // stack.getObject(sp-1));

              int tX = stack.getType(sp - 2);
              int tY = stack.getType(sp - 1);

              if (tX == tY) {
                switch(tX) {
                  case TYPE_UNDEFINED:
                  case TYPE_NULL:
                    stack.setObject(sp - 2, Boolean.TRUE);
                    break;

                  case TYPE_NUMBER:
                    stack.setBoolean(sp - 2,
                        stack.getNumber(sp - 2) == stack.getNumber(sp - 1));
                    break;

                  default:
                    stack.setBoolean(sp - 2,
                        stack.getObject(sp - 2).equals(
                            stack.getObject(sp - 1)));
                }
              } else {
                boolean result;
                if ((tX == TYPE_UNDEFINED && tY == TYPE_NULL) ||
                    (tX == TYPE_NULL && tY == TYPE_UNDEFINED)) {
                  result = true;
                } else if (tX == TYPE_NUMBER || tY == TYPE_NUMBER) {
                  result = stack.getNumber(sp - 2) == stack.getNumber(sp - 1);
                } else if ((tX == TYPE_STRING && tY == TYPE_OBJECT) ||
                    tX == TYPE_OBJECT && tY == TYPE_STRING) {
                  result = stack.getString(sp - 2)
                      .equals(stack.getString(sp - 1));
                } else {
                  result = false;
                }
                stack.setBoolean(sp - 2, result);
              }
              sp--;
              break;

            case OP_GET:
              JsObject ctx = stack.getJsObject(sp - 2);
//          System.out.println("GetMember ctx: "+ctx);
//          System.out.println("GetMember name: " + stack.getObject(sp - 1));
              ctx.vmGetOperation(stack, sp - 1, sp - 2);
              sp--;
              break;

            case OP_GT:
              if (stack.isNumber(sp - 2) && stack.isNumber(sp - 1)) {
                stack.setObject(sp - 2,
                    stack.getNumber(sp - 2) > stack.getNumber(sp - 1)
                      ? Boolean.TRUE
                      : Boolean.FALSE);
              } else {
                stack.setObject(sp - 2,
                    stack.getString(sp - 2).compareTo(stack.getString(sp - 1)) 
                    > 0 ? Boolean.TRUE : Boolean.FALSE);
              }
              sp--;
              break;

            case OP_IN:
              Object o = stack.getObject(sp - 1);
              if (o instanceof JsArray && stack.isNumber(sp - 2)) {
                int i = stack.getInt(sp - 2);
                stack.setObject(sp -2,
                    i >= 0 && i <= ((JsArray) o).size()
                        ? Boolean.TRUE
                        : Boolean.FALSE);
                sp--;
                break;
              }
              if (o instanceof JsObject) {
                stack.setObject(sp - 2,
                    ((JsObject) o).getRawInPrototypeChain(stack.getString(sp-2)) == null
                        ? Boolean.TRUE
                        : Boolean.FALSE);
                sp--;
                break;
              }
              stack.setObject(sp - 2, Boolean.FALSE);
              sp--;
              break;

            case OP_INC:
              stack.setNumber(sp - 1, stack.getNumber(sp - 1) + 1);
              break;

            case OP_INV:
              stack.setInt(sp - 1, ~stack.getInt(sp - 1));
              break;

            case OP_LT:
              if (stack.isNumber(sp - 2) && stack.isNumber(sp - 1)) {
                stack.setObject(sp - 2, stack.getNumber(sp - 2) < 
                    stack.getNumber(sp - 1) ? Boolean.TRUE : Boolean.FALSE);
              } else {
                stack.setObject(sp - 2, stack.getString(sp - 2).
                    compareTo(stack.getString(sp - 1)) < 0
                    ? Boolean.TRUE : Boolean.FALSE);
              }
              sp--;
              break;
              
            case OP_MOD:
              stack.setNumber(sp - 2,
                  (stack.getNumber(sp - 2) % (stack.getNumber(sp - 1))));
              sp--;
              break;

            case OP_MUL:
              stack.setNumber(sp - 2, stack.getNumber(sp - 2) * stack
                  .getNumber(sp - 1));
              sp--;
              break;

            case OP_NEW_ARR:
              stack.setObject(sp++, new JsArray());
              break;

            case OP_NEW:
              JsFunction constructor = ((JsFunction) stack.getObject(sp - 1));
              ctx = constructor.factory.newInstance(constructor.factoryTypeId);
              stack.setObject(sp - 1, ctx);
              stack.setObject(sp++, ctx);
              stack.setObject(sp++, constructor);
              break;

            case OP_NEW_OBJ:
              stack.setObject(sp++, new JsObject(OBJECT_PROTOTYPE));
              break;

            case OP_NEG:
              stack.setNumber(sp - 1, -stack.getNumber(sp - 1));
              break;

            case OP_NOT:
              stack.setObject(sp - 1, stack.getBoolean(sp - 1) ? Boolean.FALSE
                  : Boolean.TRUE);
              break;

            case OP_OR:
              stack.setNumber(sp - 2, stack.getInt(sp - 2) | stack.getInt(sp - 1));
              sp--;
              break;

            case OP_PUSH_FALSE:
              stack.setObject(sp++, Boolean.FALSE);
              break;

            case OP_PUSH_GLOBAL:
              stack.setObject(sp++, stack.getObject(0));
              break;

            case OP_PUSH_NULL:
              stack.setObject(sp++, JsSystem.JS_NULL);
              break;

            case OP_PUSH_THIS:
              stack.setObject(sp++, thisPtr);
              break;

            case OP_PUSH_TRUE:
              stack.setObject(sp++, Boolean.TRUE);
              break;

            case OP_PUSH_UNDEF:
              stack.setObject(sp++, null);
              break;

            case OP_RET:
              // System.out.println("sp: "+sp+" returning:
              // "+stack.getObject(sp-1));
              stack.copy(sp - 1, stack, bp - 2);
              return;

            case OP_ROT:
              stack.copy(sp - 3, stack, sp - 2, 3);
              stack.copy(sp, stack, sp - 3);
              break;

            case OP_SET_KC:
              // ctx: sp-3
              // property name: sp-2
              // value to set: sp-1;

              ctx = stack.getJsObject(sp - 3);
              ctx.vmSetOperation(stack, sp - 2, sp - 1);

              // key = (String) stack.getObject(sp-2);
              // Object curr = ctx.getRaw(key);
              // System.out.println("SetMember KC ctx: "+ctx);
              // System.out.println("SetMember name: "+stack.getObject(sp-2));
              // System.out.println("SetMember value: "+stack.getObject(sp-1));

              sp -= 2; // leave value on the stack(!)
              break;

            case OP_SET:
              ctx = stack.getJsObject(sp - 2);
              ctx.vmSetOperation(stack, sp - 1, sp - 3);

              // key = (String) stack.getObject(sp-1);
              // curr = ctx.getRaw(key);

              // System.out.println("SetMember KV ctx: "+ctx);
              // System.out.println("SetMember name: "+stack.getObject(sp-1));
              // System.out.println("SetMember value: "+stack.getObject(sp-3));

              sp -= 2; // leave value on the stack(!)
              break;

            case OP_SHR:
              stack.setNumber(sp - 2, stack.getInt(sp - 2) >> (stack.getInt(sp - 1) & 0x1f));
              sp--;
              break;

            case OP_SHL:
              stack.setNumber(sp - 2, stack.getInt(sp - 2) << (stack.getInt(sp - 1) & 0x1f));
              sp--;
              break;

            case OP_SUB:
              stack.setNumber(sp - 2, stack.getNumber(sp - 2) - stack.getNumber(sp - 1));
              sp--;
              break;

            case OP_SWAP:
              stack.swap(sp - 1, sp - 2);
              break;

            case OP_THROW:
              // line number is added in try..catch below
              throw new JsException(stack.getJsObject(sp));

            case OP_WITH_START:
              JsObject nc = new JsObject((JsObject) stack.getObject(sp - 1));
              nc.scopeChain = context;
              context = nc;
              sp--;
              break;

            case OP_WITH_END:
              context = context.scopeChain;
              break;

            case OP_TYPEOF:
              stack.setObject(sp - 1, TYPE_NAMES[stack.getType(sp-1)]);
              break;

            case OP_INSTANCEOF:
              o = stack.getObject(sp - 2);
              JsObject p = stack.getJsObject(sp - 1);
              if (p instanceof JsFunction && o instanceof JsObject) {
                JsObject j = ((JsObject) o);
                p = ((JsFunction) p).prototype;
                while (j.__proto__ != null && j.__proto__ != p){
                  j = j.__proto__;
                }
                stack.setBoolean(sp - 2, j != null);
              } else {
                stack.setObject(sp - 2, Boolean.FALSE);
              }
              sp--;
              break;

            case OP_XOR:
              stack.setNumber(sp - 2, stack.getInt(sp-2) ^ stack.getInt(sp-1));
              sp--;
              break;

            default:
              throw new RuntimeException("Illegal opcode: '" + ((char) opcode)
                  + "'/" + opcode);
          }
        }
      }
    } catch (Exception e) {
      JsException jse;
      if(e instanceof JsException) {
        jse = (JsException) e;
      } else {
        e.printStackTrace();
        jse = new JsException(e);
      }
      if(jse.pc == -1) {
        jse.pc = pc - 1;
        jse.lineNumber = getLineNumber(pc - 1);
      }
      throw jse;
    } 

    if (sp == initialSp + 1) {
//    System.out.println("sp: "+sp+" returning: "+stack.getObject(sp-1));
      stack.copy(sp - 1, stack, bp - 2);
    } else if (sp == initialSp) {
//    System.out.println("sp: "+sp+" returning NULL");
      stack.setObject(bp - 2, null);
    } else {
      throw new RuntimeException("too much or too little on the stack; sp: "
          + sp + " bp: " + bp + " varCount: " + varCount + " parCount: "
          + actualParameterCount);
    }
    return;
  }

  int getLineNumber(int pc) {
    if(lineNumbers != null && lineNumbers.length > 0) {
      int i = 0;
      while(i + 2 < lineNumbers.length && lineNumbers[i+2] <= pc) {
        i += 2;
      }
      return lineNumbers[i + 1];
    }
    return -1;
  }
  
  
  /**
   * Returns the number of expected (declared) parameters.
   */
  public int getParameterCount(){
    return expectedParameterCount;
  }

  public void evalNative(int id, JsArray stack, int sp, int pc) {
    switch(id){
      case ID_PROTOTYPE:
        stack.setObject(sp, prototype);
        break;

      case ID_PROTOTYPE_SET:
        break;

      case ID_LENGTH:
        stack.setNumber(sp, pc);
        break;

      case ID_APPLY:
        throw new RuntimeException("NYI");

      default:
        super.evalNative(id, stack, sp, pc);
    }
  }

  /**
   * Returns a string representation of this function.
   */
  public String toString() {
    StringBuffer buf = new StringBuffer("function(");
    for (int i = 0; i < expectedParameterCount; i++) {
      if (i > 0){
        buf.append(", ");
      }
      buf.append(localNames == null ? ("p" + i) : localNames[i]);
    }
    buf.append(") { [");
    buf.append(byteCode == null ? "native code" : "bytecode");
    buf.append("] }");
    return buf.toString();
  }
}
