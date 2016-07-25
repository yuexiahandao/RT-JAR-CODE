/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ConstantPool
/*     */   implements Cloneable, Node, Serializable
/*     */ {
/*     */   private int constant_pool_count;
/*     */   private Constant[] constant_pool;
/*     */ 
/*     */   public ConstantPool(Constant[] constant_pool)
/*     */   {
/*  85 */     setConstantPool(constant_pool);
/*     */   }
/*     */ 
/*     */   ConstantPool(DataInputStream file)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  99 */     this.constant_pool_count = file.readUnsignedShort();
/* 100 */     this.constant_pool = new Constant[this.constant_pool_count];
/*     */ 
/* 105 */     for (int i = 1; i < this.constant_pool_count; i++) {
/* 106 */       this.constant_pool[i] = Constant.readConstant(file);
/*     */ 
/* 115 */       byte tag = this.constant_pool[i].getTag();
/* 116 */       if ((tag == 6) || (tag == 5))
/* 117 */         i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 129 */     v.visitConstantPool(this);
/*     */   }
/*     */ 
/*     */   public String constantToString(Constant c)
/*     */     throws ClassFormatException
/*     */   {
/* 143 */     byte tag = c.getTag();
/*     */     int i;
/*     */     String str;
/* 145 */     switch (tag) {
/*     */     case 7:
/* 147 */       i = ((ConstantClass)c).getNameIndex();
/* 148 */       c = getConstant(i, (byte)1);
/* 149 */       str = Utility.compactClassName(((ConstantUtf8)c).getBytes(), false);
/* 150 */       break;
/*     */     case 8:
/* 153 */       i = ((ConstantString)c).getStringIndex();
/* 154 */       c = getConstant(i, (byte)1);
/* 155 */       str = "\"" + escape(((ConstantUtf8)c).getBytes()) + "\"";
/* 156 */       break;
/*     */     case 1:
/* 158 */       str = ((ConstantUtf8)c).getBytes(); break;
/*     */     case 6:
/* 159 */       str = "" + ((ConstantDouble)c).getBytes(); break;
/*     */     case 4:
/* 160 */       str = "" + ((ConstantFloat)c).getBytes(); break;
/*     */     case 5:
/* 161 */       str = "" + ((ConstantLong)c).getBytes(); break;
/*     */     case 3:
/* 162 */       str = "" + ((ConstantInteger)c).getBytes(); break;
/*     */     case 12:
/* 165 */       str = constantToString(((ConstantNameAndType)c).getNameIndex(), (byte)1) + " " + constantToString(((ConstantNameAndType)c).getSignatureIndex(), (byte)1);
/*     */ 
/* 169 */       break;
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 173 */       str = constantToString(((ConstantCP)c).getClassIndex(), (byte)7) + "." + constantToString(((ConstantCP)c).getNameAndTypeIndex(), (byte)12);
/*     */ 
/* 177 */       break;
/*     */     case 2:
/*     */     default:
/* 180 */       throw new RuntimeException("Unknown constant type " + tag);
/*     */     }
/*     */ 
/* 183 */     return str;
/*     */   }
/*     */ 
/*     */   private static final String escape(String str) {
/* 187 */     int len = str.length();
/* 188 */     StringBuffer buf = new StringBuffer(len + 5);
/* 189 */     char[] ch = str.toCharArray();
/*     */ 
/* 191 */     for (int i = 0; i < len; i++) {
/* 192 */       switch (ch[i]) { case '\n':
/* 193 */         buf.append("\\n"); break;
/*     */       case '\r':
/* 194 */         buf.append("\\r"); break;
/*     */       case '\t':
/* 195 */         buf.append("\\t"); break;
/*     */       case '\b':
/* 196 */         buf.append("\\b"); break;
/*     */       case '"':
/* 197 */         buf.append("\\\""); break;
/*     */       default:
/* 198 */         buf.append(ch[i]);
/*     */       }
/*     */     }
/*     */ 
/* 202 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public String constantToString(int index, byte tag)
/*     */     throws ClassFormatException
/*     */   {
/* 217 */     Constant c = getConstant(index, tag);
/* 218 */     return constantToString(c);
/*     */   }
/*     */ 
/*     */   public void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 229 */     file.writeShort(this.constant_pool_count);
/*     */ 
/* 231 */     for (int i = 1; i < this.constant_pool_count; i++)
/* 232 */       if (this.constant_pool[i] != null)
/* 233 */         this.constant_pool[i].dump(file);
/*     */   }
/*     */ 
/*     */   public Constant getConstant(int index)
/*     */   {
/* 244 */     if ((index >= this.constant_pool.length) || (index < 0)) {
/* 245 */       throw new ClassFormatException("Invalid constant pool reference: " + index + ". Constant pool size is: " + this.constant_pool.length);
/*     */     }
/*     */ 
/* 248 */     return this.constant_pool[index];
/*     */   }
/*     */ 
/*     */   public Constant getConstant(int index, byte tag)
/*     */     throws ClassFormatException
/*     */   {
/* 266 */     Constant c = getConstant(index);
/*     */ 
/* 268 */     if (c == null) {
/* 269 */       throw new ClassFormatException("Constant pool at index " + index + " is null.");
/*     */     }
/* 271 */     if (c.getTag() == tag) {
/* 272 */       return c;
/*     */     }
/* 274 */     throw new ClassFormatException("Expected class `" + com.sun.org.apache.bcel.internal.Constants.CONSTANT_NAMES[tag] + "' at index " + index + " and got " + c);
/*     */   }
/*     */ 
/*     */   public Constant[] getConstantPool()
/*     */   {
/* 282 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public String getConstantString(int index, byte tag)
/*     */     throws ClassFormatException
/*     */   {
/* 302 */     Constant c = getConstant(index, tag);
/*     */     int i;
/* 311 */     switch (tag) { case 7:
/* 312 */       i = ((ConstantClass)c).getNameIndex(); break;
/*     */     case 8:
/* 313 */       i = ((ConstantString)c).getStringIndex(); break;
/*     */     default:
/* 315 */       throw new RuntimeException("getConstantString called with illegal tag " + tag);
/*     */     }
/*     */ 
/* 319 */     c = getConstant(i, (byte)1);
/* 320 */     return ((ConstantUtf8)c).getBytes();
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 327 */     return this.constant_pool_count;
/*     */   }
/*     */ 
/*     */   public void setConstant(int index, Constant constant)
/*     */   {
/* 334 */     this.constant_pool[index] = constant;
/*     */   }
/*     */ 
/*     */   public void setConstantPool(Constant[] constant_pool)
/*     */   {
/* 341 */     this.constant_pool = constant_pool;
/* 342 */     this.constant_pool_count = (constant_pool == null ? 0 : constant_pool.length);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 348 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 350 */     for (int i = 1; i < this.constant_pool_count; i++) {
/* 351 */       buf.append(i + ")" + this.constant_pool[i] + "\n");
/*     */     }
/* 353 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public ConstantPool copy()
/*     */   {
/* 360 */     ConstantPool c = null;
/*     */     try
/*     */     {
/* 363 */       c = (ConstantPool)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 366 */     c.constant_pool = new Constant[this.constant_pool_count];
/*     */ 
/* 368 */     for (int i = 1; i < this.constant_pool_count; i++) {
/* 369 */       if (this.constant_pool[i] != null) {
/* 370 */         c.constant_pool[i] = this.constant_pool[i].copy();
/*     */       }
/*     */     }
/* 373 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.ConstantPool
 * JD-Core Version:    0.6.2
 */