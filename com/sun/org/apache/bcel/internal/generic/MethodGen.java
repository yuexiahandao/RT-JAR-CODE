/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Attribute;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Code;
/*     */ import com.sun.org.apache.bcel.internal.classfile.CodeException;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LineNumber;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.LocalVariableTypeTable;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.classfile.Utility;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class MethodGen extends FieldGenOrMethodGen
/*     */ {
/*     */   private String class_name;
/*     */   private Type[] arg_types;
/*     */   private String[] arg_names;
/*     */   private int max_locals;
/*     */   private int max_stack;
/*     */   private InstructionList il;
/*     */   private boolean strip_attributes;
/*  89 */   private ArrayList variable_vec = new ArrayList();
/*  90 */   private ArrayList line_number_vec = new ArrayList();
/*  91 */   private ArrayList exception_vec = new ArrayList();
/*  92 */   private ArrayList throws_vec = new ArrayList();
/*  93 */   private ArrayList code_attrs_vec = new ArrayList();
/*     */   private ArrayList observers;
/*     */ 
/*     */   public MethodGen(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
/*     */   {
/* 120 */     setAccessFlags(access_flags);
/* 121 */     setType(return_type);
/* 122 */     setArgumentTypes(arg_types);
/* 123 */     setArgumentNames(arg_names);
/* 124 */     setName(method_name);
/* 125 */     setClassName(class_name);
/* 126 */     setInstructionList(il);
/* 127 */     setConstantPool(cp);
/*     */ 
/* 129 */     boolean abstract_ = (isAbstract()) || (isNative());
/* 130 */     InstructionHandle start = null;
/* 131 */     InstructionHandle end = null;
/*     */ 
/* 133 */     if (!abstract_) {
/* 134 */       start = il.getStart();
/* 135 */       end = il.getEnd();
/*     */ 
/* 139 */       if ((!isStatic()) && (class_name != null)) {
/* 140 */         addLocalVariable("this", new ObjectType(class_name), start, end);
/*     */       }
/*     */     }
/*     */ 
/* 144 */     if (arg_types != null) {
/* 145 */       int size = arg_types.length;
/*     */ 
/* 147 */       for (int i = 0; i < size; i++) {
/* 148 */         if (Type.VOID == arg_types[i]) {
/* 149 */           throw new ClassGenException("'void' is an illegal argument type for a method");
/*     */         }
/*     */       }
/*     */ 
/* 153 */       if (arg_names != null) {
/* 154 */         if (size != arg_names.length)
/* 155 */           throw new ClassGenException("Mismatch in argument array lengths: " + size + " vs. " + arg_names.length);
/*     */       }
/*     */       else {
/* 158 */         arg_names = new String[size];
/*     */ 
/* 160 */         for (int i = 0; i < size; i++) {
/* 161 */           arg_names[i] = ("arg" + i);
/*     */         }
/* 163 */         setArgumentNames(arg_names);
/*     */       }
/*     */ 
/* 166 */       if (!abstract_)
/* 167 */         for (int i = 0; i < size; i++)
/* 168 */           addLocalVariable(arg_names[i], arg_types[i], start, end);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MethodGen(Method m, String class_name, ConstantPoolGen cp)
/*     */   {
/* 182 */     this(m.getAccessFlags(), Type.getReturnType(m.getSignature()), Type.getArgumentTypes(m.getSignature()), null, m.getName(), class_name, (m.getAccessFlags() & 0x500) == 0 ? new InstructionList(m.getCode().getCode()) : null, cp);
/*     */ 
/* 189 */     Attribute[] attributes = m.getAttributes();
/* 190 */     for (int i = 0; i < attributes.length; i++) {
/* 191 */       Attribute a = attributes[i];
/*     */ 
/* 193 */       if ((a instanceof Code)) {
/* 194 */         Code c = (Code)a;
/* 195 */         setMaxStack(c.getMaxStack());
/* 196 */         setMaxLocals(c.getMaxLocals());
/*     */ 
/* 198 */         CodeException[] ces = c.getExceptionTable();
/*     */ 
/* 200 */         if (ces != null) {
/* 201 */           for (int j = 0; j < ces.length; j++) {
/* 202 */             CodeException ce = ces[j];
/* 203 */             int type = ce.getCatchType();
/* 204 */             ObjectType c_type = null;
/*     */ 
/* 206 */             if (type > 0) {
/* 207 */               String cen = m.getConstantPool().getConstantString(type, (byte)7);
/* 208 */               c_type = new ObjectType(cen);
/*     */             }
/*     */ 
/* 211 */             int end_pc = ce.getEndPC();
/* 212 */             int length = m.getCode().getCode().length;
/*     */             InstructionHandle end;
/*     */             InstructionHandle end;
/* 216 */             if (length == end_pc) {
/* 217 */               end = this.il.getEnd();
/*     */             } else {
/* 219 */               end = this.il.findHandle(end_pc);
/* 220 */               end = end.getPrev();
/*     */             }
/*     */ 
/* 223 */             addExceptionHandler(this.il.findHandle(ce.getStartPC()), end, this.il.findHandle(ce.getHandlerPC()), c_type);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 228 */         Attribute[] c_attributes = c.getAttributes();
/* 229 */         for (int j = 0; j < c_attributes.length; j++) {
/* 230 */           a = c_attributes[j];
/*     */ 
/* 232 */           if ((a instanceof LineNumberTable)) {
/* 233 */             LineNumber[] ln = ((LineNumberTable)a).getLineNumberTable();
/*     */ 
/* 235 */             for (int k = 0; k < ln.length; k++) {
/* 236 */               LineNumber l = ln[k];
/* 237 */               addLineNumber(this.il.findHandle(l.getStartPC()), l.getLineNumber());
/*     */             }
/* 239 */           } else if ((a instanceof LocalVariableTable)) {
/* 240 */             LocalVariable[] lv = ((LocalVariableTable)a).getLocalVariableTable();
/*     */ 
/* 242 */             removeLocalVariables();
/*     */ 
/* 244 */             for (int k = 0; k < lv.length; k++) {
/* 245 */               LocalVariable l = lv[k];
/* 246 */               InstructionHandle start = this.il.findHandle(l.getStartPC());
/* 247 */               InstructionHandle end = this.il.findHandle(l.getStartPC() + l.getLength());
/*     */ 
/* 250 */               if (null == start) {
/* 251 */                 start = this.il.getStart();
/*     */               }
/*     */ 
/* 254 */               if (null == end) {
/* 255 */                 end = this.il.getEnd();
/*     */               }
/*     */ 
/* 258 */               addLocalVariable(l.getName(), Type.getType(l.getSignature()), l.getIndex(), start, end);
/*     */             }
/*     */           }
/* 261 */           else if ((a instanceof LocalVariableTypeTable)) {
/* 262 */             LocalVariable[] lv = ((LocalVariableTypeTable)a).getLocalVariableTypeTable();
/* 263 */             removeLocalVariables();
/* 264 */             for (int k = 0; k < lv.length; k++) {
/* 265 */               LocalVariable l = lv[k];
/* 266 */               InstructionHandle start = this.il.findHandle(l.getStartPC());
/* 267 */               InstructionHandle end = this.il.findHandle(l.getStartPC() + l.getLength());
/*     */ 
/* 269 */               if (null == start) {
/* 270 */                 start = this.il.getStart();
/*     */               }
/* 272 */               if (null == end) {
/* 273 */                 end = this.il.getEnd();
/*     */               }
/* 275 */               addLocalVariable(l.getName(), Type.getType(l.getSignature()), l.getIndex(), start, end);
/*     */             }
/*     */           }
/*     */           else {
/* 279 */             addCodeAttribute(a);
/*     */           }
/*     */         } } else if ((a instanceof ExceptionTable)) {
/* 282 */         String[] names = ((ExceptionTable)a).getExceptionNames();
/* 283 */         for (int j = 0; j < names.length; j++)
/* 284 */           addException(names[j]);
/*     */       } else {
/* 286 */         addAttribute(a);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public LocalVariableGen addLocalVariable(String name, Type type, int slot, InstructionHandle start, InstructionHandle end)
/*     */   {
/* 305 */     byte t = type.getType();
/*     */ 
/* 307 */     if (t != 16) {
/* 308 */       int add = type.getSize();
/*     */ 
/* 310 */       if (slot + add > this.max_locals) {
/* 311 */         this.max_locals = (slot + add);
/*     */       }
/* 313 */       LocalVariableGen l = new LocalVariableGen(slot, name, type, start, end);
/*     */       int i;
/* 316 */       if ((i = this.variable_vec.indexOf(l)) >= 0)
/* 317 */         this.variable_vec.set(i, l);
/*     */       else {
/* 319 */         this.variable_vec.add(l);
/*     */       }
/* 321 */       return l;
/*     */     }
/* 323 */     throw new IllegalArgumentException("Can not use " + type + " as type for local variable");
/*     */   }
/*     */ 
/*     */   public LocalVariableGen addLocalVariable(String name, Type type, InstructionHandle start, InstructionHandle end)
/*     */   {
/* 344 */     return addLocalVariable(name, type, this.max_locals, start, end);
/*     */   }
/*     */ 
/*     */   public void removeLocalVariable(LocalVariableGen l)
/*     */   {
/* 352 */     this.variable_vec.remove(l);
/*     */   }
/*     */ 
/*     */   public void removeLocalVariables()
/*     */   {
/* 359 */     this.variable_vec.clear();
/*     */   }
/*     */ 
/*     */   private static final void sort(LocalVariableGen[] vars, int l, int r)
/*     */   {
/* 366 */     int i = l; int j = r;
/* 367 */     int m = vars[((l + r) / 2)].getIndex();
/*     */     do
/*     */     {
/* 371 */       while (vars[i].getIndex() < m) i++;
/* 372 */       while (m < vars[j].getIndex()) j--;
/*     */ 
/* 374 */       if (i <= j) {
/* 375 */         LocalVariableGen h = vars[i]; vars[i] = vars[j]; vars[j] = h;
/* 376 */         i++; j--;
/*     */       }
/*     */     }
/* 378 */     while (i <= j);
/*     */ 
/* 380 */     if (l < j) sort(vars, l, j);
/* 381 */     if (i < r) sort(vars, i, r);
/*     */   }
/*     */ 
/*     */   public LocalVariableGen[] getLocalVariables()
/*     */   {
/* 391 */     int size = this.variable_vec.size();
/* 392 */     LocalVariableGen[] lg = new LocalVariableGen[size];
/* 393 */     this.variable_vec.toArray(lg);
/*     */ 
/* 395 */     for (int i = 0; i < size; i++) {
/* 396 */       if (lg[i].getStart() == null) {
/* 397 */         lg[i].setStart(this.il.getStart());
/*     */       }
/* 399 */       if (lg[i].getEnd() == null) {
/* 400 */         lg[i].setEnd(this.il.getEnd());
/*     */       }
/*     */     }
/* 403 */     if (size > 1) {
/* 404 */       sort(lg, 0, size - 1);
/*     */     }
/* 406 */     return lg;
/*     */   }
/*     */ 
/*     */   public LocalVariableTable getLocalVariableTable(ConstantPoolGen cp)
/*     */   {
/* 413 */     LocalVariableGen[] lg = getLocalVariables();
/* 414 */     int size = lg.length;
/* 415 */     LocalVariable[] lv = new LocalVariable[size];
/*     */ 
/* 417 */     for (int i = 0; i < size; i++) {
/* 418 */       lv[i] = lg[i].getLocalVariable(cp);
/*     */     }
/* 420 */     return new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + lv.length * 10, lv, cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   public LineNumberGen addLineNumber(InstructionHandle ih, int src_line)
/*     */   {
/* 432 */     LineNumberGen l = new LineNumberGen(ih, src_line);
/* 433 */     this.line_number_vec.add(l);
/* 434 */     return l;
/*     */   }
/*     */ 
/*     */   public void removeLineNumber(LineNumberGen l)
/*     */   {
/* 441 */     this.line_number_vec.remove(l);
/*     */   }
/*     */ 
/*     */   public void removeLineNumbers()
/*     */   {
/* 448 */     this.line_number_vec.clear();
/*     */   }
/*     */ 
/*     */   public LineNumberGen[] getLineNumbers()
/*     */   {
/* 455 */     LineNumberGen[] lg = new LineNumberGen[this.line_number_vec.size()];
/* 456 */     this.line_number_vec.toArray(lg);
/* 457 */     return lg;
/*     */   }
/*     */ 
/*     */   public LineNumberTable getLineNumberTable(ConstantPoolGen cp)
/*     */   {
/* 464 */     int size = this.line_number_vec.size();
/* 465 */     LineNumber[] ln = new LineNumber[size];
/*     */     try
/*     */     {
/* 468 */       for (int i = 0; i < size; i++)
/* 469 */         ln[i] = ((LineNumberGen)this.line_number_vec.get(i)).getLineNumber();
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 472 */     return new LineNumberTable(cp.addUtf8("LineNumberTable"), 2 + ln.length * 4, ln, cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, ObjectType catch_type)
/*     */   {
/* 491 */     if ((start_pc == null) || (end_pc == null) || (handler_pc == null)) {
/* 492 */       throw new ClassGenException("Exception handler target is null instruction");
/*     */     }
/* 494 */     CodeExceptionGen c = new CodeExceptionGen(start_pc, end_pc, handler_pc, catch_type);
/*     */ 
/* 496 */     this.exception_vec.add(c);
/* 497 */     return c;
/*     */   }
/*     */ 
/*     */   public void removeExceptionHandler(CodeExceptionGen c)
/*     */   {
/* 504 */     this.exception_vec.remove(c);
/*     */   }
/*     */ 
/*     */   public void removeExceptionHandlers()
/*     */   {
/* 511 */     this.exception_vec.clear();
/*     */   }
/*     */ 
/*     */   public CodeExceptionGen[] getExceptionHandlers()
/*     */   {
/* 518 */     CodeExceptionGen[] cg = new CodeExceptionGen[this.exception_vec.size()];
/* 519 */     this.exception_vec.toArray(cg);
/* 520 */     return cg;
/*     */   }
/*     */ 
/*     */   private CodeException[] getCodeExceptions()
/*     */   {
/* 527 */     int size = this.exception_vec.size();
/* 528 */     CodeException[] c_exc = new CodeException[size];
/*     */     try
/*     */     {
/* 531 */       for (int i = 0; i < size; i++) {
/* 532 */         CodeExceptionGen c = (CodeExceptionGen)this.exception_vec.get(i);
/* 533 */         c_exc[i] = c.getCodeException(this.cp);
/*     */       }
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 537 */     return c_exc;
/*     */   }
/*     */ 
/*     */   public void addException(String class_name)
/*     */   {
/* 546 */     this.throws_vec.add(class_name);
/*     */   }
/*     */ 
/*     */   public void removeException(String c)
/*     */   {
/* 553 */     this.throws_vec.remove(c);
/*     */   }
/*     */ 
/*     */   public void removeExceptions()
/*     */   {
/* 560 */     this.throws_vec.clear();
/*     */   }
/*     */ 
/*     */   public String[] getExceptions()
/*     */   {
/* 567 */     String[] e = new String[this.throws_vec.size()];
/* 568 */     this.throws_vec.toArray(e);
/* 569 */     return e;
/*     */   }
/*     */ 
/*     */   private ExceptionTable getExceptionTable(ConstantPoolGen cp)
/*     */   {
/* 576 */     int size = this.throws_vec.size();
/* 577 */     int[] ex = new int[size];
/*     */     try
/*     */     {
/* 580 */       for (int i = 0; i < size; i++)
/* 581 */         ex[i] = cp.addClass((String)this.throws_vec.get(i));
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 584 */     return new ExceptionTable(cp.addUtf8("Exceptions"), 2 + 2 * size, ex, cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   public void addCodeAttribute(Attribute a)
/*     */   {
/* 597 */     this.code_attrs_vec.add(a);
/*     */   }
/*     */ 
/*     */   public void removeCodeAttribute(Attribute a)
/*     */   {
/* 602 */     this.code_attrs_vec.remove(a);
/*     */   }
/*     */ 
/*     */   public void removeCodeAttributes()
/*     */   {
/* 608 */     this.code_attrs_vec.clear();
/*     */   }
/*     */ 
/*     */   public Attribute[] getCodeAttributes()
/*     */   {
/* 615 */     Attribute[] attributes = new Attribute[this.code_attrs_vec.size()];
/* 616 */     this.code_attrs_vec.toArray(attributes);
/* 617 */     return attributes;
/*     */   }
/*     */ 
/*     */   public Method getMethod()
/*     */   {
/* 627 */     String signature = getSignature();
/* 628 */     int name_index = this.cp.addUtf8(this.name);
/* 629 */     int signature_index = this.cp.addUtf8(signature);
/*     */ 
/* 633 */     byte[] byte_code = null;
/*     */ 
/* 635 */     if (this.il != null) {
/* 636 */       byte_code = this.il.getByteCode();
/*     */     }
/* 638 */     LineNumberTable lnt = null;
/* 639 */     LocalVariableTable lvt = null;
/*     */ 
/* 643 */     if ((this.variable_vec.size() > 0) && (!this.strip_attributes)) {
/* 644 */       addCodeAttribute(lvt = getLocalVariableTable(this.cp));
/*     */     }
/* 646 */     if ((this.line_number_vec.size() > 0) && (!this.strip_attributes)) {
/* 647 */       addCodeAttribute(lnt = getLineNumberTable(this.cp));
/*     */     }
/* 649 */     Attribute[] code_attrs = getCodeAttributes();
/*     */ 
/* 653 */     int attrs_len = 0;
/* 654 */     for (int i = 0; i < code_attrs.length; i++) {
/* 655 */       attrs_len += code_attrs[i].getLength() + 6;
/*     */     }
/* 657 */     CodeException[] c_exc = getCodeExceptions();
/* 658 */     int exc_len = c_exc.length * 8;
/*     */ 
/* 660 */     Code code = null;
/*     */ 
/* 662 */     if ((this.il != null) && (!isAbstract()))
/*     */     {
/* 664 */       Attribute[] attributes = getAttributes();
/* 665 */       for (int i = 0; i < attributes.length; i++) {
/* 666 */         Attribute a = attributes[i];
/*     */ 
/* 668 */         if ((a instanceof Code)) {
/* 669 */           removeAttribute(a);
/*     */         }
/*     */       }
/* 672 */       code = new Code(this.cp.addUtf8("Code"), 8 + byte_code.length + 2 + exc_len + 2 + attrs_len, this.max_stack, this.max_locals, byte_code, c_exc, code_attrs, this.cp.getConstantPool());
/*     */ 
/* 681 */       addAttribute(code);
/*     */     }
/*     */ 
/* 684 */     ExceptionTable et = null;
/*     */ 
/* 686 */     if (this.throws_vec.size() > 0) {
/* 687 */       addAttribute(et = getExceptionTable(this.cp));
/*     */     }
/* 689 */     Method m = new Method(this.access_flags, name_index, signature_index, getAttributes(), this.cp.getConstantPool());
/*     */ 
/* 693 */     if (lvt != null) removeCodeAttribute(lvt);
/* 694 */     if (lnt != null) removeCodeAttribute(lnt);
/* 695 */     if (code != null) removeAttribute(code);
/* 696 */     if (et != null) removeAttribute(et);
/*     */ 
/* 698 */     return m;
/*     */   }
/*     */ 
/*     */   public void removeNOPs()
/*     */   {
/* 707 */     if (this.il != null)
/*     */     {
/*     */       InstructionHandle next;
/* 711 */       for (InstructionHandle ih = this.il.getStart(); ih != null; ih = next) {
/* 712 */         next = ih.next;
/*     */ 
/* 714 */         if ((next != null) && ((ih.getInstruction() instanceof NOP))) { InstructionHandle[] targets;
/*     */           int i;
/*     */           try { this.il.delete(ih);
/*     */           } catch (TargetLostException e) {
/* 718 */             targets = e.getTargets();
/*     */ 
/* 720 */             i = 0; } for (; i < targets.length; i++) {
/* 721 */             InstructionTargeter[] targeters = targets[i].getTargeters();
/*     */ 
/* 723 */             for (int j = 0; j < targeters.length; j++)
/* 724 */               targeters[j].updateTarget(targets[i], next);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMaxLocals(int m)
/*     */   {
/* 735 */     this.max_locals = m; } 
/* 736 */   public int getMaxLocals() { return this.max_locals; }
/*     */ 
/*     */ 
/*     */   public void setMaxStack(int m)
/*     */   {
/* 741 */     this.max_stack = m; } 
/* 742 */   public int getMaxStack() { return this.max_stack; }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 746 */     return this.class_name; } 
/* 747 */   public void setClassName(String class_name) { this.class_name = class_name; } 
/*     */   public void setReturnType(Type return_type) {
/* 749 */     setType(return_type); } 
/* 750 */   public Type getReturnType() { return getType(); } 
/*     */   public void setArgumentTypes(Type[] arg_types) {
/* 752 */     this.arg_types = arg_types; } 
/* 753 */   public Type[] getArgumentTypes() { return (Type[])this.arg_types.clone(); } 
/* 754 */   public void setArgumentType(int i, Type type) { this.arg_types[i] = type; } 
/* 755 */   public Type getArgumentType(int i) { return this.arg_types[i]; } 
/*     */   public void setArgumentNames(String[] arg_names) {
/* 757 */     this.arg_names = arg_names; } 
/* 758 */   public String[] getArgumentNames() { return (String[])this.arg_names.clone(); } 
/* 759 */   public void setArgumentName(int i, String name) { this.arg_names[i] = name; } 
/* 760 */   public String getArgumentName(int i) { return this.arg_names[i]; } 
/*     */   public InstructionList getInstructionList() {
/* 762 */     return this.il; } 
/* 763 */   public void setInstructionList(InstructionList il) { this.il = il; }
/*     */ 
/*     */   public String getSignature() {
/* 766 */     return Type.getMethodSignature(this.type, this.arg_types);
/*     */   }
/*     */ 
/*     */   public void setMaxStack()
/*     */   {
/* 773 */     if (this.il != null)
/* 774 */       this.max_stack = getMaxStack(this.cp, this.il, getExceptionHandlers());
/*     */     else
/* 776 */       this.max_stack = 0;
/*     */   }
/*     */ 
/*     */   public void setMaxLocals()
/*     */   {
/* 783 */     if (this.il != null) {
/* 784 */       int max = isStatic() ? 0 : 1;
/*     */ 
/* 786 */       if (this.arg_types != null) {
/* 787 */         for (int i = 0; i < this.arg_types.length; i++)
/* 788 */           max += this.arg_types[i].getSize();
/*     */       }
/* 790 */       for (InstructionHandle ih = this.il.getStart(); ih != null; ih = ih.getNext()) {
/* 791 */         Instruction ins = ih.getInstruction();
/*     */ 
/* 793 */         if (((ins instanceof LocalVariableInstruction)) || ((ins instanceof RET)) || ((ins instanceof IINC)))
/*     */         {
/* 796 */           int index = ((IndexedInstruction)ins).getIndex() + ((TypedInstruction)ins).getType(this.cp).getSize();
/*     */ 
/* 799 */           if (index > max) {
/* 800 */             max = index;
/*     */           }
/*     */         }
/*     */       }
/* 804 */       this.max_locals = max;
/*     */     } else {
/* 806 */       this.max_locals = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stripAttributes(boolean flag)
/*     */   {
/* 812 */     this.strip_attributes = flag;
/*     */   }
/*     */ 
/*     */   public static int getMaxStack(ConstantPoolGen cp, InstructionList il, CodeExceptionGen[] et)
/*     */   {
/* 862 */     BranchStack branchTargets = new BranchStack();
/*     */ 
/* 869 */     for (int i = 0; i < et.length; i++) {
/* 870 */       InstructionHandle handler_pc = et[i].getHandlerPC();
/* 871 */       if (handler_pc != null) {
/* 872 */         branchTargets.push(handler_pc, 1);
/*     */       }
/*     */     }
/* 875 */     int stackDepth = 0; int maxStackDepth = 0;
/* 876 */     InstructionHandle ih = il.getStart();
/*     */ 
/* 878 */     while (ih != null) {
/* 879 */       Instruction instruction = ih.getInstruction();
/* 880 */       short opcode = instruction.getOpcode();
/* 881 */       int delta = instruction.produceStack(cp) - instruction.consumeStack(cp);
/*     */ 
/* 883 */       stackDepth += delta;
/* 884 */       if (stackDepth > maxStackDepth) {
/* 885 */         maxStackDepth = stackDepth;
/*     */       }
/*     */ 
/* 888 */       if ((instruction instanceof BranchInstruction)) {
/* 889 */         BranchInstruction branch = (BranchInstruction)instruction;
/* 890 */         if ((instruction instanceof Select))
/*     */         {
/* 892 */           Select select = (Select)branch;
/* 893 */           InstructionHandle[] targets = select.getTargets();
/* 894 */           for (int i = 0; i < targets.length; i++) {
/* 895 */             branchTargets.push(targets[i], stackDepth);
/*     */           }
/* 897 */           ih = null;
/* 898 */         } else if (!(branch instanceof IfInstruction))
/*     */         {
/* 901 */           if ((opcode == 168) || (opcode == 201))
/* 902 */             branchTargets.push(ih.getNext(), stackDepth - 1);
/* 903 */           ih = null;
/*     */         }
/*     */ 
/* 908 */         branchTargets.push(branch.getTarget(), stackDepth);
/*     */       }
/* 911 */       else if ((opcode == 191) || (opcode == 169) || ((opcode >= 172) && (opcode <= 177)))
/*     */       {
/* 913 */         ih = null;
/*     */       }
/*     */ 
/* 916 */       if (ih != null) {
/* 917 */         ih = ih.getNext();
/*     */       }
/* 919 */       if (ih == null) {
/* 920 */         BranchTarget bt = branchTargets.pop();
/* 921 */         if (bt != null) {
/* 922 */           ih = bt.target;
/* 923 */           stackDepth = bt.stackDepth;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 928 */     return maxStackDepth;
/*     */   }
/*     */ 
/*     */   public void addObserver(MethodObserver o)
/*     */   {
/* 936 */     if (this.observers == null) {
/* 937 */       this.observers = new ArrayList();
/*     */     }
/* 939 */     this.observers.add(o);
/*     */   }
/*     */ 
/*     */   public void removeObserver(MethodObserver o)
/*     */   {
/* 945 */     if (this.observers != null)
/* 946 */       this.observers.remove(o);
/*     */   }
/*     */ 
/*     */   public void update()
/*     */   {
/*     */     Iterator e;
/* 954 */     if (this.observers != null)
/* 955 */       for (e = this.observers.iterator(); e.hasNext(); )
/* 956 */         ((MethodObserver)e.next()).notify(this);
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 966 */     String access = Utility.accessToString(this.access_flags);
/* 967 */     String signature = Type.getMethodSignature(this.type, this.arg_types);
/*     */ 
/* 969 */     signature = Utility.methodSignatureToString(signature, this.name, access, true, getLocalVariableTable(this.cp));
/*     */ 
/* 972 */     StringBuffer buf = new StringBuffer(signature);
/*     */     Iterator e;
/* 974 */     if (this.throws_vec.size() > 0) {
/* 975 */       for (e = this.throws_vec.iterator(); e.hasNext(); ) {
/* 976 */         buf.append("\n\t\tthrows " + e.next());
/*     */       }
/*     */     }
/* 979 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public MethodGen copy(String class_name, ConstantPoolGen cp)
/*     */   {
/* 985 */     Method m = ((MethodGen)clone()).getMethod();
/* 986 */     MethodGen mg = new MethodGen(m, class_name, this.cp);
/*     */ 
/* 988 */     if (this.cp != cp) {
/* 989 */       mg.setConstantPool(cp);
/* 990 */       mg.getInstructionList().replaceConstantPool(this.cp, cp);
/*     */     }
/*     */ 
/* 993 */     return mg;
/*     */   }
/*     */ 
/*     */   static final class BranchStack
/*     */   {
/* 825 */     Stack branchTargets = new Stack();
/* 826 */     Hashtable visitedTargets = new Hashtable();
/*     */ 
/*     */     public void push(InstructionHandle target, int stackDepth) {
/* 829 */       if (visited(target)) {
/* 830 */         return;
/*     */       }
/* 832 */       this.branchTargets.push(visit(target, stackDepth));
/*     */     }
/*     */ 
/*     */     public MethodGen.BranchTarget pop() {
/* 836 */       if (!this.branchTargets.empty()) {
/* 837 */         MethodGen.BranchTarget bt = (MethodGen.BranchTarget)this.branchTargets.pop();
/* 838 */         return bt;
/*     */       }
/*     */ 
/* 841 */       return null;
/*     */     }
/*     */ 
/*     */     private final MethodGen.BranchTarget visit(InstructionHandle target, int stackDepth) {
/* 845 */       MethodGen.BranchTarget bt = new MethodGen.BranchTarget(target, stackDepth);
/* 846 */       this.visitedTargets.put(target, bt);
/*     */ 
/* 848 */       return bt;
/*     */     }
/*     */ 
/*     */     private final boolean visited(InstructionHandle target) {
/* 852 */       return this.visitedTargets.get(target) != null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class BranchTarget
/*     */   {
/*     */     InstructionHandle target;
/*     */     int stackDepth;
/*     */ 
/*     */     BranchTarget(InstructionHandle target, int stackDepth)
/*     */     {
/* 819 */       this.target = target;
/* 820 */       this.stackDepth = stackDepth;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.MethodGen
 * JD-Core Version:    0.6.2
 */