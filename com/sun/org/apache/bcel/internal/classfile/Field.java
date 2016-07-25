/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class Field extends FieldOrMethod
/*     */ {
/*     */   public Field(Field c)
/*     */   {
/*  76 */     super(c);
/*     */   }
/*     */ 
/*     */   Field(DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException, ClassFormatException
/*     */   {
/*  86 */     super(file, constant_pool);
/*     */   }
/*     */ 
/*     */   public Field(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool)
/*     */   {
/*  99 */     super(access_flags, name_index, signature_index, attributes, constant_pool);
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 110 */     v.visitField(this);
/*     */   }
/*     */ 
/*     */   public final ConstantValue getConstantValue()
/*     */   {
/* 117 */     for (int i = 0; i < this.attributes_count; i++) {
/* 118 */       if (this.attributes[i].getTag() == 1)
/* 119 */         return (ConstantValue)this.attributes[i];
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 134 */     String access = Utility.accessToString(this.access_flags);
/* 135 */     access = access + " ";
/* 136 */     String signature = Utility.signatureToString(getSignature());
/* 137 */     String name = getName();
/*     */ 
/* 139 */     StringBuffer buf = new StringBuffer(access + signature + " " + name);
/* 140 */     ConstantValue cv = getConstantValue();
/*     */ 
/* 142 */     if (cv != null) {
/* 143 */       buf.append(" = " + cv);
/*     */     }
/* 145 */     for (int i = 0; i < this.attributes_count; i++) {
/* 146 */       Attribute a = this.attributes[i];
/*     */ 
/* 148 */       if (!(a instanceof ConstantValue)) {
/* 149 */         buf.append(" [" + a.toString() + "]");
/*     */       }
/*     */     }
/* 152 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public final Field copy(ConstantPool constant_pool)
/*     */   {
/* 159 */     return (Field)copy_(constant_pool);
/*     */   }
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 166 */     return Type.getReturnType(getSignature());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Field
 * JD-Core Version:    0.6.2
 */