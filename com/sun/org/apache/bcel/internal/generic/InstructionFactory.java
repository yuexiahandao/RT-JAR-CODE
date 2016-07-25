/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class InstructionFactory
/*     */   implements InstructionConstants, Serializable
/*     */ {
/*     */   protected ClassGen cg;
/*     */   protected ConstantPoolGen cp;
/* 187 */   private static MethodObject[] append_mos = { new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.STRING }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.OBJECT }, 1), null, null, new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.BOOLEAN }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.CHAR }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.FLOAT }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.DOUBLE }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.INT }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.INT }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.INT }, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[] { Type.LONG }, 1) };
/*     */ 
/*     */   public InstructionFactory(ClassGen cg, ConstantPoolGen cp)
/*     */   {
/*  78 */     this.cg = cg;
/*  79 */     this.cp = cp;
/*     */   }
/*     */ 
/*     */   public InstructionFactory(ClassGen cg)
/*     */   {
/*  85 */     this(cg, cg.getConstantPool());
/*     */   }
/*     */ 
/*     */   public InstructionFactory(ConstantPoolGen cp)
/*     */   {
/*  91 */     this(null, cp);
/*     */   }
/*     */ 
/*     */   public InvokeInstruction createInvoke(String class_name, String name, Type ret_type, Type[] arg_types, short kind)
/*     */   {
/* 107 */     int nargs = 0;
/* 108 */     String signature = Type.getMethodSignature(ret_type, arg_types);
/*     */ 
/* 110 */     for (int i = 0; i < arg_types.length; i++)
/* 111 */       nargs += arg_types[i].getSize();
/*     */     int index;
/*     */     int index;
/* 113 */     if (kind == 185)
/* 114 */       index = this.cp.addInterfaceMethodref(class_name, name, signature);
/*     */     else {
/* 116 */       index = this.cp.addMethodref(class_name, name, signature);
/*     */     }
/* 118 */     switch (kind) { case 183:
/* 119 */       return new INVOKESPECIAL(index);
/*     */     case 182:
/* 120 */       return new INVOKEVIRTUAL(index);
/*     */     case 184:
/* 121 */       return new INVOKESTATIC(index);
/*     */     case 185:
/* 122 */       return new INVOKEINTERFACE(index, nargs + 1);
/*     */     }
/* 124 */     throw new RuntimeException("Oops: Unknown invoke kind:" + kind);
/*     */   }
/*     */ 
/*     */   public InstructionList createPrintln(String s)
/*     */   {
/* 133 */     InstructionList il = new InstructionList();
/* 134 */     int out = this.cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
/*     */ 
/* 136 */     int println = this.cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
/*     */ 
/* 139 */     il.append(new GETSTATIC(out));
/* 140 */     il.append(new PUSH(this.cp, s));
/* 141 */     il.append(new INVOKEVIRTUAL(println));
/*     */ 
/* 143 */     return il;
/*     */   }
/*     */ 
/*     */   public Instruction createConstant(Object value)
/*     */   {
/*     */     PUSH push;
/* 152 */     if ((value instanceof Number)) {
/* 153 */       push = new PUSH(this.cp, (Number)value);
/*     */     }
/*     */     else
/*     */     {
/*     */       PUSH push;
/* 154 */       if ((value instanceof String)) {
/* 155 */         push = new PUSH(this.cp, (String)value);
/*     */       }
/*     */       else
/*     */       {
/*     */         PUSH push;
/* 156 */         if ((value instanceof Boolean)) {
/* 157 */           push = new PUSH(this.cp, (Boolean)value);
/*     */         }
/*     */         else
/*     */         {
/*     */           PUSH push;
/* 158 */           if ((value instanceof Character))
/* 159 */             push = new PUSH(this.cp, (Character)value);
/*     */           else
/* 161 */             throw new ClassGenException("Illegal type: " + value.getClass());
/*     */         }
/*     */       }
/*     */     }
/*     */     PUSH push;
/* 163 */     return push.getInstruction();
/*     */   }
/*     */ 
/*     */   private InvokeInstruction createInvoke(MethodObject m, short kind)
/*     */   {
/* 184 */     return createInvoke(m.class_name, m.name, m.result_type, m.arg_types, kind);
/*     */   }
/*     */ 
/*     */   private static final boolean isString(Type type)
/*     */   {
/* 212 */     return ((type instanceof ObjectType)) && (((ObjectType)type).getClassName().equals("java.lang.String"));
/*     */   }
/*     */ 
/*     */   public Instruction createAppend(Type type)
/*     */   {
/* 217 */     byte t = type.getType();
/*     */ 
/* 219 */     if (isString(type)) {
/* 220 */       return createInvoke(append_mos[0], (short)182);
/*     */     }
/* 222 */     switch (t) {
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 231 */       return createInvoke(append_mos[t], (short)182);
/*     */     case 13:
/*     */     case 14:
/* 234 */       return createInvoke(append_mos[1], (short)182);
/*     */     case 12:
/* 236 */     }throw new RuntimeException("Oops: No append for this type? " + type);
/*     */   }
/*     */ 
/*     */   public FieldInstruction createFieldAccess(String class_name, String name, Type type, short kind)
/*     */   {
/* 250 */     String signature = type.getSignature();
/*     */ 
/* 252 */     int index = this.cp.addFieldref(class_name, name, signature);
/*     */ 
/* 254 */     switch (kind) { case 180:
/* 255 */       return new GETFIELD(index);
/*     */     case 181:
/* 256 */       return new PUTFIELD(index);
/*     */     case 178:
/* 257 */       return new GETSTATIC(index);
/*     */     case 179:
/* 258 */       return new PUTSTATIC(index);
/*     */     }
/*     */ 
/* 261 */     throw new RuntimeException("Oops: Unknown getfield kind:" + kind);
/*     */   }
/*     */ 
/*     */   public static Instruction createThis()
/*     */   {
/* 268 */     return new ALOAD(0);
/*     */   }
/*     */ 
/*     */   public static ReturnInstruction createReturn(Type type)
/*     */   {
/* 274 */     switch (type.getType()) { case 13:
/*     */     case 14:
/* 276 */       return ARETURN;
/*     */     case 4:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 281 */       return IRETURN;
/*     */     case 6:
/* 282 */       return FRETURN;
/*     */     case 7:
/* 283 */       return DRETURN;
/*     */     case 11:
/* 284 */       return LRETURN;
/*     */     case 12:
/* 285 */       return RETURN;
/*     */     }
/*     */ 
/* 288 */     throw new RuntimeException("Invalid type: " + type);
/*     */   }
/*     */ 
/*     */   private static final ArithmeticInstruction createBinaryIntOp(char first, String op)
/*     */   {
/* 293 */     switch (first) { case '-':
/* 294 */       return ISUB;
/*     */     case '+':
/* 295 */       return IADD;
/*     */     case '%':
/* 296 */       return IREM;
/*     */     case '*':
/* 297 */       return IMUL;
/*     */     case '/':
/* 298 */       return IDIV;
/*     */     case '&':
/* 299 */       return IAND;
/*     */     case '|':
/* 300 */       return IOR;
/*     */     case '^':
/* 301 */       return IXOR;
/*     */     case '<':
/* 302 */       return ISHL;
/*     */     case '>':
/* 303 */       return op.equals(">>>") ? IUSHR : ISHR;
/*     */     }
/* 305 */     throw new RuntimeException("Invalid operand " + op);
/*     */   }
/*     */ 
/*     */   private static final ArithmeticInstruction createBinaryLongOp(char first, String op)
/*     */   {
/* 310 */     switch (first) { case '-':
/* 311 */       return LSUB;
/*     */     case '+':
/* 312 */       return LADD;
/*     */     case '%':
/* 313 */       return LREM;
/*     */     case '*':
/* 314 */       return LMUL;
/*     */     case '/':
/* 315 */       return LDIV;
/*     */     case '&':
/* 316 */       return LAND;
/*     */     case '|':
/* 317 */       return LOR;
/*     */     case '^':
/* 318 */       return LXOR;
/*     */     case '<':
/* 319 */       return LSHL;
/*     */     case '>':
/* 320 */       return op.equals(">>>") ? LUSHR : LSHR;
/*     */     }
/* 322 */     throw new RuntimeException("Invalid operand " + op);
/*     */   }
/*     */ 
/*     */   private static final ArithmeticInstruction createBinaryFloatOp(char op)
/*     */   {
/* 327 */     switch (op) { case '-':
/* 328 */       return FSUB;
/*     */     case '+':
/* 329 */       return FADD;
/*     */     case '*':
/* 330 */       return FMUL;
/*     */     case '/':
/* 331 */       return FDIV;
/*     */     case ',':
/* 332 */     case '.': } throw new RuntimeException("Invalid operand " + op);
/*     */   }
/*     */ 
/*     */   private static final ArithmeticInstruction createBinaryDoubleOp(char op)
/*     */   {
/* 337 */     switch (op) { case '-':
/* 338 */       return DSUB;
/*     */     case '+':
/* 339 */       return DADD;
/*     */     case '*':
/* 340 */       return DMUL;
/*     */     case '/':
/* 341 */       return DDIV;
/*     */     case ',':
/* 342 */     case '.': } throw new RuntimeException("Invalid operand " + op);
/*     */   }
/*     */ 
/*     */   public static ArithmeticInstruction createBinaryOperation(String op, Type type)
/*     */   {
/* 352 */     char first = op.toCharArray()[0];
/*     */ 
/* 354 */     switch (type.getType()) { case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 358 */       return createBinaryIntOp(first, op);
/*     */     case 11:
/* 359 */       return createBinaryLongOp(first, op);
/*     */     case 6:
/* 360 */       return createBinaryFloatOp(first);
/*     */     case 7:
/* 361 */       return createBinaryDoubleOp(first); }
/* 362 */     throw new RuntimeException("Invalid type " + type);
/*     */   }
/*     */ 
/*     */   public static StackInstruction createPop(int size)
/*     */   {
/* 370 */     return size == 2 ? POP2 : POP;
/*     */   }
/*     */ 
/*     */   public static StackInstruction createDup(int size)
/*     */   {
/* 378 */     return size == 2 ? DUP2 : DUP;
/*     */   }
/*     */ 
/*     */   public static StackInstruction createDup_2(int size)
/*     */   {
/* 386 */     return size == 2 ? DUP2_X2 : DUP_X2;
/*     */   }
/*     */ 
/*     */   public static StackInstruction createDup_1(int size)
/*     */   {
/* 394 */     return size == 2 ? DUP2_X1 : DUP_X1;
/*     */   }
/*     */ 
/*     */   public static LocalVariableInstruction createStore(Type type, int index)
/*     */   {
/* 402 */     switch (type.getType()) { case 4:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 407 */       return new ISTORE(index);
/*     */     case 6:
/* 408 */       return new FSTORE(index);
/*     */     case 7:
/* 409 */       return new DSTORE(index);
/*     */     case 11:
/* 410 */       return new LSTORE(index);
/*     */     case 13:
/*     */     case 14:
/* 412 */       return new ASTORE(index);
/* 413 */     case 12: } throw new RuntimeException("Invalid type " + type);
/*     */   }
/*     */ 
/*     */   public static LocalVariableInstruction createLoad(Type type, int index)
/*     */   {
/* 421 */     switch (type.getType()) { case 4:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 426 */       return new ILOAD(index);
/*     */     case 6:
/* 427 */       return new FLOAD(index);
/*     */     case 7:
/* 428 */       return new DLOAD(index);
/*     */     case 11:
/* 429 */       return new LLOAD(index);
/*     */     case 13:
/*     */     case 14:
/* 431 */       return new ALOAD(index);
/* 432 */     case 12: } throw new RuntimeException("Invalid type " + type);
/*     */   }
/*     */ 
/*     */   public static ArrayInstruction createArrayLoad(Type type)
/*     */   {
/* 440 */     switch (type.getType()) { case 4:
/*     */     case 8:
/* 442 */       return BALOAD;
/*     */     case 5:
/* 443 */       return CALOAD;
/*     */     case 9:
/* 444 */       return SALOAD;
/*     */     case 10:
/* 445 */       return IALOAD;
/*     */     case 6:
/* 446 */       return FALOAD;
/*     */     case 7:
/* 447 */       return DALOAD;
/*     */     case 11:
/* 448 */       return LALOAD;
/*     */     case 13:
/*     */     case 14:
/* 450 */       return AALOAD;
/* 451 */     case 12: } throw new RuntimeException("Invalid type " + type);
/*     */   }
/*     */ 
/*     */   public static ArrayInstruction createArrayStore(Type type)
/*     */   {
/* 459 */     switch (type.getType()) { case 4:
/*     */     case 8:
/* 461 */       return BASTORE;
/*     */     case 5:
/* 462 */       return CASTORE;
/*     */     case 9:
/* 463 */       return SASTORE;
/*     */     case 10:
/* 464 */       return IASTORE;
/*     */     case 6:
/* 465 */       return FASTORE;
/*     */     case 7:
/* 466 */       return DASTORE;
/*     */     case 11:
/* 467 */       return LASTORE;
/*     */     case 13:
/*     */     case 14:
/* 469 */       return AASTORE;
/* 470 */     case 12: } throw new RuntimeException("Invalid type " + type);
/*     */   }
/*     */ 
/*     */   public Instruction createCast(Type src_type, Type dest_type)
/*     */   {
/* 479 */     if (((src_type instanceof BasicType)) && ((dest_type instanceof BasicType))) {
/* 480 */       byte dest = dest_type.getType();
/* 481 */       byte src = src_type.getType();
/*     */ 
/* 483 */       if ((dest == 11) && ((src == 5) || (src == 8) || (src == 9)))
/*     */       {
/* 485 */         src = 10;
/*     */       }
/* 487 */       String[] short_names = { "C", "F", "D", "B", "S", "I", "L" };
/*     */ 
/* 489 */       String name = "com.sun.org.apache.bcel.internal.generic." + short_names[(src - 5)] + "2" + short_names[(dest - 5)];
/*     */ 
/* 492 */       Instruction i = null;
/*     */       try {
/* 494 */         i = (Instruction)Class.forName(name).newInstance();
/*     */       } catch (Exception e) {
/* 496 */         throw new RuntimeException("Could not find instruction: " + name);
/*     */       }
/*     */ 
/* 499 */       return i;
/* 500 */     }if (((src_type instanceof ReferenceType)) && ((dest_type instanceof ReferenceType))) {
/* 501 */       if ((dest_type instanceof ArrayType)) {
/* 502 */         return new CHECKCAST(this.cp.addArrayClass((ArrayType)dest_type));
/*     */       }
/* 504 */       return new CHECKCAST(this.cp.addClass(((ObjectType)dest_type).getClassName()));
/*     */     }
/*     */ 
/* 507 */     throw new RuntimeException("Can not cast " + src_type + " to " + dest_type);
/*     */   }
/*     */ 
/*     */   public GETFIELD createGetField(String class_name, String name, Type t) {
/* 511 */     return new GETFIELD(this.cp.addFieldref(class_name, name, t.getSignature()));
/*     */   }
/*     */ 
/*     */   public GETSTATIC createGetStatic(String class_name, String name, Type t) {
/* 515 */     return new GETSTATIC(this.cp.addFieldref(class_name, name, t.getSignature()));
/*     */   }
/*     */ 
/*     */   public PUTFIELD createPutField(String class_name, String name, Type t) {
/* 519 */     return new PUTFIELD(this.cp.addFieldref(class_name, name, t.getSignature()));
/*     */   }
/*     */ 
/*     */   public PUTSTATIC createPutStatic(String class_name, String name, Type t) {
/* 523 */     return new PUTSTATIC(this.cp.addFieldref(class_name, name, t.getSignature()));
/*     */   }
/*     */ 
/*     */   public CHECKCAST createCheckCast(ReferenceType t) {
/* 527 */     if ((t instanceof ArrayType)) {
/* 528 */       return new CHECKCAST(this.cp.addArrayClass((ArrayType)t));
/*     */     }
/* 530 */     return new CHECKCAST(this.cp.addClass((ObjectType)t));
/*     */   }
/*     */ 
/*     */   public INSTANCEOF createInstanceOf(ReferenceType t) {
/* 534 */     if ((t instanceof ArrayType)) {
/* 535 */       return new INSTANCEOF(this.cp.addArrayClass((ArrayType)t));
/*     */     }
/* 537 */     return new INSTANCEOF(this.cp.addClass((ObjectType)t));
/*     */   }
/*     */ 
/*     */   public NEW createNew(ObjectType t) {
/* 541 */     return new NEW(this.cp.addClass(t));
/*     */   }
/*     */ 
/*     */   public NEW createNew(String s) {
/* 545 */     return createNew(new ObjectType(s));
/*     */   }
/*     */ 
/*     */   public Instruction createNewArray(Type t, short dim)
/*     */   {
/* 552 */     if (dim == 1) {
/* 553 */       if ((t instanceof ObjectType))
/* 554 */         return new ANEWARRAY(this.cp.addClass((ObjectType)t));
/* 555 */       if ((t instanceof ArrayType)) {
/* 556 */         return new ANEWARRAY(this.cp.addArrayClass((ArrayType)t));
/*     */       }
/* 558 */       return new NEWARRAY(((BasicType)t).getType());
/*     */     }
/*     */     ArrayType at;
/*     */     ArrayType at;
/* 562 */     if ((t instanceof ArrayType))
/* 563 */       at = (ArrayType)t;
/*     */     else {
/* 565 */       at = new ArrayType(t, dim);
/*     */     }
/* 567 */     return new MULTIANEWARRAY(this.cp.addArrayClass(at), dim);
/*     */   }
/*     */ 
/*     */   public static Instruction createNull(Type type)
/*     */   {
/* 574 */     switch (type.getType()) { case 13:
/*     */     case 14:
/* 576 */       return ACONST_NULL;
/*     */     case 4:
/*     */     case 5:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/* 581 */       return ICONST_0;
/*     */     case 6:
/* 582 */       return FCONST_0;
/*     */     case 7:
/* 583 */       return DCONST_0;
/*     */     case 11:
/* 584 */       return LCONST_0;
/*     */     case 12:
/* 585 */       return NOP;
/*     */     }
/*     */ 
/* 588 */     throw new RuntimeException("Invalid type: " + type);
/*     */   }
/*     */ 
/*     */   public static BranchInstruction createBranchInstruction(short opcode, InstructionHandle target)
/*     */   {
/* 596 */     switch (opcode) { case 153:
/* 597 */       return new IFEQ(target);
/*     */     case 154:
/* 598 */       return new IFNE(target);
/*     */     case 155:
/* 599 */       return new IFLT(target);
/*     */     case 156:
/* 600 */       return new IFGE(target);
/*     */     case 157:
/* 601 */       return new IFGT(target);
/*     */     case 158:
/* 602 */       return new IFLE(target);
/*     */     case 159:
/* 603 */       return new IF_ICMPEQ(target);
/*     */     case 160:
/* 604 */       return new IF_ICMPNE(target);
/*     */     case 161:
/* 605 */       return new IF_ICMPLT(target);
/*     */     case 162:
/* 606 */       return new IF_ICMPGE(target);
/*     */     case 163:
/* 607 */       return new IF_ICMPGT(target);
/*     */     case 164:
/* 608 */       return new IF_ICMPLE(target);
/*     */     case 165:
/* 609 */       return new IF_ACMPEQ(target);
/*     */     case 166:
/* 610 */       return new IF_ACMPNE(target);
/*     */     case 167:
/* 611 */       return new GOTO(target);
/*     */     case 168:
/* 612 */       return new JSR(target);
/*     */     case 198:
/* 613 */       return new IFNULL(target);
/*     */     case 199:
/* 614 */       return new IFNONNULL(target);
/*     */     case 200:
/* 615 */       return new GOTO_W(target);
/*     */     case 201:
/* 616 */       return new JSR_W(target);
/*     */     case 169:
/*     */     case 170:
/*     */     case 171:
/*     */     case 172:
/*     */     case 173:
/*     */     case 174:
/*     */     case 175:
/*     */     case 176:
/*     */     case 177:
/*     */     case 178:
/*     */     case 179:
/*     */     case 180:
/*     */     case 181:
/*     */     case 182:
/*     */     case 183:
/*     */     case 184:
/*     */     case 185:
/*     */     case 186:
/*     */     case 187:
/*     */     case 188:
/*     */     case 189:
/*     */     case 190:
/*     */     case 191:
/*     */     case 192:
/*     */     case 193:
/*     */     case 194:
/*     */     case 195:
/*     */     case 196:
/* 618 */     case 197: } throw new RuntimeException("Invalid opcode: " + opcode);
/*     */   }
/*     */ 
/*     */   public void setClassGen(ClassGen c) {
/* 622 */     this.cg = c; } 
/* 623 */   public ClassGen getClassGen() { return this.cg; } 
/* 624 */   public void setConstantPool(ConstantPoolGen c) { this.cp = c; } 
/* 625 */   public ConstantPoolGen getConstantPool() { return this.cp; }
/*     */ 
/*     */ 
/*     */   private static class MethodObject
/*     */   {
/*     */     Type[] arg_types;
/*     */     Type result_type;
/*     */     String[] arg_names;
/*     */     String class_name;
/*     */     String name;
/*     */     int access;
/*     */ 
/*     */     MethodObject(String c, String n, Type r, Type[] a, int acc)
/*     */     {
/* 175 */       this.class_name = c;
/* 176 */       this.name = n;
/* 177 */       this.result_type = r;
/* 178 */       this.arg_types = a;
/* 179 */       this.access = acc;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.InstructionFactory
 * JD-Core Version:    0.6.2
 */