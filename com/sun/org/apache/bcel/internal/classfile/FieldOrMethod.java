/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class FieldOrMethod extends AccessFlags
/*     */   implements Cloneable, Node
/*     */ {
/*     */   protected int name_index;
/*     */   protected int signature_index;
/*     */   protected int attributes_count;
/*     */   protected Attribute[] attributes;
/*     */   protected ConstantPool constant_pool;
/*     */ 
/*     */   FieldOrMethod()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected FieldOrMethod(FieldOrMethod c)
/*     */   {
/*  82 */     this(c.getAccessFlags(), c.getNameIndex(), c.getSignatureIndex(), c.getAttributes(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   protected FieldOrMethod(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  95 */     this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), null, constant_pool);
/*     */ 
/*  98 */     this.attributes_count = file.readUnsignedShort();
/*  99 */     this.attributes = new Attribute[this.attributes_count];
/* 100 */     for (int i = 0; i < this.attributes_count; i++)
/* 101 */       this.attributes[i] = Attribute.readAttribute(file, constant_pool);
/*     */   }
/*     */ 
/*     */   protected FieldOrMethod(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool)
/*     */   {
/* 114 */     this.access_flags = access_flags;
/* 115 */     this.name_index = name_index;
/* 116 */     this.signature_index = signature_index;
/* 117 */     this.constant_pool = constant_pool;
/*     */ 
/* 119 */     setAttributes(attributes);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 130 */     file.writeShort(this.access_flags);
/* 131 */     file.writeShort(this.name_index);
/* 132 */     file.writeShort(this.signature_index);
/* 133 */     file.writeShort(this.attributes_count);
/*     */ 
/* 135 */     for (int i = 0; i < this.attributes_count; i++)
/* 136 */       this.attributes[i].dump(file);
/*     */   }
/*     */ 
/*     */   public final Attribute[] getAttributes()
/*     */   {
/* 142 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public final void setAttributes(Attribute[] attributes)
/*     */   {
/* 148 */     this.attributes = attributes;
/* 149 */     this.attributes_count = (attributes == null ? 0 : attributes.length);
/*     */   }
/*     */ 
/*     */   public final ConstantPool getConstantPool()
/*     */   {
/* 155 */     return this.constant_pool;
/*     */   }
/*     */ 
/*     */   public final void setConstantPool(ConstantPool constant_pool)
/*     */   {
/* 161 */     this.constant_pool = constant_pool;
/*     */   }
/*     */ 
/*     */   public final int getNameIndex()
/*     */   {
/* 167 */     return this.name_index;
/*     */   }
/*     */ 
/*     */   public final void setNameIndex(int name_index)
/*     */   {
/* 173 */     this.name_index = name_index;
/*     */   }
/*     */ 
/*     */   public final int getSignatureIndex()
/*     */   {
/* 179 */     return this.signature_index;
/*     */   }
/*     */ 
/*     */   public final void setSignatureIndex(int signature_index)
/*     */   {
/* 185 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 193 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, (byte)1);
/*     */ 
/* 195 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   public final String getSignature()
/*     */   {
/* 203 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
/*     */ 
/* 205 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   protected FieldOrMethod copy_(ConstantPool constant_pool)
/*     */   {
/* 212 */     FieldOrMethod c = null;
/*     */     try
/*     */     {
/* 215 */       c = (FieldOrMethod)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 218 */     c.constant_pool = constant_pool;
/* 219 */     c.attributes = new Attribute[this.attributes_count];
/*     */ 
/* 221 */     for (int i = 0; i < this.attributes_count; i++) {
/* 222 */       c.attributes[i] = this.attributes[i].copy(constant_pool);
/*     */     }
/* 224 */     return c;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.FieldOrMethod
 * JD-Core Version:    0.6.2
 */