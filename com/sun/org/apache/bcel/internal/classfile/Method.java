/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class Method extends FieldOrMethod
/*     */ {
/*     */   public Method()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Method(Method c)
/*     */   {
/*  83 */     super(c);
/*     */   }
/*     */ 
/*     */   Method(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  95 */     super(file, constant_pool);
/*     */   }
/*     */ 
/*     */   public Method(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool)
/*     */   {
/* 108 */     super(access_flags, name_index, signature_index, attributes, constant_pool);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 119 */     v.visitMethod(this);
/*     */   }
/*     */ 
/*     */   public final Code getCode()
/*     */   {
/* 126 */     for (int i = 0; i < this.attributes_count; i++) {
/* 127 */       if ((this.attributes[i] instanceof Code))
/* 128 */         return (Code)this.attributes[i];
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public final ExceptionTable getExceptionTable()
/*     */   {
/* 138 */     for (int i = 0; i < this.attributes_count; i++) {
/* 139 */       if ((this.attributes[i] instanceof ExceptionTable))
/* 140 */         return (ExceptionTable)this.attributes[i];
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   public final LocalVariableTable getLocalVariableTable()
/*     */   {
/* 149 */     Code code = getCode();
/*     */ 
/* 151 */     if (code != null) {
/* 152 */       return code.getLocalVariableTable();
/*     */     }
/* 154 */     return null;
/*     */   }
/*     */ 
/*     */   public final LineNumberTable getLineNumberTable()
/*     */   {
/* 161 */     Code code = getCode();
/*     */ 
/* 163 */     if (code != null) {
/* 164 */       return code.getLineNumberTable();
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 180 */     String access = Utility.accessToString(this.access_flags);
/*     */ 
/* 183 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
/*     */ 
/* 185 */     String signature = c.getBytes();
/*     */ 
/* 187 */     c = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, (byte)1);
/* 188 */     String name = c.getBytes();
/*     */ 
/* 190 */     signature = Utility.methodSignatureToString(signature, name, access, true, getLocalVariableTable());
/*     */ 
/* 192 */     StringBuffer buf = new StringBuffer(signature);
/*     */ 
/* 194 */     for (int i = 0; i < this.attributes_count; i++) {
/* 195 */       Attribute a = this.attributes[i];
/*     */ 
/* 197 */       if ((!(a instanceof Code)) && (!(a instanceof ExceptionTable))) {
/* 198 */         buf.append(" [" + a.toString() + "]");
/*     */       }
/*     */     }
/* 201 */     ExceptionTable e = getExceptionTable();
/* 202 */     if (e != null) {
/* 203 */       String str = e.toString();
/* 204 */       if (!str.equals("")) {
/* 205 */         buf.append("\n\t\tthrows " + str);
/*     */       }
/*     */     }
/* 208 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public final Method copy(ConstantPool constant_pool)
/*     */   {
/* 215 */     return (Method)copy_(constant_pool);
/*     */   }
/*     */ 
/*     */   public Type getReturnType()
/*     */   {
/* 222 */     return Type.getReturnType(getSignature());
/*     */   }
/*     */ 
/*     */   public Type[] getArgumentTypes()
/*     */   {
/* 229 */     return Type.getArgumentTypes(getSignature());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Method
 * JD-Core Version:    0.6.2
 */