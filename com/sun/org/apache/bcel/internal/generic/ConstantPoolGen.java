/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Constant;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantCP;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantDouble;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantFloat;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantLong;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantString;
/*     */ import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class ConstantPoolGen
/*     */   implements Serializable
/*     */ {
/*  79 */   protected int size = 1024;
/*  80 */   protected Constant[] constants = new Constant[this.size];
/*  81 */   protected int index = 1;
/*     */   private static final String METHODREF_DELIM = ":";
/*     */   private static final String IMETHODREF_DELIM = "#";
/*     */   private static final String FIELDREF_DELIM = "&";
/*     */   private static final String NAT_DELIM = "%";
/* 182 */   private HashMap string_table = new HashMap();
/*     */ 
/* 221 */   private HashMap class_table = new HashMap();
/*     */ 
/* 363 */   private HashMap utf8_table = new HashMap();
/*     */ 
/* 481 */   private HashMap n_a_t_table = new HashMap();
/*     */ 
/* 520 */   private HashMap cp_table = new HashMap();
/*     */ 
/*     */   public ConstantPoolGen(Constant[] cs)
/*     */   {
/*  99 */     if (cs.length > this.size) {
/* 100 */       this.size = cs.length;
/* 101 */       this.constants = new Constant[this.size];
/*     */     }
/*     */ 
/* 104 */     System.arraycopy(cs, 0, this.constants, 0, cs.length);
/*     */ 
/* 106 */     if (cs.length > 0) {
/* 107 */       this.index = cs.length;
/*     */     }
/* 109 */     for (int i = 1; i < this.index; i++) {
/* 110 */       Constant c = this.constants[i];
/*     */ 
/* 112 */       if ((c instanceof ConstantString)) {
/* 113 */         ConstantString s = (ConstantString)c;
/* 114 */         ConstantUtf8 u8 = (ConstantUtf8)this.constants[s.getStringIndex()];
/*     */ 
/* 116 */         this.string_table.put(u8.getBytes(), new Index(i));
/* 117 */       } else if ((c instanceof ConstantClass)) {
/* 118 */         ConstantClass s = (ConstantClass)c;
/* 119 */         ConstantUtf8 u8 = (ConstantUtf8)this.constants[s.getNameIndex()];
/*     */ 
/* 121 */         this.class_table.put(u8.getBytes(), new Index(i));
/* 122 */       } else if ((c instanceof ConstantNameAndType)) {
/* 123 */         ConstantNameAndType n = (ConstantNameAndType)c;
/* 124 */         ConstantUtf8 u8 = (ConstantUtf8)this.constants[n.getNameIndex()];
/* 125 */         ConstantUtf8 u8_2 = (ConstantUtf8)this.constants[n.getSignatureIndex()];
/*     */ 
/* 127 */         this.n_a_t_table.put(u8.getBytes() + "%" + u8_2.getBytes(), new Index(i));
/* 128 */       } else if ((c instanceof ConstantUtf8)) {
/* 129 */         ConstantUtf8 u = (ConstantUtf8)c;
/*     */ 
/* 131 */         this.utf8_table.put(u.getBytes(), new Index(i));
/* 132 */       } else if ((c instanceof ConstantCP)) {
/* 133 */         ConstantCP m = (ConstantCP)c;
/* 134 */         ConstantClass clazz = (ConstantClass)this.constants[m.getClassIndex()];
/* 135 */         ConstantNameAndType n = (ConstantNameAndType)this.constants[m.getNameAndTypeIndex()];
/*     */ 
/* 137 */         ConstantUtf8 u8 = (ConstantUtf8)this.constants[clazz.getNameIndex()];
/* 138 */         String class_name = u8.getBytes().replace('/', '.');
/*     */ 
/* 140 */         u8 = (ConstantUtf8)this.constants[n.getNameIndex()];
/* 141 */         String method_name = u8.getBytes();
/*     */ 
/* 143 */         u8 = (ConstantUtf8)this.constants[n.getSignatureIndex()];
/* 144 */         String signature = u8.getBytes();
/*     */ 
/* 146 */         String delim = ":";
/*     */ 
/* 148 */         if ((c instanceof ConstantInterfaceMethodref))
/* 149 */           delim = "#";
/* 150 */         else if ((c instanceof ConstantFieldref)) {
/* 151 */           delim = "&";
/*     */         }
/* 153 */         this.cp_table.put(class_name + delim + method_name + delim + signature, new Index(i));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public ConstantPoolGen(ConstantPool cp)
/*     */   {
/* 162 */     this(cp.getConstantPool());
/*     */   }
/*     */ 
/*     */   public ConstantPoolGen()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void adjustSize()
/*     */   {
/* 173 */     if (this.index + 3 >= this.size) {
/* 174 */       Constant[] cs = this.constants;
/*     */ 
/* 176 */       this.size *= 2;
/* 177 */       this.constants = new Constant[this.size];
/* 178 */       System.arraycopy(cs, 0, this.constants, 0, this.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int lookupString(String str)
/*     */   {
/* 191 */     Index index = (Index)this.string_table.get(str);
/* 192 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int addString(String str)
/*     */   {
/* 204 */     if ((ret = lookupString(str)) != -1) {
/* 205 */       return ret;
/*     */     }
/* 207 */     int utf8 = addUtf8(str);
/*     */ 
/* 209 */     adjustSize();
/*     */ 
/* 211 */     ConstantString s = new ConstantString(utf8);
/*     */ 
/* 213 */     int ret = this.index;
/* 214 */     this.constants[(this.index++)] = s;
/*     */ 
/* 216 */     this.string_table.put(str, new Index(ret));
/*     */ 
/* 218 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupClass(String str)
/*     */   {
/* 230 */     Index index = (Index)this.class_table.get(str.replace('.', '/'));
/* 231 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   private int addClass_(String clazz)
/*     */   {
/* 237 */     if ((ret = lookupClass(clazz)) != -1) {
/* 238 */       return ret;
/*     */     }
/* 240 */     adjustSize();
/*     */ 
/* 242 */     ConstantClass c = new ConstantClass(addUtf8(clazz));
/*     */ 
/* 244 */     int ret = this.index;
/* 245 */     this.constants[(this.index++)] = c;
/*     */ 
/* 247 */     this.class_table.put(clazz, new Index(ret));
/*     */ 
/* 249 */     return ret;
/*     */   }
/*     */ 
/*     */   public int addClass(String str)
/*     */   {
/* 259 */     return addClass_(str.replace('.', '/'));
/*     */   }
/*     */ 
/*     */   public int addClass(ObjectType type)
/*     */   {
/* 269 */     return addClass(type.getClassName());
/*     */   }
/*     */ 
/*     */   public int addArrayClass(ArrayType type)
/*     */   {
/* 280 */     return addClass_(type.getSignature());
/*     */   }
/*     */ 
/*     */   public int lookupInteger(int n)
/*     */   {
/* 290 */     for (int i = 1; i < this.index; i++) {
/* 291 */       if ((this.constants[i] instanceof ConstantInteger)) {
/* 292 */         ConstantInteger c = (ConstantInteger)this.constants[i];
/*     */ 
/* 294 */         if (c.getBytes() == n) {
/* 295 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 299 */     return -1;
/*     */   }
/*     */ 
/*     */   public int addInteger(int n)
/*     */   {
/* 311 */     if ((ret = lookupInteger(n)) != -1) {
/* 312 */       return ret;
/*     */     }
/* 314 */     adjustSize();
/*     */ 
/* 316 */     int ret = this.index;
/* 317 */     this.constants[(this.index++)] = new ConstantInteger(n);
/*     */ 
/* 319 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupFloat(float n)
/*     */   {
/* 329 */     int bits = Float.floatToIntBits(n);
/*     */ 
/* 331 */     for (int i = 1; i < this.index; i++) {
/* 332 */       if ((this.constants[i] instanceof ConstantFloat)) {
/* 333 */         ConstantFloat c = (ConstantFloat)this.constants[i];
/*     */ 
/* 335 */         if (Float.floatToIntBits(c.getBytes()) == bits) {
/* 336 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 340 */     return -1;
/*     */   }
/*     */ 
/*     */   public int addFloat(float n)
/*     */   {
/* 352 */     if ((ret = lookupFloat(n)) != -1) {
/* 353 */       return ret;
/*     */     }
/* 355 */     adjustSize();
/*     */ 
/* 357 */     int ret = this.index;
/* 358 */     this.constants[(this.index++)] = new ConstantFloat(n);
/*     */ 
/* 360 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupUtf8(String n)
/*     */   {
/* 372 */     Index index = (Index)this.utf8_table.get(n);
/*     */ 
/* 374 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int addUtf8(String n)
/*     */   {
/* 386 */     if ((ret = lookupUtf8(n)) != -1) {
/* 387 */       return ret;
/*     */     }
/* 389 */     adjustSize();
/*     */ 
/* 391 */     int ret = this.index;
/* 392 */     this.constants[(this.index++)] = new ConstantUtf8(n);
/*     */ 
/* 394 */     this.utf8_table.put(n, new Index(ret));
/*     */ 
/* 396 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupLong(long n)
/*     */   {
/* 406 */     for (int i = 1; i < this.index; i++) {
/* 407 */       if ((this.constants[i] instanceof ConstantLong)) {
/* 408 */         ConstantLong c = (ConstantLong)this.constants[i];
/*     */ 
/* 410 */         if (c.getBytes() == n) {
/* 411 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 415 */     return -1;
/*     */   }
/*     */ 
/*     */   public int addLong(long n)
/*     */   {
/* 427 */     if ((ret = lookupLong(n)) != -1) {
/* 428 */       return ret;
/*     */     }
/* 430 */     adjustSize();
/*     */ 
/* 432 */     int ret = this.index;
/* 433 */     this.constants[this.index] = new ConstantLong(n);
/* 434 */     this.index += 2;
/*     */ 
/* 436 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupDouble(double n)
/*     */   {
/* 446 */     long bits = Double.doubleToLongBits(n);
/*     */ 
/* 448 */     for (int i = 1; i < this.index; i++) {
/* 449 */       if ((this.constants[i] instanceof ConstantDouble)) {
/* 450 */         ConstantDouble c = (ConstantDouble)this.constants[i];
/*     */ 
/* 452 */         if (Double.doubleToLongBits(c.getBytes()) == bits) {
/* 453 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 457 */     return -1;
/*     */   }
/*     */ 
/*     */   public int addDouble(double n)
/*     */   {
/* 469 */     if ((ret = lookupDouble(n)) != -1) {
/* 470 */       return ret;
/*     */     }
/* 472 */     adjustSize();
/*     */ 
/* 474 */     int ret = this.index;
/* 475 */     this.constants[this.index] = new ConstantDouble(n);
/* 476 */     this.index += 2;
/*     */ 
/* 478 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupNameAndType(String name, String signature)
/*     */   {
/* 491 */     Index index = (Index)this.n_a_t_table.get(name + "%" + signature);
/* 492 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int addNameAndType(String name, String signature)
/*     */   {
/* 506 */     if ((ret = lookupNameAndType(name, signature)) != -1) {
/* 507 */       return ret;
/*     */     }
/* 509 */     adjustSize();
/*     */ 
/* 511 */     int name_index = addUtf8(name);
/* 512 */     int signature_index = addUtf8(signature);
/* 513 */     int ret = this.index;
/* 514 */     this.constants[(this.index++)] = new ConstantNameAndType(name_index, signature_index);
/*     */ 
/* 516 */     this.n_a_t_table.put(name + "%" + signature, new Index(ret));
/* 517 */     return ret;
/*     */   }
/*     */ 
/*     */   public int lookupMethodref(String class_name, String method_name, String signature)
/*     */   {
/* 531 */     Index index = (Index)this.cp_table.get(class_name + ":" + method_name + ":" + signature);
/*     */ 
/* 533 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int lookupMethodref(MethodGen method) {
/* 537 */     return lookupMethodref(method.getClassName(), method.getName(), method.getSignature());
/*     */   }
/*     */ 
/*     */   public int addMethodref(String class_name, String method_name, String signature)
/*     */   {
/* 551 */     if ((ret = lookupMethodref(class_name, method_name, signature)) != -1) {
/* 552 */       return ret;
/*     */     }
/* 554 */     adjustSize();
/*     */ 
/* 556 */     int name_and_type_index = addNameAndType(method_name, signature);
/* 557 */     int class_index = addClass(class_name);
/* 558 */     int ret = this.index;
/* 559 */     this.constants[(this.index++)] = new ConstantMethodref(class_index, name_and_type_index);
/*     */ 
/* 561 */     this.cp_table.put(class_name + ":" + method_name + ":" + signature, new Index(ret));
/*     */ 
/* 564 */     return ret;
/*     */   }
/*     */ 
/*     */   public int addMethodref(MethodGen method) {
/* 568 */     return addMethodref(method.getClassName(), method.getName(), method.getSignature());
/*     */   }
/*     */ 
/*     */   public int lookupInterfaceMethodref(String class_name, String method_name, String signature)
/*     */   {
/* 581 */     Index index = (Index)this.cp_table.get(class_name + "#" + method_name + "#" + signature);
/*     */ 
/* 583 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int lookupInterfaceMethodref(MethodGen method) {
/* 587 */     return lookupInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
/*     */   }
/*     */ 
/*     */   public int addInterfaceMethodref(String class_name, String method_name, String signature)
/*     */   {
/* 601 */     if ((ret = lookupInterfaceMethodref(class_name, method_name, signature)) != -1) {
/* 602 */       return ret;
/*     */     }
/* 604 */     adjustSize();
/*     */ 
/* 606 */     int class_index = addClass(class_name);
/* 607 */     int name_and_type_index = addNameAndType(method_name, signature);
/* 608 */     int ret = this.index;
/* 609 */     this.constants[(this.index++)] = new ConstantInterfaceMethodref(class_index, name_and_type_index);
/*     */ 
/* 611 */     this.cp_table.put(class_name + "#" + method_name + "#" + signature, new Index(ret));
/*     */ 
/* 614 */     return ret;
/*     */   }
/*     */ 
/*     */   public int addInterfaceMethodref(MethodGen method) {
/* 618 */     return addInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
/*     */   }
/*     */ 
/*     */   public int lookupFieldref(String class_name, String field_name, String signature)
/*     */   {
/* 631 */     Index index = (Index)this.cp_table.get(class_name + "&" + field_name + "&" + signature);
/*     */ 
/* 633 */     return index != null ? index.index : -1;
/*     */   }
/*     */ 
/*     */   public int addFieldref(String class_name, String field_name, String signature)
/*     */   {
/* 647 */     if ((ret = lookupFieldref(class_name, field_name, signature)) != -1) {
/* 648 */       return ret;
/*     */     }
/* 650 */     adjustSize();
/*     */ 
/* 652 */     int class_index = addClass(class_name);
/* 653 */     int name_and_type_index = addNameAndType(field_name, signature);
/* 654 */     int ret = this.index;
/* 655 */     this.constants[(this.index++)] = new ConstantFieldref(class_index, name_and_type_index);
/*     */ 
/* 657 */     this.cp_table.put(class_name + "&" + field_name + "&" + signature, new Index(ret));
/*     */ 
/* 659 */     return ret;
/*     */   }
/*     */ 
/*     */   public Constant getConstant(int i)
/*     */   {
/* 666 */     return this.constants[i];
/*     */   }
/*     */ 
/*     */   public void setConstant(int i, Constant c)
/*     */   {
/* 674 */     this.constants[i] = c;
/*     */   }
/*     */ 
/*     */   public ConstantPool getConstantPool()
/*     */   {
/* 680 */     return new ConstantPool(this.constants);
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 687 */     return this.index;
/*     */   }
/*     */ 
/*     */   public ConstantPool getFinalConstantPool()
/*     */   {
/* 694 */     Constant[] cs = new Constant[this.index];
/*     */ 
/* 696 */     System.arraycopy(this.constants, 0, cs, 0, this.index);
/*     */ 
/* 698 */     return new ConstantPool(cs);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 705 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 707 */     for (int i = 1; i < this.index; i++) {
/* 708 */       buf.append(i + ")" + this.constants[i] + "\n");
/*     */     }
/* 710 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public int addConstant(Constant c, ConstantPoolGen cp)
/*     */   {
/* 716 */     Constant[] constants = cp.getConstantPool().getConstantPool();
/*     */ 
/* 718 */     switch (c.getTag()) {
/*     */     case 8:
/* 720 */       ConstantString s = (ConstantString)c;
/* 721 */       ConstantUtf8 u8 = (ConstantUtf8)constants[s.getStringIndex()];
/*     */ 
/* 723 */       return addString(u8.getBytes());
/*     */     case 7:
/* 727 */       ConstantClass s = (ConstantClass)c;
/* 728 */       ConstantUtf8 u8 = (ConstantUtf8)constants[s.getNameIndex()];
/*     */ 
/* 730 */       return addClass(u8.getBytes());
/*     */     case 12:
/* 734 */       ConstantNameAndType n = (ConstantNameAndType)c;
/* 735 */       ConstantUtf8 u8 = (ConstantUtf8)constants[n.getNameIndex()];
/* 736 */       ConstantUtf8 u8_2 = (ConstantUtf8)constants[n.getSignatureIndex()];
/*     */ 
/* 738 */       return addNameAndType(u8.getBytes(), u8_2.getBytes());
/*     */     case 1:
/* 742 */       return addUtf8(((ConstantUtf8)c).getBytes());
/*     */     case 6:
/* 745 */       return addDouble(((ConstantDouble)c).getBytes());
/*     */     case 4:
/* 748 */       return addFloat(((ConstantFloat)c).getBytes());
/*     */     case 5:
/* 751 */       return addLong(((ConstantLong)c).getBytes());
/*     */     case 3:
/* 754 */       return addInteger(((ConstantInteger)c).getBytes());
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 758 */       ConstantCP m = (ConstantCP)c;
/* 759 */       ConstantClass clazz = (ConstantClass)constants[m.getClassIndex()];
/* 760 */       ConstantNameAndType n = (ConstantNameAndType)constants[m.getNameAndTypeIndex()];
/* 761 */       ConstantUtf8 u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
/* 762 */       String class_name = u8.getBytes().replace('/', '.');
/*     */ 
/* 764 */       u8 = (ConstantUtf8)constants[n.getNameIndex()];
/* 765 */       String name = u8.getBytes();
/*     */ 
/* 767 */       u8 = (ConstantUtf8)constants[n.getSignatureIndex()];
/* 768 */       String signature = u8.getBytes();
/*     */ 
/* 770 */       switch (c.getTag()) {
/*     */       case 11:
/* 772 */         return addInterfaceMethodref(class_name, name, signature);
/*     */       case 10:
/* 775 */         return addMethodref(class_name, name, signature);
/*     */       case 9:
/* 778 */         return addFieldref(class_name, name, signature);
/*     */       }
/*     */ 
/* 781 */       throw new RuntimeException("Unknown constant type " + c);
/*     */     case 2:
/*     */     }
/*     */ 
/* 786 */     throw new RuntimeException("Unknown constant type " + c);
/*     */   }
/*     */ 
/*     */   private static class Index
/*     */     implements Serializable
/*     */   {
/*     */     int index;
/*     */ 
/*     */     Index(int i)
/*     */     {
/*  90 */       this.index = i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.ConstantPoolGen
 * JD-Core Version:    0.6.2
 */