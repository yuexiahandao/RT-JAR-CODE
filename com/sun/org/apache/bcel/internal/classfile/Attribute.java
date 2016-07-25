/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public abstract class Attribute
/*     */   implements Cloneable, Node, Serializable
/*     */ {
/*     */   protected int name_index;
/*     */   protected int length;
/*     */   protected byte tag;
/*     */   protected ConstantPool constant_pool;
/* 121 */   private static HashMap readers = new HashMap();
/*     */ 
/*     */   protected Attribute(byte tag, int name_index, int length, ConstantPool constant_pool)
/*     */   {
/*  94 */     this.tag = tag;
/*  95 */     this.name_index = name_index;
/*  96 */     this.length = length;
/*  97 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public abstract void accept(Visitor paramVisitor);
/*     */ 
/*     */   public void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 117 */     file.writeShort(this.name_index);
/* 118 */     file.writeInt(this.length);
/*     */   }
/*     */ 
/*     */   public static void addAttributeReader(String name, AttributeReader r)
/*     */   {
/* 131 */     readers.put(name, r);
/*     */   }
/*     */ 
/*     */   public static void removeAttributeReader(String name)
/*     */   {
/* 139 */     readers.remove(name);
/*     */   }
/*     */ 
/*     */   public static final Attribute readAttribute(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException, ClassFormatException
/*     */   {
/* 162 */     byte tag = -1;
/*     */ 
/* 165 */     int name_index = file.readUnsignedShort();
/* 166 */     ConstantUtf8 c = (ConstantUtf8)constant_pool.getConstant(name_index, (byte)1);
/*     */ 
/* 168 */     String name = c.getBytes();
/*     */ 
/* 171 */     int length = file.readInt();
/*     */ 
/* 174 */     for (byte i = 0; i < 13; i = (byte)(i + 1)) {
/* 175 */       if (name.equals(com.sun.org.apache.bcel.internal.Constants.ATTRIBUTE_NAMES[i])) {
/* 176 */         tag = i;
/* 177 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 182 */     switch (tag) {
/*     */     case -1:
/* 184 */       AttributeReader r = (AttributeReader)readers.get(name);
/*     */ 
/* 186 */       if (r != null) {
/* 187 */         return r.createAttribute(name_index, length, file, constant_pool);
/*     */       }
/* 189 */       return new Unknown(name_index, length, file, constant_pool);
/*     */     case 1:
/* 192 */       return new ConstantValue(name_index, length, file, constant_pool);
/*     */     case 0:
/* 195 */       return new SourceFile(name_index, length, file, constant_pool);
/*     */     case 2:
/* 198 */       return new Code(name_index, length, file, constant_pool);
/*     */     case 3:
/* 201 */       return new ExceptionTable(name_index, length, file, constant_pool);
/*     */     case 4:
/* 204 */       return new LineNumberTable(name_index, length, file, constant_pool);
/*     */     case 5:
/* 207 */       return new LocalVariableTable(name_index, length, file, constant_pool);
/*     */     case 12:
/* 210 */       return new LocalVariableTypeTable(name_index, length, file, constant_pool);
/*     */     case 6:
/* 213 */       return new InnerClasses(name_index, length, file, constant_pool);
/*     */     case 7:
/* 216 */       return new Synthetic(name_index, length, file, constant_pool);
/*     */     case 8:
/* 219 */       return new Deprecated(name_index, length, file, constant_pool);
/*     */     case 9:
/* 222 */       return new PMGClass(name_index, length, file, constant_pool);
/*     */     case 10:
/* 225 */       return new Signature(name_index, length, file, constant_pool);
/*     */     case 11:
/* 228 */       return new StackMap(name_index, length, file, constant_pool);
/*     */     }
/*     */ 
/* 231 */     throw new IllegalStateException("Ooops! default case reached.");
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/* 238 */     return this.length;
/*     */   }
/*     */ 
/*     */   public final void setLength(int length)
/*     */   {
/* 244 */     this.length = length;
/*     */   }
/*     */ 
/*     */   public final void setNameIndex(int name_index)
/*     */   {
/* 251 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public final int getNameIndex()
/*     */   {
/* 257 */     return this.name_index;
/*     */   }
/*     */ 
/*     */   public final byte getTag()
/*     */   {
/* 263 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public final ConstantPool getConstantPool()
/*     */   {
/* 269 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public final void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 276 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 286 */     Object o = null;
/*     */     try
/*     */     {
/* 289 */       o = super.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 291 */       e.printStackTrace();
/*     */     }
/*     */ 
/* 294 */     return o;
/*     */   }
/*     */ 
/*     */   public abstract Attribute copy(ConstantPool paramConstantPool);
/*     */ 
/*     */   public String toString()
/*     */   {
/* 306 */     return com.sun.org.apache.bcel.internal.Constants.ATTRIBUTE_NAMES[this.tag];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Attribute
 * JD-Core Version:    0.6.2
 */