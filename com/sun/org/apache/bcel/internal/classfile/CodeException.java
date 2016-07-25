/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.Constants;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public final class CodeException
/*     */   implements Cloneable, Constants, Node, Serializable
/*     */ {
/*     */   private int start_pc;
/*     */   private int end_pc;
/*     */   private int handler_pc;
/*     */   private int catch_type;
/*     */ 
/*     */   public CodeException(CodeException c)
/*     */   {
/*  88 */     this(c.getStartPC(), c.getEndPC(), c.getHandlerPC(), c.getCatchType());
/*     */   }
/*     */ 
/*     */   CodeException(DataInputStream file)
/*     */     throws IOException
/*     */   {
/*  98 */     this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort());
/*     */   }
/*     */ 
/*     */   public CodeException(int start_pc, int end_pc, int handler_pc, int catch_type)
/*     */   {
/* 115 */     this.start_pc = start_pc;
/* 116 */     this.end_pc = end_pc;
/* 117 */     this.handler_pc = handler_pc;
/* 118 */     this.catch_type = catch_type;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 129 */     v.visitCodeException(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 139 */     file.writeShort(this.start_pc);
/* 140 */     file.writeShort(this.end_pc);
/* 141 */     file.writeShort(this.handler_pc);
/* 142 */     file.writeShort(this.catch_type);
/*     */   }
/*     */ 
/*     */   public final int getCatchType()
/*     */   {
/* 149 */     return this.catch_type;
/*     */   }
/*     */ 
/*     */   public final int getEndPC()
/*     */   {
/* 154 */     return this.end_pc;
/*     */   }
/*     */ 
/*     */   public final int getHandlerPC()
/*     */   {
/* 159 */     return this.handler_pc;
/*     */   }
/*     */ 
/*     */   public final int getStartPC()
/*     */   {
/* 164 */     return this.start_pc;
/*     */   }
/*     */ 
/*     */   public final void setCatchType(int catch_type)
/*     */   {
/* 170 */     this.catch_type = catch_type;
/*     */   }
/*     */ 
/*     */   public final void setEndPC(int end_pc)
/*     */   {
/* 177 */     this.end_pc = end_pc;
/*     */   }
/*     */ 
/*     */   public final void setHandlerPC(int handler_pc)
/*     */   {
/* 184 */     this.handler_pc = handler_pc;
/*     */   }
/*     */ 
/*     */   public final void setStartPC(int start_pc)
/*     */   {
/* 191 */     this.start_pc = start_pc;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 198 */     return "CodeException(start_pc = " + this.start_pc + ", end_pc = " + this.end_pc + ", handler_pc = " + this.handler_pc + ", catch_type = " + this.catch_type + ")";
/*     */   }
/*     */ 
/*     */   public final String toString(ConstantPool cp, boolean verbose)
/*     */   {
/*     */     String str;
/*     */     String str;
/* 209 */     if (this.catch_type == 0)
/* 210 */       str = "<Any exception>(0)";
/*     */     else {
/* 212 */       str = Utility.compactClassName(cp.getConstantString(this.catch_type, (byte)7), false) + (verbose ? "(" + this.catch_type + ")" : "");
/*     */     }
/*     */ 
/* 215 */     return this.start_pc + "\t" + this.end_pc + "\t" + this.handler_pc + "\t" + str;
/*     */   }
/*     */ 
/*     */   public final String toString(ConstantPool cp) {
/* 219 */     return toString(cp, true);
/*     */   }
/*     */ 
/*     */   public CodeException copy()
/*     */   {
/*     */     try
/*     */     {
/* 227 */       return (CodeException)clone();
/*     */     } catch (CloneNotSupportedException e) {
/*     */     }
/* 230 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.CodeException
 * JD-Core Version:    0.6.2
 */